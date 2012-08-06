package asakichy.object_relational.behavioral.unit_of_work;

import static asakichy.object_relational.behavioral.DB.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import asakichy.object_relational.behavioral.AppRuntimeException;
import asakichy.object_relational.behavioral.DB;

/**
 * テーブル「persons」のデータマッパー.
 */

public class PersonMapper extends AbstractMapper<Person> {
	private static final String STATEMENT_FIND = "SELECT * FROM persons WHERE id = ?";
	private static final String STATEMENT_FIND_BY_LASTNAME = "SELECT * FROM persons WHERE lastname = ?";
	private static final String STATEMENT_INSERT = "INSERT INTO persons VALUES ( ?, ?, ?, ? )";
	private static final String STATEMENT_UPDATE = "UPDATE persons SET lastname = ?, firstname = ?, dependents = ? WHERE id = ?";
	private static final String STATEMENT_DELETE = "DELETE FROM persons WHERE id = ?";

	public List<Person> findByLastName(String name) {
		try {
			PreparedStatement pstmt = DB.prepareStatement(STATEMENT_FIND_BY_LASTNAME);
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			return loadAll(rs);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void update(Person subject) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_UPDATE);
			pstmt.setString(1, subject.getLastName());
			pstmt.setString(2, subject.getFirstName());
			pstmt.setLong(3, subject.getNumberOfDependents());
			pstmt.setLong(4, subject.getId());
			pstmt.executeUpdate();
			loadedMap.put(subject.getId(), subject);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void delete(Person subject) {
		try {
			PreparedStatement stmt = prepareStatement(STATEMENT_DELETE);
			stmt.setLong(1, subject.getId());
			stmt.executeUpdate();
			loadedMap.remove(subject.getId());
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
	protected Person doLoad(long id, ResultSet rs) throws SQLException {
		String lastName = rs.getString(2);
		String firstName = rs.getString(3);
		long numberOfDependents = rs.getLong(4);
		return new Person(id, lastName, firstName, numberOfDependents);
	}

	@Override
	protected void doInsert(Person subject, PreparedStatement pstmt) throws SQLException {
		pstmt.setString(2, subject.getLastName());
		pstmt.setString(3, subject.getFirstName());
		pstmt.setLong(4, subject.getNumberOfDependents());
	}

}
