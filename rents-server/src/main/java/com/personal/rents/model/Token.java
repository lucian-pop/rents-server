package com.personal.rents.model;

import java.util.Date;

public class Token {
	
	private Integer tokenId;
	
	private Integer accountId;
	
	private String tokenKey;
	
	private Date tokenCreationDate;

	public Integer getTokenId() {
		return tokenId;
	}

	public void setTokenId(Integer tokenId) {
		this.tokenId = tokenId;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public String getTokenKey() {
		return tokenKey;
	}

	public void setTokenKey(String tokenKey) {
		this.tokenKey = tokenKey;
	}

	public Date getTokenCreationDate() {
		return tokenCreationDate;
	}

	public void setTokenCreationDate(Date tokenCreationDate) {
		this.tokenCreationDate = tokenCreationDate;
	}
	
}
