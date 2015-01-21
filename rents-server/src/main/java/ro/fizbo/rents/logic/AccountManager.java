package ro.fizbo.rents.logic;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import ro.fizbo.rents.dao.AccountDAO;
import ro.fizbo.rents.dao.TokenDAO;
import ro.fizbo.rents.dto.AccountUpdate;
import ro.fizbo.rents.listener.ApplicationManager;
import ro.fizbo.rents.model.Account;
import ro.fizbo.rents.model.Token;
import ro.fizbo.rents.rest.client.FacebookClient;
import ro.fizbo.rents.util.PasswordHashing;
import ro.fizbo.rents.webservice.exception.AccountConflictException;
import ro.fizbo.rents.webservice.exception.AuthenticationException;
import ro.fizbo.rents.webservice.exception.OperationFailedException;
import ro.fizbo.rents.webservice.exception.UnauthorizedException;

public final class AccountManager {
	
	private static Logger logger = Logger.getLogger(AccountManager.class);
	
	public static Account facebookLogin(Account account, boolean isIosClient) {
		logger.info("Validate facebook access token for " + account.getAccountEmail());
		final boolean isTokenAccessValid = FacebookClient.validateUserAccessToken(
				account.getAccountExternalId(), account.getTokenKey(), isIosClient);
		if(!isTokenAccessValid) {
			throw new UnauthorizedException();
		}
		
		logger.info("Facebook login for " + account.getAccountEmail());
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			AccountDAO accountDAO = session.getMapper(AccountDAO.class);
			Account existingAccount = accountDAO.getAccountByEmail(account.getAccountEmail());
			if(existingAccount != null) {
				if(existingAccount.getAccountExternalId() == null) {
					existingAccount.setAccountExternalId(account.getAccountExternalId());
					existingAccount.setAccountFirstname(account.getAccountFirstname());
					
					session.update("AccountMapper.updateAccountExternalInfo", existingAccount);
				} else if(!existingAccount.getAccountExternalId()
							.equals(account.getAccountExternalId())) {
					throw new UnauthorizedException();
				}
				
				// update token
				TokenDAO tokenDAO = session.getMapper(TokenDAO.class);
				Token token = tokenDAO.getTokenByAccountId(existingAccount.getAccountId());
				int insertCount = -1;
				int retry = 0;
				while(insertCount != 1 && retry < 3) {
					insertCount = tokenDAO.updateTokenKey(token.getTokenId(), account.getTokenKey(),
							new Date());
					++retry;
				}

				session.commit();

				existingAccount.setTokenKey(account.getTokenKey());
				clearSensitiveData(existingAccount);
				
				return existingAccount;
			}
		} catch (UnauthorizedException ue) {
			logger.error("Facebook stored id is different from provided id for account "
					+ account.getAccountEmail());
			
			throw ue;
		}  catch (RuntimeException runtimeException) {
			logger.error("Unable to update facebook account information for " + account.getAccountEmail(),
					runtimeException);
			session.rollback();

			throw new OperationFailedException();
		} finally {
			session.close();
		}

		Token token = new Token();
		token.setTokenKey(account.getTokenKey());
		token.setTokenCreationDate(new Date());
		
		return createAccount(account, token, false);
	}
	
	public static Account createAccount(Account account) {
		// Create a token to be linked with the account.
		Token token = new Token();
		token.setTokenKey(TokenGenerator.generateToken());
		token.setTokenCreationDate(new Date());
		
		String hashedPassword = createHashedPassword(account.getAccountPassword());
		account.setAccountPassword(hashedPassword);
		
		return createAccount(account, token, true);
	}
	
	private static Account createAccount(Account account, Token token, boolean verifyExisting) {
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			AccountDAO accountDAO = session.getMapper(AccountDAO.class);
			if(verifyExisting) {
				List<Account> existingAccounts = 
						accountDAO.getAccountByEmailOrPhone(account.getAccountEmail(),
								account.getAccountPhone());
				if(existingAccounts != null && existingAccounts.size() > 0) {
					throw new AccountConflictException();
				}
			}
			
			// Create account.
			logger.info("Create account with email " + account.getAccountEmail());

			if(account.getAccountPhone() != null && account.getAccountPhone().trim().equals("")) {
				account.setAccountPhone(null);
			}
			
			int insertCount = -1;
			int retry = 0;
			while (insertCount != 1 && retry < 3) {
				insertCount = accountDAO.insertAccount(account);
				++retry;
			}

			// Save token for account.
			if (account.getAccountId() != null) {
				token.setAccountId(account.getAccountId());
				TokenDAO tokenDAO = session.getMapper(TokenDAO.class);
				
				insertCount = -1;
				retry = 0;
				while(insertCount != 1 && retry < 3) {
					insertCount = tokenDAO.insertToken(token);
					++retry;
				}
			}

			session.commit();
		} catch (AccountConflictException accountConflictException) {
			logger.error("Email or phone are already registered. Failed to create account for email"
					+ account.getAccountEmail());
			
			throw accountConflictException;
		} catch (RuntimeException runtimeException) {
			logger.error("Unable to create account with email " + account.getAccountEmail(),
					runtimeException);
			session.rollback();

			throw new OperationFailedException();
		} finally {
			session.close();
		}

		account.setTokenKey(token.getTokenKey());
		clearSensitiveData(account);
		
		return account;
	}

	public static Account login(String email, String password) {
		Account account = loginWithoutClearingSensitiveData(email, password);
		clearSensitiveData(account);
		
		return account;
	}
	
	public static Account loginWithoutClearingSensitiveData(String email, String password) {
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		Account account = null;
		try {
			AccountDAO accountDAO = session.getMapper(AccountDAO.class);
			account = accountDAO.getAccountByEmail(email);
		} catch (RuntimeException runtimeException) {
			logger.error("Unable to authenticate account with email " + account.getAccountEmail(),
					runtimeException);
			session.rollback();

			throw new OperationFailedException();
		} finally {
			session.close();
		}

		if(account == null || !isPasswordValid(password, account.getAccountPassword())) {
			throw new AuthenticationException();
		}
		
		// Set password field to null. We use the token for authorization.
		String tokenKey = TokenManager.getNewTokenKey(account.getAccountId());
		account.setTokenKey(tokenKey);
		
		return account;
	}
	
	public static Account updateAccount(AccountUpdate accountUpdate) {
		Account editedAccount = accountUpdate.account;
		Account originalAccount = 
				loginWithoutClearingSensitiveData(accountUpdate.accountEmail, 
						editedAccount.getAccountPassword());
		
		return updateAccountInfo(originalAccount, editedAccount, false);
	
	}
	
	public static Account updateExternalAccount(Account editedAccount, boolean isIosClient) {
		logger.info("Validate facebook access token for account " 
				+ editedAccount.getAccountEmail());
		final boolean isTokenAccessValid = FacebookClient.validateUserAccessToken(
				editedAccount.getAccountExternalId(), editedAccount.getTokenKey(), isIosClient);
		if(!isTokenAccessValid) {
			throw new UnauthorizedException();
		}
		
		logger.info("Facebook login by externalId for " + editedAccount.getAccountEmail());
		Account originalAccount = externalLogin(editedAccount.getAccountEmail(), 
				editedAccount.getAccountExternalId(), editedAccount.getTokenKey());
		
		return updateAccountInfo(originalAccount, editedAccount, true);
	}
	
	private static Account updateAccountInfo(Account originalAccount, Account editedAccount, 
			boolean isExternalAccount) {
		logger.info("Update account " + editedAccount.getAccountEmail());
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		int updateCount = -1;
		try {
			AccountDAO accountDAO = session.getMapper(AccountDAO.class);

			List<Account> existingAccounts = 
					accountDAO.getAccountByEmailOrPhoneRestrictById(editedAccount.getAccountEmail(),
							editedAccount.getAccountPhone(), originalAccount.getAccountId());
			if(existingAccounts != null && existingAccounts.size() > 0) {
				throw new AccountConflictException();
			}

			if(editedAccount.getAccountPhone() != null 
					&& editedAccount.getAccountPhone().trim().equals("")){
				editedAccount.setAccountPhone(null);
			}
			editedAccount.setAccountId(originalAccount.getAccountId());
			if(!isExternalAccount) {
				updateCount = session.update("AccountMapper.updateAccount", editedAccount);
			} else {
				updateCount = session.update("AccountMapper.updateExternalAccount", editedAccount);
			}
			
			session.commit();
		} catch (AccountConflictException ace) {
			logger.error("Unable to update account. There is already an account with email " 
					+ editedAccount.getAccountEmail() + " or phone " + editedAccount.getAccountPhone());
			
			throw ace;
		} catch (RuntimeException runtimeException) {
			logger.error("Unable to update account with email " + originalAccount.getAccountEmail(),
					runtimeException);
			session.rollback();

			throw new OperationFailedException();
		} finally {
			session.close();
		}
		
		if(updateCount != 1) {
			logger.error("An error occured while updating account '" 
					+ originalAccount.getAccountEmail());
			
			throw new OperationFailedException();
		}

		editedAccount.setTokenKey(originalAccount.getTokenKey());
		clearSensitiveData(editedAccount);
		
		return editedAccount;
	}
	
	private static Account externalLogin(String accountEmail, String externalId, 
			String accessToken) {
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		Account account = null;
		try {
			AccountDAO accountDAO = session.getMapper(AccountDAO.class);
			account = accountDAO.getExternalAccountByEmail(accountEmail);
		} catch (RuntimeException runtimeException) {
			logger.error("Unable to authenticate account " + accountEmail, runtimeException);
			session.rollback();

			throw new OperationFailedException();
		} finally {
			session.close();
		}

		if(account == null || !account.getAccountExternalId().equals(externalId) 
				|| !account.getTokenKey().equals(accessToken)) {
			throw new UnauthorizedException();
		}
		
		return account;
	}
	
	public static String changePassword(String email, String password, String newPassword) {
		String tokenKey = TokenGenerator.generateToken();
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		int updatesCount = -1;
		try {
			AccountDAO accountDAO = session.getMapper(AccountDAO.class);
			Account account = accountDAO.getAccountByEmail(email);
			
			if(account == null || !isPasswordValid(password, account.getAccountPassword())) {
				throw new AuthenticationException();
			}
			
			String hashedNewPassword = createHashedPassword(newPassword);

			int retry = 0;
			while(updatesCount != 2 && retry < 3) {
				updatesCount = accountDAO.updatePassword(account.getAccountId(),  hashedNewPassword, 
						tokenKey, new Date());
				++retry;
			}
			
			session.commit();
		} catch (RuntimeException runtimeException) {
			logger.error("Unable to update password for account with email " + email,
					runtimeException);
			session.rollback();

			throw new OperationFailedException();
		} finally {
			if(updatesCount != 2) {
				session.rollback();
			}

			session.close();
		}

		if(updatesCount != 2) {
			throw new OperationFailedException();
		}
		
		return tokenKey;
	}
	
	public static void clearSensitiveData(Account account) {
		account.setAccountId(null);
		account.setAccountPassword(null);
	}
	
	private static boolean isPasswordValid(String providedPassword, String accountPassword) {
		if(providedPassword == null || accountPassword == null) {
			return false;
		}
		
		boolean isPasswordValid = false;
		try {
			isPasswordValid = PasswordHashing.validatePassword(providedPassword, accountPassword);
		} catch (NoSuchAlgorithmException e) {
			throw new OperationFailedException();
		} catch (InvalidKeySpecException e) {
			throw new OperationFailedException();
		}
		
		return isPasswordValid;
	}
	
	private static String createHashedPassword(String password) {
		String hashedPassword = null;
		try {
			hashedPassword = PasswordHashing.createHashString(password);
		} catch (NoSuchAlgorithmException e) {
			throw new OperationFailedException();
		} catch (InvalidKeySpecException e) {
			throw new OperationFailedException();
		}
		
		return hashedPassword;
	}
}
