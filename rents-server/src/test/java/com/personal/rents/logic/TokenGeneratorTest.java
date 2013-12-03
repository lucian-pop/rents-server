package com.personal.rents.logic;

import junit.framework.TestCase;

public class TokenGeneratorTest extends TestCase {
	
	public void testGenerateToken() {
		String token = TokenGenerator.generateToken();
		
		assertNotNull(token);
	}

}
