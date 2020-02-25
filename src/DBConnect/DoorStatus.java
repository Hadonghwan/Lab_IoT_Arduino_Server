package DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoorStatus {
	private DBConnector dbc;    //  db���ٿ� �ʿ��� db connector
	private Connection conn;    //  connecttion:db�� �����ϰ� ���ִ� ��ü
	private PreparedStatement pstmt;    //  db�� sql���� �����ϴ� ��ü
	private String sql;    //  sql ������ �����ϱ� ���� ����
	private ResultSet rs;    //  db���� sql������ ������ ����� �������� ��ü
	private String returns;    //  db �������� return�ϴ� ����

	public DoorStatus() {
		dbc = new DBConnector();    //  DBConnector ��ü ����
		conn = dbc.getConn();    //  connection ���� �� db ����
	}
	
	public String doorStatusUpdate(String check, String value) {    //  �Ƶ��̳뿡�� �� ���� ������Ʈ
		if(dbc.arduinoCheck(check)) {    //  �Ƶ��̳� ������ �´��� Ȯ��
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				sql = "update iot set door=?";    //  door ���� ������Ʈ ��Ű�� ������
				pstmt = conn.prepareStatement(sql);    //  db�� �����ϱ� ���� ���� ����
				pstmt.setString(1, dbc.decryptoString(value));    //  sql���� ?�� ��ȣȭ�� st�� ��ȯ
				pstmt.executeUpdate();    //  db�� ������ ������
				returns = "ok";    //  db�� �ùٸ��� �����ϸ� ok return
			} catch (SQLException e) {    //  ���� ó��
				System.err.println("DoorStatus Update SQLException error");
				returns = "SQLError";
			} catch (ClassNotFoundException e) {
				System.err.println("DoorStatus Update ClassNotFoundException error");
				returns = "ClassError";
			} finally {    //  db������ ������ �ʱ�ȭ �� �ݾ��ֱ�
				try {
					if (pstmt != null) {
						pstmt.close();
					}
				} catch (SQLException e) {
					System.err.println("DoorStatus Update PreparedStatement close SQLException error");
				}
			}
		}
		else {    //  �Ƶ��̳� ������ �ƴ� ��� noArduino return
			returns="no Arduino";
		}
		return returns;
	}

	public String doorStatusCheck(String androidCheck) {    //  �ȵ���̵忡�� ������ Ȯ��
		if(androidCheck.equals("security")) {    //  �ȵ���̵� ������ �´��� Ȯ��
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				sql = "select door from iot";    //  door ���� üũ�ϴ� ������
				pstmt = conn.prepareStatement(sql);    //  db�� �����ϱ� ���� ���� ����
				rs = pstmt.executeQuery();    //  db�� ������ ������
				//  db���� ������ ������ ���� ����� ��������
				if(rs.next()) {    //  ����� ������ ����� return
					returns = rs.getString("door");
				}
				else {    //  ����� ������ no return
					returns = "no";
				}
			} catch (SQLException e) {    //  ���� ó��
				System.err.println("DoorStatus Check SQLException error");
				returns = "SQLError";
			} catch (ClassNotFoundException e) {
				System.err.println("DoorStatus Check ClassNotFoundException error");
				returns = "ClassError";
			} finally {    //  db������ ������ �ʱ�ȭ �� �ݾ��ֱ�
				try {
					if (rs != null) {
						rs.close();
					}
					if (pstmt != null) {
						pstmt.close();
					}
				} catch (SQLException e) {
					System.err.println("DoorStatus Check close SQLException error");
				}
			}
		}
		else {    //  �ȵ���̵� ������ �ƴ� ��� noAndroid return
			returns = "noAndroid";
		}
		return returns;
	}
}
