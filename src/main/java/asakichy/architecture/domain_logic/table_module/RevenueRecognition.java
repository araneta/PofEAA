package asakichy.architecture.domain_logic.table_module;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import asakichy.architecture.domain_logic.AppRuntimeException;

/**
 * 表「revenue_recognitions」のテーブルモジュール.
 */

public class RevenueRecognition {
	private Connection conn;
	private static final String STATEMENT_FIND =
			"SELECT amount FROM revenue_recognitions WHERE contract_id = ? AND date_recognized <= ?";
	private static final String STATEMENT_INSERT = "INSERT INTO revenue_recognitions VALUES ( ?, ?, ? )";

	public RevenueRecognition(Connection conn) {
		this.conn = conn;
	}

	/**
	 * 収益認識を登録します.
	 * 
	 * @param contractID 契約番号
	 * @param recognitionDate 収益認識日
	 * @param amount 金額
	 * 
	 * @return 収益認識額
	 */
	public void insert(long contractID, Date recognitionDate, long amount) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(STATEMENT_INSERT);
		stmt.setLong(1, contractID);
		stmt.setDate(2, recognitionDate);
		stmt.setLong(3, amount);
		stmt.executeUpdate();
	}

	/**
	 * 契約における、指定時点での収益認識を検索します.
	 * 
	 * @param contractID 契約番号
	 * @param asOf 時点
	 * 
	 * @return 収益認識額
	 */
	public ResultSet find(long contractID, Date asOf) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(STATEMENT_FIND);
		stmt.setLong(1, contractID);
		stmt.setDate(2, asOf);
		return stmt.executeQuery();
	}

	/**
	 * 契約における、指定時点での収益認識額を取得します.
	 * 
	 * @param contractID 契約番号
	 * @param asOf 時点
	 * 
	 * @return 収益認識額
	 */
	public long recognizedRevenue(long contractID, Date asOf) {
		long result = 0;
		try {
			ResultSet rs = find(contractID, asOf);
			while (rs.next()) {
				result += rs.getLong("amount");
			}
			return result;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}
}
