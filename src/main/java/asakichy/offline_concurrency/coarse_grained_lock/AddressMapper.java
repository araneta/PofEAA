package asakichy.offline_concurrency.coarse_grained_lock;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import asakichy.offline_concurrency.AppRuntimeException;
import asakichy.offline_concurrency.DB;

/**
 * テーブル「addresses」のデータマッパー.
 */

public class AddressMapper extends AbstractMapper<Address> {
	private static final String STATEMENT_FIND = "SELECT * FROM addresses WHERE id = ?";
	private static final String STATEMENT_FIND_BY_CUSTOMER_ID = "SELECT * FROM addresses WHERE customer_id = ?";
	private static final String STATEMENT_INSERT = "INSERT INTO addresses VALUES ( ?, ?, ?, ? )";
	private static final String STATEMENT_DELETE = "DELETE FROM addresses WHERE id = ?";
	private static final String STATEMENT_DELETE_BY_CUSTOMER_ID = "DELETE FROM addresses WHERE customer_id = ?";

	public List<Address> findByCustomerId(long customerId) {
		try {
			PreparedStatement pstmt = DB.prepareStatement(STATEMENT_FIND_BY_CUSTOMER_ID);
			pstmt.setLong(1, customerId);
			ResultSet rs = pstmt.executeQuery();
			return loadAll(rs);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void deleteByCustomerId(Customer customer) {
		try {
			PreparedStatement stmt = DB.prepareStatement(STATEMENT_DELETE_BY_CUSTOMER_ID);
			stmt.setLong(1, customer.getId());
			stmt.executeUpdate();
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
	protected Address doLoad(ResultSet rs) throws SQLException {
		long customerId = rs.getLong("customer_id");
		String prefecture = rs.getString("prefecture");
		String city = rs.getString("city");
		return new Address(customerId, prefecture, city);
	}

	@Override
	protected void doInsert(Address subject, PreparedStatement pstmt, int index) throws SQLException {
		pstmt.setLong(index++, subject.getCustomerId());
		pstmt.setString(index++, subject.getPrefecture());
		pstmt.setString(index++, subject.getCity());
	}

}
