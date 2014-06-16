package ro.fizbo.rents.logic;
import java.util.UUID;

public final class TokenGenerator {

	private TokenGenerator(){
	}
	
	public static final String generateToken() {
		String token = UUID.randomUUID().toString();
		
		return token;
	}
}
