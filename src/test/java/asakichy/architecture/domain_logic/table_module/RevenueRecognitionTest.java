package asakichy.architecture.domain_logic.table_module;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static asakichy.architecture.domain_logic.DateUtils.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import asakichy.architecture.domain_logic.table_module.Contract;
import asakichy.architecture.domain_logic.table_module.RevenueRecognition;

public class RevenueRecognitionTest {
	private static Connection conn;
	private static Date signedDate;

	@Test
	public void ある時点での収益認識() throws Exception {
		Contract contract = new Contract(conn);
		RevenueRecognition revenueRecognition = new RevenueRecognition(conn);

		// 商品「ワープロ」の契約
		contract.calculateRevenueRecognitions(0);
		// 契約日に全て計上
		assertThat(revenueRecognition.recognizedRevenue(0, signedDate), is(100L));

		// 商品「データベース」の契約
		contract.calculateRevenueRecognitions(1);
		// 契約時は1/3
		assertThat(revenueRecognition.recognizedRevenue(1, signedDate), is(33L));
		// 60日後に1/3
		assertThat(revenueRecognition.recognizedRevenue(1, addDays(signedDate, 60)), is(66L));
		// 90日後に残り
		assertThat(revenueRecognition.recognizedRevenue(1, addDays(signedDate, 90)), is(100L));

		// 商品「表計算」の契約
		contract.calculateRevenueRecognitions(2);
		// 契約時は1/3
		assertThat(revenueRecognition.recognizedRevenue(2, signedDate), is(33L));
		// 30日後に1/3
		assertThat(revenueRecognition.recognizedRevenue(2, addDays(signedDate, 30)), is(66L));
		// 60日後に残り
		assertThat(revenueRecognition.recognizedRevenue(2, addDays(signedDate, 60)), is(100L));
	}

	@BeforeClass
	public static void コネクション取得_テーブル準備_データ準備() throws Exception {
		// 接続
		Class.forName("org.h2.Driver");
		conn = DriverManager.getConnection("jdbc:h2:./pofeaa.db", "sa", "");

		// テーブル作成(NOT NULL, FOREIGN KEYは省略)
		Statement stmt = conn.createStatement();
		stmt.execute("DROP TABLE IF EXISTS revenue_recognitions");
		stmt.execute("DROP TABLE IF EXISTS contracts");
		stmt.execute("DROP TABLE IF EXISTS products");

		stmt.execute("CREATE TABLE products ( id BIGINT PRIMARY KEY, name VARCHAR(32), type VARCHAR(32) )");
		stmt.execute("CREATE TABLE contracts ( id BIGINT PRIMARY KEY, product_id BIGINT, revenue BIGINT, date_signed DATE )");
		stmt.execute("CREATE TABLE revenue_recognitions ( contract_id BIGINT, date_recognized DATE, amount BIGINT, "
				+ "PRIMARY KEY(contract_id, date_recognized) )");

		// 商品データ
		PreparedStatement pstmtProducts = conn.prepareStatement("INSERT INTO products VALUES( ?, ?, ? )");
		pstmtProducts.setLong(1, 0L);
		pstmtProducts.setString(2, "ワープロ１");
		pstmtProducts.setString(3, "WordProcessor");
		pstmtProducts.executeUpdate();
		pstmtProducts.setLong(1, 1L);
		pstmtProducts.setString(2, "データベース１");
		pstmtProducts.setString(3, "Database");
		pstmtProducts.executeUpdate();
		pstmtProducts.setLong(1, 2L);
		pstmtProducts.setString(2, "表計算１");
		pstmtProducts.setString(3, "SpreadSheet");
		pstmtProducts.executeUpdate();

		// 契約日
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2012, Calendar.JANUARY, 1);
		signedDate = new Date(cal.getTimeInMillis());

		// 契約データ（全て１００円）
		PreparedStatement pstmtContracts = conn.prepareStatement("INSERT INTO contracts VALUES( ?, ?, ?, ? )");
		pstmtContracts.setLong(1, 0L);
		pstmtContracts.setLong(2, 0L);// ワープロ
		pstmtContracts.setLong(3, 100L);
		pstmtContracts.setDate(4, signedDate);
		pstmtContracts.executeUpdate();
		pstmtContracts.setLong(1, 1L);
		pstmtContracts.setLong(2, 1L);// データベース
		pstmtContracts.setLong(3, 100L);
		pstmtContracts.setDate(4, signedDate);
		pstmtContracts.executeUpdate();
		pstmtContracts.setLong(1, 2L);
		pstmtContracts.setLong(2, 2L);// 表計算
		pstmtContracts.setLong(3, 100L);
		pstmtContracts.setDate(4, signedDate);
		pstmtContracts.executeUpdate();
	}

	@AfterClass
	public static void コネクション解放() throws Exception {
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
