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
import ro.fizbo.rents.util.PasswordHashing;
import ro.fizbo.rents.webservice.exception.AccountConflictException;
import ro.fizbo.rents.webservice.exception.AuthenticationException;
import ro.fizbo.rents.webservice.exception.OperationFailedException;

public final class AccountManager {
	
	private static Logger logger = Logger.getLogger(AccountManager.class);
	
	public static Account createAccount(Account account) {
		// Create a token to be linked with the account.
		Token token = new Token();
		token.setTokenKey(TokenGenerator.generateToken());
		token.setTokenCreationDate(new Date());
		
		String hashedPassword = createHashedPassword(account.getAccountPassword());
		account.setAccountPassword(hashedPassword);
		
		SqlSession session = ApplicationManager.getSqlSessionFactory().openSession();
		try {
			AccountDAO accountDAO = session.getMapper(AccountDAO.class);
			List<Account> existingAccounts = 
					accountDAO.getAccountByEmailOrPhone(account.getAccountEmail(),
							account.getAccountPhone());
			if(existingAccounts != null && existingAccounts.size() > 0) {
				throw new AccountConflictException();
			}
			
			// Create account.
			logger.info("Create account with email " + account.getAccountEmail());
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
				loginWithoutClearingSensitiveData(accountUpdate.accountEmail, editedAccount.getAccountPassword());
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

			editedAccount.setAccountId(originalAccount.getAccountId());
			updateCount = session.update("AccountMapper.updateAccount", editedAccount);

			session.commit();
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
