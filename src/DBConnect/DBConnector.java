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
	
	private String dbURL = "jdbc:mysql://localhost:3306/securitylab_iot?serverTimezone=UTC";    //  localhost:3306은 DB 포트
	private String dbId = "root";    //  db 아이디(기본 최고 권한자 root)
	private String dbPassword = "security915!";    //  db 비밀번호
	private Connection conn;    //  connecttion:db에 접근하게 해주는 객체
	private PreparedStatement pstmt;    //  db에 sql문을 전달하는 객체
	private String sql;    //  sql 쿼리를 저장하기 위한 변수
	private ResultSet rs;    //  db에서 sql쿼리를 실행한 결과를 가져오는 객체
	private String returns;    //  db 연결결과를 return하는 변수
	
	public String doorStatusUpdate(String st) {    //  아두이노에서 문 상태 업데이트
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbId, dbPassword);    //  DB와 연결
			sql = "update iot set door=?";    //  door 상태 업데이트 시키는 쿼리문
			pstmt = conn.prepareStatement(sql);    //  db와 접근하기 위한 쿼리 저장
			pstmt.setString(1, st);    //  sql문에 ?를 st로 변환
			pstmt.executeUpdate();    //  db에 쿼리문 날리기
			returns = "ok";    //  db에 올바르게 업뎃하면 ok return
		} catch(Exception e) {    //  error
			e.printStackTrace();
			returns = "error";
		} finally {    //  db접속이 끝나면 초기화 및 닫아주기
			if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
			if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	
	public String doorStatusCheck(String androidCheck) {    //  안드로이드에서 문상태 확인
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
}
