package asakichy;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class H2Study {

	// ui使用するときのURL
	// jdbc:h2:フルパス\pofeaa.db

	// リファレンス
	// http://www.h2database.com/html/grammar.html

	private static Connection conn;

	@BeforeClass
	public static void 準備() throws Exception {
		Class.forName("org.h2.Driver");
		// 接続
		conn = DriverManager.getConnection("jdbc:h2:./pofeaa.db", "sa", "");
	}

	@Test
	public void 接続確認() throws Exception {
		assertThat(conn, is(notNullValue()));
		assertThat(conn.isClosed(), is(false));
	}

	@Test
	public void データ操作() throws Exception {
		// テーブル作成
		Statement stmt = conn.createStatement();
		stmt.execute("DROP TABLE IF EXISTS revenue_recognitions");
		stmt.execute("CREATE TABLE revenue_recognitions " + "( contract_id BIGINT NOT NULL, date_recognized DATE NOT NULL, "
				+ "amount BIGINT NOT NULL, " + "PRIMARY KEY(contract_id, date_recognized) )");

		final String STATEMENT_RECOGNITION_INSERT = "INSERT INTO revenue_recognitions VALUES ( ?, ?, ? )";
		final String STATEMENT_RECOGNITION_SELECT = "SELECT * FROM revenue_recognitions";
		PreparedStatement stmtINsert = conn.prepareStatement(STATEMENT_RECOGNITION_INSERT);
		Calendar cal = Calendar.getInstance();
		cal.clear();// 時間部分のclear
		cal.set(2012, Calendar.JANUARY, 1);
		Date recognizedDate = new Date(cal.getTimeInMillis());
		stmtINsert.setLong(1, 0L);
		stmtINsert.setDate(2, recognizedDate);
		stmtINsert.setLong(3, 100);
		stmtINsert.executeUpdate();

		PreparedStatement stmtSelect = conn.prepareStatement(STATEMENT_RECOGNITION_SELECT);
		ResultSet rs = stmtSelect.executeQuery();
		rs.next();
		assertThat(rs.getLong("contract_id"), is(0L));
		assertThat(rs.getDate("date_recognized"), is(recognizedDate));
		assertThat(rs.getLong("amount"), is(100L));
	}

	@Test
	public void CLOB型() throws Exception {
		// テーブル作成
		Statement stmt = conn.createStatement();
		stmt.execute("DROP TABLE IF EXISTS clob_tbl");
		stmt.execute("CREATE TABLE clob_tbl ( id BIGINT PRIMARY KEY, long_data CLOB )");

		// 追加
		final String INSERT = "INSERT INTO clob_tbl VALUES ( ?, ? )";
		PreparedStatement pstmt = conn.prepareStatement(INSERT);
		pstmt.setLong(1, 0);
		String data = "clobdata";
		StringReader sr = new StringReader(data);
		pstmt.setClob(2, sr, data.length());
		pstmt.executeUpdate();

		// 取得
		final String FIND = "SELECT * FROM clob_tbl WHERE id = ?";
		pstmt = conn.prepareStatement(FIND);
		pstmt.setLong(1, 0);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		Clob clob = rs.getClob(2);
		String foundData = clob.getSubString(1, (int) clob.length());
		assertThat(foundData, is(data));
	}

	@AfterClass
	public static void 後片づけ() throws Exception {
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
