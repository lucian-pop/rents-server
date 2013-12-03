package com.personal.rents.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.personal.rents.model.Token;

public interface TokenDAO {
	
	public static final String INSERT = "insert into token (accountId, tokenKey, creationDate) "
			+ "values(#{accountId}, #{tokenKey}, #{creationDate})";
	
	public static final String GET_TOKEN_KEY = "select token.tokenKey from token where "
			+ "token.accountId=#{accountId}";
	
	@Insert(INSERT)
	@Options(useGeneratedKeys=true)
	public int insertToken(Token token);
	
	@Select(GET_TOKEN_KEY)
	public String getTokenKey(@Param("accountId") int accountId);
}
