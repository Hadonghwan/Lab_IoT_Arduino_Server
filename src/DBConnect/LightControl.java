package DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LightControl {
	private DBConnector dbc;    //  db접근에 필요한 db connector
	private Connection conn;    //  connecttion:db에 접근하게 해주는 객체
	private PreparedStatement pstmt;    //  db에 sql문을 전달하는 객체
	private String sql;    //  sql 쿼리를 저장하기 위한 변수
	private ResultSet rs;    //  db에서 sql쿼리를 실행한 결과를 가져오는 객체
	private String returns;    //  db 연결결과를 return하는 변수
	
	public LightControl() {
		dbc = new DBConnector();    //  DBConnector 객체 생성
		conn = dbc.getConn();    //  connection 생성 및 db 연결
	}
	
	public String lightControlAndroid(String androidCheck, String control) {    //  안드로이드에서 Light turn on-off
		if(androidCheck.equals("security")) {    //  안드로이드 접근이 맞는지 확인
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				sql = "update iot set light=?";    //  Light Control 업데이트 하는 쿼리문
				pstmt = conn.prepareStatement(sql);    //  db에 접근하기 위한 퀴리 저장
				pstmt.setString(1, control);    //  sql문 ?를 control로 저장
				pstmt.executeUpdate();    //  db에 쿼리문 날리기
				returns = "ok";    //  db에 올바르게 업뎃하면 ok return
			} catch (SQLException e) {    //  예외 처리
				System.err.println("LightControl Android SQLException error");
				returns = "SQLError";
			} catch (ClassNotFoundException e) {
				System.err.println("LightControl Android ClassNotFoundException error");
				returns = "ClassError";
			} finally {    //  db접속이 끝나면 초기화 및 닫아주기
				try {
					if(pstmt != null) {
						pstmt.close();
					}
				} catch (SQLException ex) {
					System.err.println("LightControl Android close SQLException error");
				}
			}
		}
		else {    //  안드로이드 접근이 아닐 경우 noAndroid return
			returns = "noAndroid";
		}
		return returns;
	}
	
	public String lightControlArduino(String check) {    //  아두이노에서 Light turn on-off
		if(dbc.arduinoCheck(check)) {    //  아두이노 접근이 맞는지 확인
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				sql = "select light from iot";    //  Light Control 확인 하는 쿼리문
				pstmt = conn.prepareStatement(sql);    //  db에 접근하기 위한 쿼리 저장
				rs = pstmt.executeQuery();    //  db에 쿼리 날리기
				//  db에서 쿼리문 날리고 얻은 결과값 가져오기
				if(rs.next()) {
					returns = rs.getString("light");
				}
				else {    //  결과값이 없으면 no return
					returns = "no";
				}
			} catch (SQLException e) {    //  예외 처리
				System.err.println("LightControl Arduino SQLException error");
				returns = "SQLError";
			} catch (ClassNotFoundException e) {
				System.err.println("LightControl Arduino ClassNotFoundException error");
				returns = "ClassError";
			} finally {    //  db접속이 끝나면 초기화 및 닫아주기
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
		else {    //  아두이노 접근이 아닐 경우 noArduino return
			returns = "noArduino";
		}
		return returns;
	}
}
