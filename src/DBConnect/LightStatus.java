package DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LightStatus {	
	private DBConnector dbc;    //  db���ٿ� �ʿ��� db connector
	private Connection conn;    //  connecttion:db�� �����ϰ� ���ִ� ��ü, DB�� ����
	private PreparedStatement pstmt;    //  db�� sql���� �����ϴ� ��ü
	private String sql;    //  sql ������ �����ϱ� ���� ����
	private ResultSet rs;    //  db���� sql������ ������ ����� �������� ��ü
	private String returns;    //  db �������� return�ϴ� ����

	public LightStatus() {
		dbc = new DBConnector();    //  DBConnector ��ü ����
		conn = dbc.getConn();    //  connection ���� �� db ����
	}
	
	public String lightStatusUpdate(String check, String value) {    //  �Ƶ��̳뿡�� �� ���� ������Ʈ
		if(dbc.arduinoCheck(check)) {    //  �Ƶ��̳� ������ �´��� Ȯ��
			try {
				sql = "update iot set light=?";    //  light ���� ������Ʈ ��Ű�� ������
				pstmt = conn.prepareStatement(sql);    //  db�� �����ϱ� ���� ���� ����
				pstmt.setString(1, dbc.decryptoString(value));    //  sql���� ?�� ��ȣȭ�� st�� ��ȯ
				pstmt.executeUpdate();    //  db�� ������ ������
				returns = "ok";    //  db�� �ùٸ��� �����ϸ� ok return
			}catch (SQLException e) {    //  ���� ó��
				System.err.println("LightStatus Update SQLException error");
				returns = "SQLError";
			} finally {    //  db������ ������ �ʱ�ȭ �� �ݾ��ֱ�
				try {
					if(pstmt != null) {
						pstmt.close();
					}
				} catch (SQLException e) {
					System.err.println("LightStatus Update close SQLException error");
				}
			}
		}
		else {    //  �Ƶ��̳� ������ �ƴ� ��� noArduino return
			returns = "noArduino";
		}
		return returns;
	}
	
	public String lightStatusCheck(String check) {    //  �ȵ���̵忡�� �� ���� Ȯ��
		if(check.equals("security")) {    //  �ȵ���̵� ������ �´��� Ȯ��
			try {
				sql = "select light from iot";    //  light ���� üũ�ϴ� ������
				pstmt = conn.prepareStatement(sql);    //  db�� �����ϱ� ���� ���� ����
				rs = pstmt.executeQuery();    //  db�� ������ ������
			//  db���� ������ ������ ���� ����� ��������
				if(rs.next()) {    //  ����� ������ ����� return
					String value;
					value = rs.getString("light");
					if(value.equals("1")) {
						returns = "TurnOn";
					} else if(value.equals("0")) {
						returns = "TurnOff";
					}
				}
				else {    //  ����� ������ no return
					returns = "no";
				}
			} catch (SQLException e) {    //  ���� ó��
				System.err.println("LightStatus Check SQLException error");
				returns = "SQLError";
			} finally {    //  db������ ������ �ʱ�ȭ �� �ݾ��ֱ�
				try {
					if(rs != null) {
						rs.close();
					}
					if(pstmt != null) {
						pstmt.close();
					}
				} catch (SQLException e) {
					System.err.println("LightStatus Check close SQLException error");
				}
			}
		}
		else {    //  �ȵ���̵� ������ �ƴ� ��� noAndroid return
			returns = "noAndroid";
		}
		return returns;
	}
}
