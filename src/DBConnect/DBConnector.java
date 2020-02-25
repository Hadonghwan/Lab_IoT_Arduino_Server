
package DBConnect;

import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
	private Properties prop;
	private FileInputStream fis_prop;
	private Properties key;
	private FileInputStream fis_key;
	private String aes_key;
	
	//  Lea 암호
	private Properties prop_lea;
	private FileInputStream fis_lea;
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
			//  암호화된 DB password read
			prop = new Properties();
			fis_prop = new FileInputStream("/src/Security/key.properties");
			prop.load(new BufferedInputStream(fis_prop));

			//외부에 저장된 비밀키 read
			key = new Properties();
			fis_key = new FileInputStream("C:/Users/SECURITY/Key/key.properties");
			key.load(new BufferedInputStream(fis_key));

			aes_key = key.getProperty("key");
			
			if(aes_key != null) {
				aes = new AESDec(aes_key);
			}
			if(aes != null) {
				dbPassword = aes.aesDecode(prop.getProperty("password"));
			}
		} catch (FileNotFoundException e) { //예외처리 ,대응부재 제거
			System.err.println("DBConnector FileNotFoundException error");	
		} catch (IOException e) {
			System.err.println("DBConnector IOException error");
		} catch (InvalidKeyException e) {
			System.err.println("DBConnector InvalidKeyException error");
		} catch (NoSuchAlgorithmException e) {
			System.err.println("DBConnector NoSuchAlgorithmException error");
		} catch (NoSuchPaddingException e) {
			System.err.println("DBConnector NoSuchPaddingException error");
		} catch (InvalidAlgorithmParameterException e) {
			System.err.println("DBConnector InvalidAlgorithmParameterException error");
		} catch (IllegalBlockSizeException e) {
			System.err.println("DBConnector IllegalBlockSizeException error");
		} catch (BadPaddingException e) {
			System.err.println("DBConnector BadPaddingException error");
		} finally {    //  자원 해제
			try {
				if(fis_prop != null) {
					fis_prop.close();
				}
				if(fis_key != null) {
					fis_key.close();
				}
			} catch (IOException e) {
				System.err.println("DBConnector close IOException error");
			}
		}
	}
	
	public Connection getConn() {    //  DB 연결 Connection 객체 반환
		try {
			return DriverManager.getConnection(dbURL, dbId, dbPassword);
		} catch (SQLException e) {
			System.err.println("DBConnector Connection SQLException error");
			return null;
		}
	}
	
	private byte[] get_Lea() {    //  Lea 암호 키 얻기
		try {
			prop_lea = new Properties();
			fis_lea = new FileInputStream("/WebContent/WEB-INF/lea_key.properties");    //  key 가져오기
			prop_lea.load(new BufferedInputStream(fis_lea));
			lea_key = prop_lea.getProperty("key").getBytes();    //  String 형태의 key를 byte[] 형태로 변환
			lea = new byte[16];
			System.arraycopy(lea_key, 0, lea, 0, lea_key.length);    //  key의 길이를 16으로 설정

		} catch (FileNotFoundException e) { //예외처리 ,대응부재 제거
			System.err.println("DBConnector FileNotFoundException error");	
		} catch (IOException e) {
			System.err.println("DBConnector IOException error");
		} finally {    //  자원 해제
			try {
				if(fis_lea != null) {
					fis_lea.close();
				}
			} catch (IOException e) {
				System.err.println("DBConnector close IOException error");
			}
		}
		return lea;
	}
	
	public String decryptoString(String st) {    //  Lea 암호 복호화
		cipher.init(Mode.DECRYPT, get_Lea(), roundkey);
		String check = new String(cipher.doFinal(hexStringToByteArray(st)));    //  복호화
		return check.trim();
	}
	
	public boolean arduinoCheck(String st) {    //  아두이노 체크
		if("security".equals(decryptoString(st))) return true;
		else return false;
	}
	
	public byte[] hexStringToByteArray(String hexSt) {
	    int len = hexSt.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(hexSt.charAt(i), 16) << 4)
	                             + Character.digit(hexSt.charAt(i+1), 16));
	    }
	    return data;
	}
}
