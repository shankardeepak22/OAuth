/**
 * 
 */
package com.oauth.example.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import sun.misc.BASE64Encoder;

/**
 * @author DEEPAK
 *
 */
public class Secret {

	static String secretKeyString = new String();

	public static String generateSecretKey(String encString) {

		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			keyGen.init(128);
			SecretKey secretKey = keyGen.generateKey();
			Cipher aesCipher = Cipher.getInstance("AES");
			aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] byteDateToEncrypt = encString.getBytes();
			byte[] byteCipherText = aesCipher.doFinal(byteDateToEncrypt);
			secretKeyString = new BASE64Encoder().encode(byteCipherText);

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		} catch (NoSuchPaddingException e) {

			e.printStackTrace();
		} catch (InvalidKeyException e) {

			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {

			e.printStackTrace();
		} catch (BadPaddingException e) {

			e.printStackTrace();
		}
		return secretKeyString;
	}

}
