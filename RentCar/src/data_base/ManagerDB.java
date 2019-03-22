package data_base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ManagerDB {
	
	public static Statement stmt;
	public static Connection conn;
	
	public static void connect() {
		try {
				conn = DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/rentcar?allowPublicKeyRetrieval=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false", "manager", "manager_password");
				stmt = conn.createStatement();
			} catch(SQLException ex) {
				ex.printStackTrace();
			}
	}
}