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
	
	private String dbURL = "jdbc:mysql://localhost:3306/securitylab_iot?serverTimezone=UTC";    //  localhost:3306은 DB 포트
	private String dbId = "root";    //  db 아이디(기본 최고 권한자 root)
	private String dbPassword = "security915!";    //  db 비밀번호
	private Connection conn;    //  connecttion:db에 접근하게 해주는 객체
	private PreparedStatement pstmt;    //  db에 sql문을 전달하는 객체
	private String sql;    //  sql 구문을 저장하기 위한 변수
	private String returns;    //  db 연결결과를 return하는 변수
	
	public String doorStatusUpdate(String st) {    //  문 상태 업뎃
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
