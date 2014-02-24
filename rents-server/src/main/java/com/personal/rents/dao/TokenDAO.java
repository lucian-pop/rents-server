package com.personal.rents.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.personal.rents.model.Token;

public interface TokenDAO {
	
	public static final String INSERT = "insert into token (accountId, tokenKey, tokenCreationDate)"
			+ " values(#{accountId}, #{tokenKey}, #{tokenCreationDate})";
	
	public static final String SELECT_BY_TOKEN_KEY = "select * from token where"
			+ " token.tokenKey=#{tokenKey}";
	
	@Insert(INSERT)
	@Options(useGeneratedKeys=true, keyProperty="accountId")
	public int insertToken(Token token);
	
	@Select(SELECT_BY_TOKEN_KEY)
	public Token getToken(@Param("tokenKey") String tokenKey);
}
