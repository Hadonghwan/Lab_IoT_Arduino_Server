package DBConnect;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import Security.AESDec;
import Security.Crypto;

public class CoffeeStatus {
	private DBConnector dbc;
	private Connection conn;    //  connecttion:db�� �����ϰ� ���ִ� ��ü, DB�� ����
	private PreparedStatement pstmt;    //  db�� sql���� �����ϴ� ��ü
	private String sql;    //  sql ������ �����ϱ� ���� ����
	private ResultSet rs;    //  db���� sql������ ������ ����� �������� ��ü
	private String returns;    //  db �������� return�ϴ� ����
	
	private Crypto crypto;

	public CoffeeStatus() {
		dbc = new DBConnector();    //  DBConnector ��ü ����
		conn = dbc.getConn();    //  connection ���� �� db ����
		crypto = new Crypto();
	}

	public String coffeeStatusUpdate(String check, String value) {    //  �Ƶ��̳뿡�� A4 ���� ������Ʈ
		if(dbc.arduinoCheck(check)) {    //  �Ƶ��̳� ������ �´��� Ȯ��
			try {
				sql = "update iot set coffee_weight=?";    //  A4 ���� ������Ʈ ��Ű�� ������
				pstmt = conn.prepareStatement(sql);    //  db�� �����ϱ� ���� ���� ����
				pstmt.setString(1, dbc.decryptoString(value));    //  sql���� ?�� ��ȣȭ�� st�� ��ȯ
				pstmt.executeUpdate();    //  db�� ������ ������
				returns = "ok";    //  db�� �ùٸ��� �����ϸ� ok return
			}catch (SQLException e) {    //  ���� ó��
				System.err.println("A4Status Update SQLException error");
				returns = "SQLError";
			} finally {    //  db������ ������ �ʱ�ȭ �� �ݾ��ֱ�
				try {
					if(pstmt != null) {
						pstmt.close();
					}
				} catch (SQLException e) {
					System.err.println("A4Status Update close SQLException error");
				}
			}
		}
		else {    //  �Ƶ��̳� ������ �ƴ� ��� noArduino return
			returns = "noArduino";
		}
		return returns;
	}

	public String coffeeStatusCheck(String check, String key) {    //  �ȵ���̵忡�� A4 ���� Ȯ��
		try {
			if("security".equals(AESDec.aesDecryption(check, crypto.decrypto(key)))) {    //  �ȵ���̵� ������ �´��� Ȯ��
				try {
					sql = "select coffee_weight from iot";    //  A4 ���� üũ�ϴ� ������
					pstmt = conn.prepareStatement(sql);    //  db�� �����ϱ� ���� ���� ����
					rs = pstmt.executeQuery();    //  db�� ������ ������
					//  db���� ������ ������ ���� ����� ��������
					if(rs.next()) {    //  ����� ������ ����� return
						String value;
						value = rs.getString("coffee_weight");
						if(value.equals("1")) {
							returns = "coffeeenough-";
						} else if(value.equals("0")) {
							returns = "coffeelack-";
						}
					}
					else {    //  ����� ������ no return
						returns = "no";
					}
				} catch (SQLException e) {    //  ���� ó��
					System.err.println("A4Status Check SQLException error");
					returns = "SQLError";
				} finally {    //  db������ ������ �ʱ�ȭ �� �ݾ��ֱ�
					try {
						if(rs != null) {
							rs.close();
						}
						if(pstmt != null) {
							pstmt.close();
						}
					} catch (SQLException e) {
						System.err.println("A4Status Check close SQLException error");
					}
				}
			}
			else {    //  �ȵ���̵� ������ �ƴ� ��� noAndroid return
				returns = "noAndroid";
			}
		} catch (InvalidKeyException e) {
			System.err.println("Coffee Check InvalidKeyException error");
		} catch (UnsupportedEncodingException e) {
			System.err.println("Coffee Check UnsupportedEncodingException error");
		} catch (NoSuchPaddingException e) {
			System.err.println("Coffee Check NoSuchPaddingException error");
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Coffee Check NoSuchAlgorithmException error");
		} catch (InvalidAlgorithmParameterException e) {
			System.err.println("Coffee Check InvalidAlgorithmParameterException error");
		} catch (BadPaddingException e) {
			System.err.println("Coffee Check BadPaddingException error");
		} catch (IllegalBlockSizeException e) {
			System.err.println("Coffee Check IllegalBlockSizeException error");
		}
		return returns;
	}
}