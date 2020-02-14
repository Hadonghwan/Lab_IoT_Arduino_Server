
package DBConnect;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.io.BufferedInputStream;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import Security.AESDec;

import kr.re.nsr.crypto.BlockCipher.Mode;
import kr.re.nsr.crypto.BlockCipherMode;
import kr.re.nsr.crypto.symm.LEA;

public class DBConnector {
	
	//  DB 암호화 필요 변수
	private AESDec aes;
	private String propFile;
	private Properties prop;
	private FileInputStream fis;
	private String read_key;
	private Properties key;
	private FileInputStream fis_key;
	private String aes_key;
	
	//  Lea 암호
	private Properties prop_lea;
	private BlockCipherMode cipher = new LEA.CBC();
	private byte[] roundkey = { 0x0, };
	private byte[] lea_key;
	private byte[] lea;
	
	//DB 접속
	private String dbURL = "jdbc:mysql://localhost:3306/securitylab_iot?serverTimezone=UTC";    //  localhost:3306은 DB 포트
	private String dbId = "root";    //  db 아이디(기본 최고 권한자 root)
	private String dbPassword = "";    //  db 비밀번호
	
	public DBConnector() {
		try {
			//  내부 암호화 	DB password read
			propFile = "/src/Security/key.properties";
			prop = new Properties();
			fis = new FileInputStream(propFile);
			prop.load(new BufferedInputStream(fis));

			//외부에 저장된 비밀키 read
			read_key = "C:/Users/SECURITY/Key/key.properties";
			key = new Properties();
			fis_key = new FileInputStream(read_key);
			key.load(new BufferedInputStream(fis_key));

			aes_key = key.getProperty("key");
			if(aes_key != null)
				aes = new AESDec(aes_key);

			if(aes != null)
				dbPassword = aes.aesDecode(prop.getProperty("password"));
			if(fis != null) {
				fis.close();
			}
			if(fis_key != null) {
				fis_key.close();
			}
		} catch (FileNotFoundException e) { //예외처리 ,대응부재 제거
			System.err.println("BbsDAO FileNotFoundException error");	
		} catch (IOException e) {
			System.err.println("BbsDAO IOException error");
		} catch (InvalidKeyException e) {
			System.err.println("BbsDAO InvalidKeyException error");
		} catch (NoSuchAlgorithmException e) {
			System.err.println("BbsDAO NoSuchAlgorithmException error");
		} catch (NoSuchPaddingException e) {
			System.err.println("BbsDAO NoSuchPaddingException error");
		} catch (InvalidAlgorithmParameterException e) {
			System.err.println("BbsDAO InvalidAlgorithmParameterException error");
		} catch (IllegalBlockSizeException e) {
			System.err.println("BbsDAO IllegalBlockSizeException error");
		} catch (BadPaddingException e) {
			System.err.println("BbsDAO BadPaddingException error");
		}
	}
	
	public String getURL() {
		return dbURL;
	}
	
	public String getID() {
		return dbId;
	}
	
	public String getPassword() {
		return dbPassword;
	}
	
	public byte[] get_Lea() {
		try {
			prop_lea = new Properties();
			prop_lea.load(new BufferedInputStream(new FileInputStream("/WebContent/WEB-INF/lea_key.properties")));
			lea_key = prop_lea.getProperty("key").getBytes();
			lea = new byte[16];
			System.arraycopy(lea_key, 0, lea, 0, lea_key.length);

		} catch (FileNotFoundException e) { //예외처리 ,대응부재 제거
			System.err.println("BbsDAO FileNotFoundException error");	
		} catch (IOException e) {
			System.err.println("BbsDAO IOException error");
		}
		return lea;
	}
	
	public String decryptoString(String st) {
		cipher.init(Mode.DECRYPT, get_Lea(), roundkey);
		String check = new String(cipher.doFinal(hexStringToByteArray(st)));
		return check.trim();
	}
	
	public boolean checkString(String st) {
		if("security".equals(decryptoString(st))) return true;
		else return false;
	}
	
	public byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
}
