package ro.fizbo.rents.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class PasswordHashing {
	
	private static final String HASH_ALGHORITM = "SHA-256";

    public static String createHashString(String password) 
    		throws NoSuchAlgorithmException, InvalidKeySpecException {
    	
    	byte[] hashedSaltedPasswordBytes = createHashByteArray(password);

        return toHex(hashedSaltedPasswordBytes);
    }
    
    private static byte[] createHashByteArray(String password)
    		throws NoSuchAlgorithmException, InvalidKeySpecException {
    	MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGHORITM);
    	messageDigest.update(password.getBytes());
    	
    	return messageDigest.digest();
    }
    
    public static boolean validatePassword(String password, String hashedPassword)
    		throws NoSuchAlgorithmException, InvalidKeySpecException {
    	byte[] hashedPasswordToValidateBytes = createHashByteArray(password);
    	
    	return slowEquals(hashedPasswordToValidateBytes, fromHex(hashedPassword));
    }
    
    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for(int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }

        return diff == 0;
    }
    
    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();

        if(paddingLength > 0)
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }
    
    private static byte[] fromHex(String hex){
        byte[] binary = new byte[hex.length() / 2];
        for(int i = 0; i < binary.length; i++) {
            binary[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }

        return binary;
    }
}