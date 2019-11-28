package DBConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnector {
	private static DBConnector instance = new DBConnector();
	
	public static DBConnector getInstance() {
		return instance;
	}
	
	public DBConnector() {}
	
	private String dbURL = "jdbc:mysql://localhost:3306/securitylab_iot?serverTimezone=UTC";    //  localhost:3306�� DB ��Ʈ
	private String dbId = "root";    //  db ���̵�(�⺻ �ְ� ������ root)
	private String dbPassword = "security915!";    //  db ��й�ȣ
	private Connection conn;    //  connecttion:db�� �����ϰ� ���ִ� ��ü
	private PreparedStatement pstmt;    //  db�� sql���� �����ϴ� ��ü
	private String sql;    //  sql ������ �����ϱ� ���� ����
	private ResultSet rs;    //  db���� sql������ ������ ����� �������� ��ü
	private String returns;    //  db �������� return�ϴ� ����
	
	public String doorStatusUpdate(String check, String st) {    //  �Ƶ��̳뿡�� �� ���� ������Ʈ
		if(check.equals("security")) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				conn = DriverManager.getConnection(dbURL, dbId, dbPassword);    //  DB�� ����
				sql = "update iot set door=?";    //  door ���� ������Ʈ ��Ű�� ������
				pstmt = conn.prepareStatement(sql);    //  db�� �����ϱ� ���� ���� ����
				pstmt.setString(1, st);    //  sql���� ?�� st�� ��ȯ
				pstmt.executeUpdate();    //  db�� ������ ������
				returns = "ok";    //  db�� �ùٸ��� �����ϸ� ok return
			} catch(Exception e) {    //  error
				e.printStackTrace();
				returns = "error";
			} finally {    //  db������ ������ �ʱ�ȭ �� �ݾ��ֱ�
				if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
				if (conn != null)try {conn.close();} catch (SQLException ex) {}
			}
		}
		else
			returns="no Arduino";
		return returns;
	}
	
	public String doorStatusCheck(String androidCheck) {    //  �ȵ���̵忡�� ������ Ȯ��
		if(androidCheck.equals("security")) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				conn = DriverManager.getConnection(dbURL, dbId, dbPassword);
				sql = "select door from iot";
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				if(rs.next())
					returns = rs.getString("door");
				else
					returns = "no";
			} catch(Exception e) {
				e.printStackTrace();
				returns = "error";
			} finally {
				if (rs != null)try {rs.close();} catch (SQLException ex) {}
				if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
				if (conn != null)try {conn.close();} catch (SQLException ex) {}
			}
		}
		else
			returns = "noAndroid";
		return returns;
	}
	
	public String lightTurnOnOffAndroid(String androidCheck, String control) {    //  �ȵ���̵忡�� Light turn on-off
		if(androidCheck.equals("security")) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				conn = DriverManager.getConnection(dbURL, dbId, dbPassword);
				sql = "update iot set light=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, control);
				pstmt.executeUpdate();
				returns = "ok";
			} catch(Exception e) {
				e.printStackTrace();
				returns = "error";
			} finally {
				if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
				if (conn != null)try {conn.close();} catch (SQLException ex) {}
			}
		}
		else
			returns = "noAndroid";
		return returns;
	}
	
	public String lightTurnOnOffArduino(String check) {    //  �Ƶ��̳뿡�� Light turn on-off
		if(check.equals("security")) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				conn = DriverManager.getConnection(dbURL, dbId, dbPassword);
				sql = "select light from iot";
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				if(rs.next())
					returns = rs.getString("light");
				else
					returns = "no";
			} catch(Exception e) {
				e.printStackTrace();
				returns = "error";
			} finally {
				if (rs != null)try {rs.close();} catch (SQLException ex) {}
				if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
				if (conn != null)try {conn.close();} catch (SQLException ex) {}
			}
		}
		else
			returns = "noArduino";
		return returns;
	}
}
