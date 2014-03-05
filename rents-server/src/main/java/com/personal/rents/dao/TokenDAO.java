package com.personal.rents.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.personal.rents.model.Token;

public interface TokenDAO {
	
	public static final String INSERT = "insert into token (accountId, tokenKey, tokenCreationDate)"
			+ " values(#{accountId}, #{tokenKey}, #{tokenCreationDate})";
	
	public static final String SELECT_BY_ACCOUNT_ID = "select * from token"
			+ " where token.accountId=#{accountId}";
	
	public static final String SELECT_BY_TOKEN_KEY = "select * from token"
			+ " where token.tokenKey=#{tokenKey}";
	
	public static final String UPDATE_TOKEN_KEY = "update token set tokenKey = #{tokenKey},"
			+ "tokenCreationDate = #{tokenCreationDate}"
			+ " where tokenId = #{tokenId}";
	
	@Insert(INSERT)
	@Options(useGeneratedKeys=true, keyProperty="accountId")
	public int insertToken(Token token);
	
	@Select(SELECT_BY_ACCOUNT_ID)
	public Token getTokenByAccountId(@Param("accountId") int accountId);
	
	@Select(SELECT_BY_TOKEN_KEY)
	public Token getTokenByTokenKey(@Param("tokenKey") String tokenKey);
	
	@Update(UPDATE_TOKEN_KEY)
	public int updateTokenKey(@Param("tokenId") int tokenId, @Param("tokenKey") String tokenKey,
			@Param("tokenCreationDate") Date tokenCreationDate);
}
