package asakichy.architecture.domain_logic;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * テーブルゲートウェイ.
 */
public class Gateway {
	private Connection conn;
	private static final String STATEMENT_FIND_RECOGNITIONS =
			"SELECT amount FROM revenue_recognitions WHERE contract_id = ? AND date_recognized <= ?";
	private static final String STATEMENT_FIND_CONTRACTS =
			"SELECT * FROM contracts c, products p WHERE c.id = ? AND c.product_id = p.id";
	private static final String STATEMENT_INSERT_RECOGNITION =
			"INSERT INTO revenue_recognitions VALUES ( ?, ?, ? )";
	
	public Gateway(Connection conn) {
		this.conn = conn;
	}

	/**
	 * 収益認識テーブルより、「契約番号」「時点」を指定して、レコードを取得します。
	 * 
	 * @param contractID 契約番号
	 * @param asOf 時点
	 * 
	 * @return 収益認識レコードのリスト
	 */
	public ResultSet findRecognitionsFor(long contractID, Date asOf) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(STATEMENT_FIND_RECOGNITIONS);
		stmt.setLong(1, contractID);
		stmt.setDate(2, asOf);
		return stmt.executeQuery();
	}

	/**
	 * 契約テーブルより、「契約番号」を指定して、レコードを取得します。
	 * 
	 * @param contractID 契約番号
	 * 
	 * @return 契約レコードのリスト
	 */
	public ResultSet findContract(long contractID) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(STATEMENT_FIND_CONTRACTS);
		stmt.setLong(1, contractID);
		return stmt.executeQuery();
	}

	/**
	 * 契約テーブルより、「契約番号」を指定して、レコードを取得します。
	 * 
	 * @param contractID 契約番号
	 * @param recognitionDate 認識日
	 * @param amount 金額
	 * 
	 * @return 契約レコードのリスト
	 */

	public void insertRecognition(long contractID, Date recognitionDate, long amount)
			throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(STATEMENT_INSERT_RECOGNITION);
		stmt.setLong(1, contractID);
		stmt.setDate(2, recognitionDate);
		stmt.setLong(3, amount);
		stmt.executeUpdate();
	}
}
