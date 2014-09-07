package ro.fizbo.rents.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import ro.fizbo.rents.model.Account;

public interface AccountDAO {
	
	public static final String INSERT= "insert into account (accountType, accountExternalId,"
			+ "accountEmail, accountPassword, accountFirstname, accountLastname, accountPhone,"
			+ "accountSignupDate) values(#{accountType}, #{accountExternalId}, #{accountEmail},"
			+ "#{accountPassword}, #{accountFirstname}, #{accountLastname}, #{accountPhone},"
			+ "#{accountSignupDate})";
	
	public static final String SELECT_BY_ID = "select account.*, token.tokenKey as tokenKey"
			+ " from account left join token on token.accountId=account.accountId"
			+ " where account.accountId=#{accountId}";
	
	public static final String SELECT_EXTERNAL_BY_EMAIL = "select account.*, token.tokenKey as tokenKey"
			+ " from account inner join token on token.accountId=account.accountId"
			+ " where account.accountEmail=#{accountEmail}";
	
	public static final String DELETE_BY_ID = "delete from account where account.accountId="
			+ "#{accountId}";
	
	public static final String DELETE_BY_EMAIL = "delete from account where account.accountEmail="
			+ "#{accountEmail}";
	
	public static final String SELECT_BY_EMAIL = "select account.* from account "
			+ " where account.accountEmail=#{email}";
	
	public static final String SELECT_BY_EMAIL_OR_PHONE = "select account.* from account"
			+ " where account.accountEmail=#{accountEmail} or account.accountPhone=#{accountPhone}";
	
	public static final String SELECT_BY_EMAIL_OR_PHONE_RESTRICT_BY_ID = "select account.* from account"
			+ " where (account.accountEmail=#{accountEmail} or account.accountPhone=#{accountPhone})"
			+ " and account.accountId!=#{accountId}";
	
	public static final String UPDATE_PASSWORD_AND_TOKEN = "update account left join token"
			+ " on token.accountId=account.accountId set account.accountPassword=#{password},"
			+ " token.tokenKey=#{tokenKey}, token.tokenCreationDate=#{creationDate}"
			+ " where account.accountId=#{accountId}";

	@Insert(INSERT)
	@Options(useGeneratedKeys=true, keyProperty="accountId")
	public int insertAccount(Account account);
	
	@Select(SELECT_BY_ID)
	public Account getAccountById(@Param("accountId") int accountId);
	
	@Select(SELECT_BY_EMAIL)
	public Account getAccountByEmail(@Param("email") String email);
	
	@Select(SELECT_BY_EMAIL_OR_PHONE)
	public List<Account> getAccountByEmailOrPhone(@Param("accountEmail") String accountEmail, 
			@Param("accountPhone") String accountPhone);
	
	@Select(SELECT_BY_EMAIL_OR_PHONE_RESTRICT_BY_ID)
	public List<Account> getAccountByEmailOrPhoneRestrictById(@Param("accountEmail") String accountEmail, 
			@Param("accountPhone") String accountPhone, @Param("accountId") int accountId);
	
	@Select(SELECT_EXTERNAL_BY_EMAIL)
	public Account getExternalAccountByEmail(@Param("accountEmail") String accountEmail);
	
	@Delete(DELETE_BY_ID)
	public int deleteAccount(@Param("accountId") int accountId);
	
	@Delete(DELETE_BY_EMAIL)
	public int deleteAccountByEmail(@Param("accountEmail") String accountEmail);
	
	@Update(UPDATE_PASSWORD_AND_TOKEN)
	public int updatePassword(@Param("accountId") int accountId, @Param("password") String password,
			@Param("tokenKey") String tokenKey, @Param("creationDate") Date date);
}
