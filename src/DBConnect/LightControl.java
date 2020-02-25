package DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LightControl {
	private DBConnector dbc;    //  db���ٿ� �ʿ��� db connector
	private Connection conn;    //  connecttion:db�� �����ϰ� ���ִ� ��ü
	private PreparedStatement pstmt;    //  db�� sql���� �����ϴ� ��ü
	private String sql;    //  sql ������ �����ϱ� ���� ����
	private ResultSet rs;    //  db���� sql������ ������ ����� �������� ��ü
	private String returns;    //  db �������� return�ϴ� ����
	
	public LightControl() {
		dbc = new DBConnector();    //  DBConnector ��ü ����
		conn = dbc.getConn();    //  connection ���� �� db ����
	}
	
	public String lightControlAndroid(String androidCheck, String control) {    //  �ȵ���̵忡�� Light turn on-off
		if(androidCheck.equals("security")) {    //  �ȵ���̵� ������ �´��� Ȯ��
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				sql = "update iot set light=?";    //  Light Control ������Ʈ �ϴ� ������
				pstmt = conn.prepareStatement(sql);    //  db�� �����ϱ� ���� ���� ����
				pstmt.setString(1, control);    //  sql�� ?�� control�� ����
				pstmt.executeUpdate();    //  db�� ������ ������
				returns = "ok";    //  db�� �ùٸ��� �����ϸ� ok return
			} catch (SQLException e) {    //  ���� ó��
				System.err.println("LightControl Android SQLException error");
				returns = "SQLError";
			} catch (ClassNotFoundException e) {
				System.err.println("LightControl Android ClassNotFoundException error");
				returns = "ClassError";
			} finally {    //  db������ ������ �ʱ�ȭ �� �ݾ��ֱ�
				try {
					if(pstmt != null) {
						pstmt.close();
					}
				} catch (SQLException ex) {
					System.err.println("LightControl Android close SQLException error");
				}
			}
		}
		else {    //  �ȵ���̵� ������ �ƴ� ��� noAndroid return
			returns = "noAndroid";
		}
		return returns;
	}
	
	public String lightControlArduino(String check) {    //  �Ƶ��̳뿡�� Light turn on-off
		if(dbc.arduinoCheck(check)) {    //  �Ƶ��̳� ������ �´��� Ȯ��
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				sql = "select light from iot";    //  Light Control Ȯ�� �ϴ� ������
				pstmt = conn.prepareStatement(sql);    //  db�� �����ϱ� ���� ���� ����
				rs = pstmt.executeQuery();    //  db�� ���� ������
				//  db���� ������ ������ ���� ����� ��������
				if(rs.next()) {
					returns = rs.getString("light");
				}
				else {    //  ������� ������ no return
					returns = "no";
				}
			} catch (SQLException e) {    //  ���� ó��
				System.err.println("LightControl Arduino SQLException error");
				returns = "SQLError";
			} catch (ClassNotFoundException e) {
				System.err.println("LightControl Arduino ClassNotFoundException error");
				returns = "ClassError";
			} finally {    //  db������ ������ �ʱ�ȭ �� �ݾ��ֱ�
				try {
					if(rs != null) {
						rs.close();
					}
					if(pstmt != null) {
						pstmt.close();
					}
				} catch(SQLException e) {
					System.err.println("LightControl Arduino close SQLException error");
				}
			}
		}
		else {    //  �Ƶ��̳� ������ �ƴ� ��� noArduino return
			returns = "noArduino";
		}
		return returns;
	}
}
