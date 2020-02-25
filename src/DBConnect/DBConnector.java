
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
	
	//  DB ��ȣȭ �ʿ� ����
	private AESDec aes;
	private Properties prop;
	private FileInputStream fis_prop;
	private Properties key;
	private FileInputStream fis_key;
	private String aes_key;
	
	//  Lea ��ȣ
	private Properties prop_lea;
	private FileInputStream fis_lea;
	private BlockCipherMode cipher = new LEA.CBC();
	private byte[] roundkey = { 0x0, };
	private byte[] lea_key;
	private byte[] lea;
	
	//DB ����
	private String dbURL = "jdbc:mysql://localhost:3306/securitylab_iot?serverTimezone=UTC";    //  localhost:3306�� DB ��Ʈ
	private String dbId = "root";    //  db ���̵�(�⺻ �ְ� ������ root)
	private String dbPassword = "";    //  db ��й�ȣ
	
	public DBConnector() {
		try {
			//  ��ȣȭ�� DB password read
			prop = new Properties();
			fis_prop = new FileInputStream("/src/Security/key.properties");
			prop.load(new BufferedInputStream(fis_prop));

			//�ܺο� ����� ���Ű read
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
		} catch (FileNotFoundException e) { //����ó�� ,�������� ����
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
		} finally {    //  �ڿ� ����
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
	
	public Connection getConn() {    //  DB ���� Connection ��ü ��ȯ
		try {
			return DriverManager.getConnection(dbURL, dbId, dbPassword);
		} catch (SQLException e) {
			System.err.println("DBConnector Connection SQLException error");
			return null;
		}
	}
	
	private byte[] get_Lea() {    //  Lea ��ȣ Ű ���
		try {
			prop_lea = new Properties();
			fis_lea = new FileInputStream("/WebContent/WEB-INF/lea_key.properties");    //  key ��������
			prop_lea.load(new BufferedInputStream(fis_lea));
			lea_key = prop_lea.getProperty("key").getBytes();    //  String ������ key�� byte[] ���·� ��ȯ
			lea = new byte[16];
			System.arraycopy(lea_key, 0, lea, 0, lea_key.length);    //  key�� ���̸� 16���� ����

		} catch (FileNotFoundException e) { //����ó�� ,�������� ����
			System.err.println("DBConnector FileNotFoundException error");	
		} catch (IOException e) {
			System.err.println("DBConnector IOException error");
		} finally {    //  �ڿ� ����
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
	
	public String decryptoString(String st) {    //  Lea ��ȣ ��ȣȭ
		cipher.init(Mode.DECRYPT, get_Lea(), roundkey);
		String check = new String(cipher.doFinal(hexStringToByteArray(st)));    //  ��ȣȭ
		return check.trim();
	}
	
	public boolean arduinoCheck(String st) {    //  �Ƶ��̳� üũ
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
