package DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoorStatus {
	public DoorStatus() {}
	
	private DBConnector dbc = new DBConnector();
	private Connection conn = dbc.getConn();    //  connecttion:db�� �����ϰ� ���ִ� ��ü, DB�� ����
	private PreparedStatement pstmt;    //  db�� sql���� �����ϴ� ��ü
	private String sql;    //  sql ������ �����ϱ� ���� ����
	private ResultSet rs;    //  db���� sql������ ������ ����� �������� ��ü
	private String returns;    //  db �������� return�ϴ� ����
	
	public String doorStatusUpdate(String check, String st) {    //  �Ƶ��̳뿡�� �� ���� ������Ʈ
		if(dbc.checkString(check)) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				sql = "update iot set door=?";    //  door ���� ������Ʈ ��Ű�� ������
				pstmt = conn.prepareStatement(sql);    //  db�� �����ϱ� ���� ���� ����
				pstmt.setString(1, dbc.decryptoString(st));    //  sql���� ?�� st�� ��ȯ
				pstmt.executeUpdate();    //  db�� ������ ������
				returns = "ok";    //  db�� �ùٸ��� �����ϸ� ok return
			} catch (SQLException e) {
				System.err.println("DoorStatus Update SQLException error");
				returns = "SQLError";
			} catch (ClassNotFoundException e) {
				System.err.println("DoorStatus Update ClassNotFoundException error");
				returns = "ClassError";
			} finally {    //  db������ ������ �ʱ�ȭ �� �ݾ��ֱ�
				if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {System.err.println("DoorStatus Update PreparedStatement close SQLException error");}
			}
		}
		else
			returns="no Arduino";
		return returns;
	}

	public String doorStatusCheck(String androidCheck) {    //  �ȵ���̵忡�� ������ Ȯ��
		if(dbc.checkString(androidCheck)) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				sql = "select door from iot";
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				if(rs.next())
					returns = rs.getString("door");
				else
					returns = "no";
			} catch (SQLException e) {
				System.err.println("BbsDAO SQLException error");
				returns = "SQLError";
			} catch (ClassNotFoundException e) {
				System.err.println("BbsDAO ClassNotFoundException error");
				returns = "ClassError";
			} finally {
				if (rs != null)try {rs.close();} catch (SQLException ex) {System.err.println("DoorStatus Check ResultSet close SQLException error");}
				if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {System.err.println("DoorStatus Check PreparedStatement close SQLException error");}
			}
		}
		else
			returns = "noAndroid";
		return returns;
	}
}
