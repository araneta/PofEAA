package asakichy.offline_concurrency.coarse_grained_lock;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import asakichy.offline_concurrency.AppRuntimeException;
import asakichy.offline_concurrency.DB;

/**
 * テーブル「customers」のデータマッパー.
 * 
 * 緩オフラインロック機能を持っています.
 */

public class CustomerMapper extends AbstractMapper<Customer> {
	private static final String STATEMENT_FIND = "SELECT * FROM customers WHERE id = ?";
	private static final String STATEMENT_INSERT = "INSERT INTO customers VALUES ( ?, ?, ? )";
	private static final String STATEMENT_UPDATE = "UPDATE customers SET name = ?  WHERE id = ?";
	private static final String STATEMENT_DELETE = "DELETE FROM customers WHERE id = ?";

	private AddressMapper addressMapper = new AddressMapper();

	@Override
	public Customer find(long id) {
		Customer customer = super.find(id);
		customer.setAddress(addressMapper.findByCustomerId(id));
		return customer;
	}

	@Override
	public void insert(Customer subject) {
		subject.getVersion().insert();
		super.insert(subject);
		for (Address address : subject.getAddress()) {
			addressMapper.insert(address);
		}
	}

	public void update(Customer subject) {
		subject.getVersion().increment();
		doUpdate(subject);
		addressMapper.deleteByCustomerId(subject);
		for (Address address : subject.getAddress()) {
			addressMapper.insert(address);
		}
	}

	private void doUpdate(Customer subject) {
		try {
			PreparedStatement pstmt = DB.prepareStatement(STATEMENT_UPDATE);
			pstmt.setString(1, subject.getName());
			pstmt.setLong(2, subject.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void delete(Customer subject) {
		super.delete(subject);
		addressMapper.deleteByCustomerId(subject);
		subject.getVersion().delete();
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
		Customer customer = new Customer(name);

		long versionId = rs.getLong("version_id");
		Version version = Version.find(versionId);
		customer.setVersion(version);

		return customer;
	}

	@Override
	protected void doInsert(Customer subject, PreparedStatement pstmt, int index) throws SQLException {
		pstmt.setString(index++, subject.getName());
		pstmt.setLong(index++, subject.getVersion().getId());
	}

}
