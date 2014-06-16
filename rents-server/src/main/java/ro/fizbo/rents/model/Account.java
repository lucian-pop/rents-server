package ro.fizbo.rents.model;

import java.util.Date;

public class Account {
	
	private Integer accountId;
	
	private Byte accountType;
	
	private String accountExternalId;
	
	private String accountEmail;
	
	private String accountPassword;
	
	private String accountFirstname;
	
	private String accountLastname;
	
	private String accountPhone;
	
	private Date accountSignupDate;
	
	private String tokenKey;

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Byte getAccountType() {
		return accountType;
	}

	public void setAccountType(Byte accountType) {
		this.accountType = accountType;
	}

	public String getAccountExternalId() {
		return accountExternalId;
	}

	public void setAccountExternalId(String accountExternalId) {
		this.accountExternalId = accountExternalId;
	}

	public String getAccountEmail() {
		return accountEmail;
	}

	public void setAccountEmail(String accountEmail) {
		this.accountEmail = accountEmail;
	}

	public String getAccountPassword() {
		return accountPassword;
	}

	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
	}

	public String getAccountFirstname() {
		return accountFirstname;
	}

	public void setAccountFirstname(String accountFirstname) {
		this.accountFirstname = accountFirstname;
	}

	public String getAccountLastname() {
		return accountLastname;
	}

	public void setAccountLastname(String accountLastname) {
		this.accountLastname = accountLastname;
	}

	public String getAccountPhone() {
		return accountPhone;
	}

	public void setAccountPhone(String accountPhone) {
		this.accountPhone = accountPhone;
	}

	public Date getAccountSignupDate() {
		return accountSignupDate;
	}

	public void setAccountSignupDate(Date accountSignupDate) {
		this.accountSignupDate = accountSignupDate;
	}

	public String getTokenKey() {
		return tokenKey;
	}

	public void setTokenKey(String tokenKey) {
		this.tokenKey = tokenKey;
	}

}
