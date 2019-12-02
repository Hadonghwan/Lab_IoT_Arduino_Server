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
	
	public String getURL() {
		return dbURL;
	}
	
	public String getID() {
		return dbId;
	}
	
	public String getPassword() {
		return dbPassword;
	}
}
