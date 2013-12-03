package com.personal.rents.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.personal.rents.model.Account;

public interface AccountDAO {
	
	public static final String INSERT= "insert into account (accountType, externalId, email, password,"
			+ "firstname, lastname, phone, signupDate) values(#{accountType}, #{externalId}, #{email},"
			+ "#{password}, #{firstname}, #{lastname}, #{phone}, #{signupDate})";
	
	public static final String SELECT_BY_ID = "select account.*, token.tokenKey as tokenKey"
			+ " from account left join token on token.accountId=account.id"
			+ " where account.id=#{accountId}";
	
	public static final String DELETE_BY_ID = "delete from account where account.id=#{accountId}";
	
	public static final String SELECT_BY_EMAIL_PASSWORD = "select account.*, token.tokenKey as tokenKey"
			+ " from account left join token on account.id = token.accountId"
			+ " where account.email=#{email} and account.password=#{password}";
	
	public static final String SELECT_ID_BY_EMAIL_PASSWORD = "select id from account where email="
			+ "#{email} and password=#{password}";
	
	public static final String UPDATE_PASSWORD_AND_TOKEN = "update account left join token"
			+ " on token.accountId=account.id set account.password=#{password},"
			+ " token.tokenKey=#{tokenKey}, token.creationDate=#{creationDate}"
			+ " where account.id=#{accountId}";

	@Insert(INSERT)
	@Options(useGeneratedKeys=true)
	public int insertAccount(Account account);
	
	@Select(SELECT_BY_ID)
	public Account getAccountById(@Param("accountId") int accountId);
	
	@Delete(DELETE_BY_ID)
	public int deleteAccount(@Param("accountId") int accountId);
	
	@Select(SELECT_BY_EMAIL_PASSWORD)
	public Account login(@Param("email") String email, @Param("password") String password);
	
	@Select(SELECT_ID_BY_EMAIL_PASSWORD)
	public Integer getAccountId(@Param("email") String email, @Param("password") String password);
	
	@Update(UPDATE_PASSWORD_AND_TOKEN)
	public int updatePassword(@Param("accountId") int accountId, @Param("password") String password,
			@Param("tokenKey") String tokenKey, @Param("creationDate") Date date);
}
