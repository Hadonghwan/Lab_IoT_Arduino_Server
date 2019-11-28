package DBConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
	private String returns;    //  db �������� return�ϴ� ����
	
	public String doorStatusUpdate(String st) {    //  �� ���� ����
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbId, dbPassword);
			sql = "update iot set door=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, st);
			pstmt.executeUpdate();
			returns = "ok";
		} catch(Exception e) {
			e.printStackTrace();
			returns = "error";
		} finally {
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returns;
	}
}
