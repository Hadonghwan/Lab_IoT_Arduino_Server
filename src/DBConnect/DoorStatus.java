package DBConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoorStatus {
	public DoorStatus() {}
	
	private DBConnector dbc = new DBConnector();
	private Connection conn;    //  connecttion:db에 접근하게 해주는 객체
	private PreparedStatement pstmt;    //  db에 sql문을 전달하는 객체
	private String sql;    //  sql 쿼리를 저장하기 위한 변수
	private ResultSet rs;    //  db에서 sql쿼리를 실행한 결과를 가져오는 객체
	private String returns;    //  db 연결결과를 return하는 변수
	
	public String doorStatusUpdate(String check, String st) {    //  아두이노에서 문 상태 업데이트
		if(dbc.checkString(check)) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				conn = DriverManager.getConnection(dbc.getURL(), dbc.getID(), dbc.getPassword());    //  DB와 연결
				sql = "update iot set door=?";    //  door 상태 업데이트 시키는 쿼리문
				pstmt = conn.prepareStatement(sql);    //  db와 접근하기 위한 쿼리 저장
				pstmt.setString(1, dbc.decryptoString(st));    //  sql문에 ?를 st로 변환
				pstmt.executeUpdate();    //  db에 쿼리문 날리기
				returns = "ok";    //  db에 올바르게 업뎃하면 ok return
			} catch (SQLException e) {
				System.err.println("BbsDAO SQLException error");
				returns = "error";
			} catch (ClassNotFoundException e) {
				System.err.println("BbsDAO ClassNotFoundException error");
				returns = "error";
			} finally {    //  db접속이 끝나면 초기화 및 닫아주기
				if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
				if (conn != null)try {conn.close();} catch (SQLException ex) {}
			}
		}
		else
			returns="no Arduino";
		return returns;
	}

	public String doorStatusCheck(String androidCheck) {    //  안드로이드에서 문상태 확인
		if(dbc.checkString(androidCheck)) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				conn = DriverManager.getConnection(dbc.getURL(), dbc.getID(), dbc.getPassword());
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
