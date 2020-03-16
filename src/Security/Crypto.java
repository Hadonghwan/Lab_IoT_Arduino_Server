package Security;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Crypto {
	private FileInputStream fis_key;
	private Properties prop;
	private String returns;
	
	public String encrypto(String value, String key) {
		try {
			fis_key = new FileInputStream("Security/PRIVATEkey.properites");
			prop = new Properties();
			prop.load(new BufferedInputStream(fis_key));
			
			returns = RSAEnc.rsaEncryption(value, prop.getProperty("key"));
		} catch (FileNotFoundException e) {
			System.err.println("Cryto FileNotFoundException error");
		} catch (IOException e) {
			System.err.println("Cryto IOException error");
		} catch (InvalidKeyException e) {
			System.err.println("Cryto InvalidKeyException error");
		} catch (NoSuchPaddingException e) {
			System.err.println("Cryto NoSuchPaddingException error");
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Cryto NoSuchAlgorithmException error");
		} catch (BadPaddingException e) {
			System.err.println("Cryto BadPaddingException error");
		} catch (IllegalBlockSizeException e) {
			System.err.println("Cryto IllegalBlockSizeException error");
		} catch (InvalidKeySpecException e) {
			System.err.println("Cryto InvalidKeySpecException error");
		} finally {
			try {
				if(fis_key != null) {
					fis_key.close();
				}
			} catch (IOException e) {
				System.err.println("Crypto IOException error");
			}
		}
		return returns;
	}
	
	public String decrypto(String value) {
		try {
			fis_key = new FileInputStream("Security/PRIVATEkey.properites");
			prop = new Properties();
			prop.load(new BufferedInputStream(fis_key));
			
			returns = RSAEnc.rsaDecryption(value, prop.getProperty("key"));
		} catch (FileNotFoundException e) {
			System.err.println("Cryto FileNotFoundException error");
		} catch (IOException e) {
			System.err.println("Cryto IOException error");
		} catch (InvalidKeyException e) {
			System.err.println("Cryto InvalidKeyException error");
		} catch (NoSuchPaddingException e) {
			System.err.println("Cryto NoSuchPaddingException error");
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Cryto NoSuchAlgorithmException error");
		} catch (BadPaddingException e) {
			System.err.println("Cryto BadPaddingException error");
		} catch (IllegalBlockSizeException e) {
			System.err.println("Cryto IllegalBlockSizeException error");
		} catch (InvalidKeySpecException e) {
			System.err.println("Cryto InvalidKeySpecException error");
		} finally {
			try {
				if(fis_key != null) {
					fis_key.close();
				}
			} catch (IOException e) {
				System.err.println("Crypto IOException error");
			}
		}
		return returns;
	}
}
