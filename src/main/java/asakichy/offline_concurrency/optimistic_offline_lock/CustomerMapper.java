package asakichy.offline_concurrency.optimistic_offline_lock;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import asakichy.offline_concurrency.AppRuntimeException;
import asakichy.offline_concurrency.DB;

/**
 * テーブル「customers」のデータマッパー.
 *
 * 軽オフラインロック機能を持っています.
 */

public class CustomerMapper extends AbstractMapper<Customer> {
	private static final String STATEMENT_FIND = "SELECT * FROM customers WHERE id = ?";
	private static final String STATEMENT_INSERT = "INSERT INTO customers VALUES ( ?, ?, ?, ?, ?, ?, ? )";
	private static final String STATEMENT_UPDATE = "UPDATE customers SET name = ?, modified_by = ?, modified = ?, version = ? "
			+ "WHERE id = ? AND version = ?";
	private static final String STATEMENT_DELETE = "DELETE FROM customers WHERE id = ?";

	public void update(Customer subject) {
		try {
			PreparedStatement pstmt = DB.prepareStatement(STATEMENT_UPDATE);

			long newVersion = subject.getVersion() + 1;
			pstmt.setString(1, subject.getName());
			pstmt.setString(2, subject.getModifiedBy());
			pstmt.setTimestamp(3, subject.getModified());
			pstmt.setLong(4, newVersion);

			pstmt.setLong(5, subject.getId());
			pstmt.setLong(6, subject.getVersion());

			int updatedCount = pstmt.executeUpdate();
			if (updatedCount == 0) {
				throwConcurrencyException(subject);
			}
			subject.setVersion(newVersion);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	@Override
	protected String findStatement() {
		return STATEMENT_FIND;
	}

	@Override
	protected String insertStatement() {
		return STATEMENT_INSERT;
	}

	@Override
	protected String deleteStatement() {
		return STATEMENT_DELETE;
	}

	@Override
	protected Customer doLoad(ResultSet rs) throws SQLException {
		String name = rs.getString("name");
		return new Customer(name);
	}

	@Override
	protected void doInsert(Customer subject, PreparedStatement pstmt) throws SQLException {
		pstmt.setString(7, subject.getName());
	}

}
