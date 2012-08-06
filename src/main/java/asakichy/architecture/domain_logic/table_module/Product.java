package asakichy.architecture.domain_logic.table_module;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 表「products」のテーブルモジュール.
 */

public class Product {
	private static final String STATEMENT_GET = "SELECT * FROM products WHERE id = ?";

	private Connection conn;

	public Product(Connection conn) {
		this.conn = conn;
	}

	/**
	 * 商品を取得します.
	 * 
	 * @param productID 商品番号
	 */
	public ResultSet get(long productID) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(STATEMENT_GET);
		stmt.setLong(1, productID);
		return stmt.executeQuery();
	}
}
