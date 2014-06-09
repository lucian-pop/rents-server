package com.personal.rents.util;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import junit.framework.TestCase;

public class PasswordHashingTest extends TestCase {
	
	public void testPasswordHashing() {
		String password = "test";
		try {
			PasswordHashing.createHashString(password);
		} catch (NoSuchAlgorithmException e) {
			fail();
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			fail();
			e.printStackTrace();
		}
	}
	
	public void testPasswordsMatching() {
		String password = "#ddfsdfs@@#d22321";
		String passwordToMatch = password;
		
		String hashedPasswordToMatch = null;
		boolean passwordsMatch = false;
		try {
			hashedPasswordToMatch = PasswordHashing.createHashString(passwordToMatch);
			passwordsMatch = PasswordHashing.validatePassword(password, hashedPasswordToMatch);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		
		assertTrue(passwordsMatch);
	}

	public void testPasswordsNotMatching() {
		String password = "#ddfsdfs@@#d22321";
		String passwordToMatch = password + "2dsf3";
		
		String hashedPasswordToMatch = null;
		boolean passwordsMatch = false;
		try {
			hashedPasswordToMatch = PasswordHashing.createHashString(passwordToMatch);
			passwordsMatch = PasswordHashing.validatePassword(password, hashedPasswordToMatch);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		
		assertFalse(passwordsMatch);
	}
}
