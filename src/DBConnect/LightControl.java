package DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LightControl {
	public LightControl() {}
	
	private DBConnector dbc = new DBConnector();
	private Connection conn = dbc.getConn();    //  connecttion:db�� �����ϰ� ���ִ� ��ü, DB�� ����
	private PreparedStatement pstmt;    //  db�� sql���� �����ϴ� ��ü
	private String sql;    //  sql ������ �����ϱ� ���� ����
	private ResultSet rs;    //  db���� sql������ ������ ����� �������� ��ü
	private String returns;    //  db �������� return�ϴ� ����
	
	public String lightControlAndroid(String androidCheck, String control) {    //  �ȵ���̵忡�� Light turn on-off
		if(androidCheck.equals("security")) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				sql = "update iot set light=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, control);
				pstmt.executeUpdate();
				returns = "ok";
			} catch (SQLException e) {
				System.err.println("LightControl Android SQLException error");
				returns = "SQLError";
			} catch (ClassNotFoundException e) {
				System.err.println("LightControl Android ClassNotFoundException error");
				returns = "ClassError";
			} finally {    //  db������ ������ �ʱ�ȭ �� �ݾ��ֱ�
				if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {System.err.println("LightControl Android PreparedStatement close SQLException error");}
			}
		}
		else
			returns = "noAndroid";
		return returns;
	}
	
	public String lightControlArduino(String check) {    //  �Ƶ��̳뿡�� Light turn on-off
		if(dbc.checkString(check)) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				sql = "select light from iot";
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				if(rs.next())
					returns = rs.getString("light");
				else
					returns = "no";
			} catch (SQLException e) {
				System.err.println("LightControl Arduino SQLException error");
				returns = "SQLError";
			} catch (ClassNotFoundException e) {
				System.err.println("LightControl Arduino ClassNotFoundException error");
				returns = "ClassError";
			} finally {
				if (rs != null)try {rs.close();} catch (SQLException ex) {System.err.println("LightControl Arduino ResultSet close SQLException error");}
				if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {System.err.println("LightControl Arduino PreparedStatement close SQLException error");}
			}
		}
		else
			returns = "noArduino";
		return returns;
	}
}
