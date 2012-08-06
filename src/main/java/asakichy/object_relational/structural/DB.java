package asakichy.object_relational.structural;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
	private static Connection conn;

	public static void init() throws SQLException, ClassNotFoundException {
		Class.forName("org.h2.Driver");
		conn = DriverManager.getConnection("jdbc:h2:./pofeaa.db", "sa", "");
	}

	public static Statement createStatement() throws SQLException {
		return conn.createStatement();
	}

	public static PreparedStatement prepareStatement(String sql) throws SQLException {
		return conn.prepareStatement(sql);
	}

	public static void terminate() throws Exception {
		if (conn == null) {
			return;
		}
		try {
			if (conn.isClosed() == false) {
				conn.close();
			}
		} catch (SQLException ignore) {
		}
	}

}
