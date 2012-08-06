package asakichy.architecture.domain_logic.table_module;

import static asakichy.architecture.domain_logic.DateUtils.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import asakichy.architecture.domain_logic.AppRuntimeException;

/**
 * 表「contracts」のテーブルモジュール.
 */

public class Contract {
	private static final String STATEMENT_GET = "SELECT * FROM contracts WHERE id = ?";

	private Connection conn;

	public Contract(Connection conn) {
		this.conn = conn;
	}

	/**
	 * 契約を取得します.
	 * 
	 * @param contractID 契約番号
	 */
	public ResultSet get(long contractID) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(STATEMENT_GET);
		stmt.setLong(1, contractID);
		return stmt.executeQuery();
	}

	/**
	 * 契約における、収益認識を登録します.
	 * 
	 * @param contractID 契約番号
	 */
	public void calculateRevenueRecognitions(long contractID) {
		try {
			ResultSet contracts = get(contractID);
			contracts.next();
			long totalRevenue = contracts.getLong("revenue");
			long productID = contracts.getLong("product_id");
			Date signedDate = contracts.getDate("date_signed");

			Product product = new Product(conn);
			ResultSet products = product.get(productID);
			products.next();
			String productType = products.getString("type");

			RevenueRecognition revenueRecognition = new RevenueRecognition(conn);

			if (productType.equals("WordProcessor")) {
				revenueRecognition.insert(contractID, signedDate, totalRevenue);
			} else if (productType.equals("Database")) {
				long oneThirdRevenue = totalRevenue / 3;
				long remainder = totalRevenue % 3;
				revenueRecognition.insert(contractID, signedDate, oneThirdRevenue);
				revenueRecognition.insert(contractID, addDays(signedDate, 60), oneThirdRevenue);
				revenueRecognition.insert(contractID, addDays(signedDate, 90), oneThirdRevenue + remainder);

			} else if (productType.equals("SpreadSheet")) {
				long oneThirdRevenue = totalRevenue / 3;
				long remainder = totalRevenue % 3;
				revenueRecognition.insert(contractID, signedDate, oneThirdRevenue);
				revenueRecognition.insert(contractID, addDays(signedDate, 30), oneThirdRevenue);
				revenueRecognition.insert(contractID, addDays(signedDate, 60), oneThirdRevenue + remainder);
			}

		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}
}
