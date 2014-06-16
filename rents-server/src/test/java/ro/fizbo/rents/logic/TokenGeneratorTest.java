package ro.fizbo.rents.logic;

import ro.fizbo.rents.logic.TokenGenerator;
import junit.framework.TestCase;

public class TokenGeneratorTest extends TestCase {
	
	public void testGenerateToken() {
		String token = TokenGenerator.generateToken();
		
		assertNotNull(token);
	}

}
