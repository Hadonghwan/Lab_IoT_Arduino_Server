package DBConnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LightControl {
	public LightControl() {}
	
	private DBConnector dbc;
	private Connection conn;    //  connecttion:db에 접근하게 해주는 객체
	private PreparedStatement pstmt;    //  db에 sql문을 전달하는 객체
	private String sql;    //  sql 쿼리를 저장하기 위한 변수
	private ResultSet rs;    //  db에서 sql쿼리를 실행한 결과를 가져오는 객체
	private String returns;    //  db 연결결과를 return하는 변수
	
	public String lightTurnOnOffAndroid(String androidCheck, String control) {    //  안드로이드에서 Light turn on-off
		if(dbc.checkString(androidCheck)) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				conn = DriverManager.getConnection(dbc.getURL(), dbc.getID(), dbc.getPassword());
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
	
	public String lightTurnOnOffArduino(String check) {    //  아두이노에서 Light turn on-off
		if(dbc.checkString(check)) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				conn = DriverManager.getConnection(dbc.getURL(), dbc.getID(), dbc.getPassword());
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
