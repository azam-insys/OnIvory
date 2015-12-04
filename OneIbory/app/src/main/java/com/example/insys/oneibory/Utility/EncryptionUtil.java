package com.example.insys.oneibory.Utility;

import org.apache.commons.codec.binary.Base64;
import java.nio.charset.Charset;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtil {

	private static final String ENCRYPTION_KEY = "eNS1ce04utT6w6o8";
	private static final String CIPHER = "AES/CBC/PKCS5Padding";
	public static final String ENCRYPTION_PREFIX = "{AES}";

//	private static Logger logger = Logger.getLogger("com.perfect.webprocure.util.EncryptionUtil");

	public static String encrypt(String data){
		if (data.equals("") || data.equals("null"))
			return data;
		try{
			javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(CIPHER);
			cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, new SecretKeySpec(ENCRYPTION_KEY.getBytes(Charset.forName("UTF-8")), "AES"),  new IvParameterSpec(new byte[16]));
			byte[] encryptedBytes = cipher.doFinal(data.getBytes(Charset.forName("UTF-8")));
			return ENCRYPTION_PREFIX + new String(Base64.encodeBase64(encryptedBytes), Charset.forName("UTF-8"));
		}catch (Exception e) {
			System.err.println( "Error in EncryptionUtil.encrypt: " + e );
			return data;
		}
	}

	public static String decrypt(String data){
		if (data == null ||
				data.startsWith(ENCRYPTION_PREFIX) == false ||
				data.length() <= ENCRYPTION_PREFIX.length())
		{
			return data;
		}
		try{
			javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(CIPHER);

			cipher.init(javax.crypto.Cipher.DECRYPT_MODE, new SecretKeySpec(ENCRYPTION_KEY.getBytes(Charset.forName("UTF-8")), "AES"),  new IvParameterSpec(new byte[16]));
			byte[] decryptBytes = cipher.doFinal(Base64.decodeBase64((data.substring(ENCRYPTION_PREFIX.length())).getBytes(Charset.forName("UTF-8"))));
			String decrypted =  new String( decryptBytes, Charset.forName("UTF-8") );
			return decrypted;
		}catch (Exception e) {
			System.err.println( "Error in EncryptionUtil.decrypt: " + e );
			return data;
		}
	}


//	public static String maskNumber(String number, char maskChar, int unmasked) {
//
//		StringBuilder masked = new StringBuilder();
//
//		for (int i = 0; i < number.length()-unmasked; i++) {
//			masked.append(maskChar);
//		}
//		for (int i = number.length()-unmasked; i <  number.length(); i++) {
//			masked.append(number.charAt(i));
//		}
//		return masked.toString();
//	}

}
