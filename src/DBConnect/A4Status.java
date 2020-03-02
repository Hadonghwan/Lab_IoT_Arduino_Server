package DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class A4Status {
	private DBConnector dbc;
	private Connection conn;    //  connecttion:db�� �����ϰ� ���ִ� ��ü, DB�� ����
	private PreparedStatement pstmt;    //  db�� sql���� �����ϴ� ��ü
	private String sql;    //  sql ������ �����ϱ� ���� ����
	private ResultSet rs;    //  db���� sql������ ������ ����� �������� ��ü
	private String returns;    //  db �������� return�ϴ� ����
	
	public A4Status() {
		dbc = new DBConnector();    //  DBConnector ��ü ����
		conn = dbc.getConn();    //  connection ���� �� db ����
	}
	
	public String a4StatusUpdate(String check, String value) {    //  �Ƶ��̳뿡�� A4 ���� ������Ʈ
		if(dbc.arduinoCheck(check)) {    //  �Ƶ��̳� ������ �´��� Ȯ��
			try {
				sql = "update iot set A4=?";    //  A4 ���� ������Ʈ ��Ű�� ������
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
	
	public String a4StatusCheck(String check) {    //  �ȵ���̵忡�� A4 ���� Ȯ��
		if(check.equals("serucity")) {    //  �ȵ���̵� ������ �´��� Ȯ��
			try {
				sql = "select A4 from iot";    //  A4 ���� üũ�ϴ� ������
				pstmt = conn.prepareStatement(sql);    //  db�� �����ϱ� ���� ���� ����
				rs = pstmt.executeQuery();    //  db�� ������ ������
				//  db���� ������ ������ ���� ����� ��������
				if(rs.next()) {    //  ����� ������ ����� return
					String value;
					value = rs.getString("A4");
					if(value.equals("1")) {
						returns = "enough";
					} else if(value.equals("0")) {
						returns = "lack";
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
		return returns;
	}
}
