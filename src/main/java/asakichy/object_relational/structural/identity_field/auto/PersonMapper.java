package asakichy.object_relational.structural.identity_field.auto;

import static asakichy.object_relational.structural.DB.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asakichy.object_relational.structural.AppRuntimeException;

/**
 * テーブル「persons」のデータマッパー.
 * 
 * レコード追加時に、一意フィールドを自動生成しています.
 */

public class PersonMapper {
	private static final String STATEMENT_FIND = "SELECT * FROM persons WHERE id = ?";
	private static final String STATEMENT_FIND_BY_LASTNAME = "SELECT * FROM persons WHERE lastname = ?";
	private static final String STATEMENT_INSERT = "INSERT INTO persons VALUES ( person_id.nextval, ?, ?, ? )";
	private static final String STATEMENT_UPDATE = "UPDATE persons SET lastname = ?, firstname = ?, dependents = ? WHERE id = ?";
	private static final String STATEMENT_DELETE = "DELETE FROM persons WHERE id = ?";

	private Map<Long, Person> loadedMap = new HashMap<Long, Person>();

	public Person find(long id) {
		Person person = loadedMap.get(id);
		if (person != null) {
			return person;
		}

		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_FIND);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			person = load(rs);
			return person;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public List<Person> findByLastName(String name) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_FIND_BY_LASTNAME);
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			return loadAll(rs);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void insert(Person person) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_INSERT);
			// idはデータベースによる自動生成（シーケンス）
			pstmt.setString(1, person.getLastName());
			pstmt.setString(2, person.getFirstName());
			pstmt.setLong(3, person.getNumberOfDependents());
			pstmt.executeUpdate();

			// 自動生成したidの取得
			ResultSet keys = pstmt.getGeneratedKeys();
			keys.next();
			long id = keys.getLong(1);
			loadedMap.put(id, person);
			person.setId(id);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void update(Person person) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_UPDATE);
			pstmt.setString(1, person.getLastName());
			pstmt.setString(2, person.getFirstName());
			pstmt.setLong(3, person.getNumberOfDependents());
			pstmt.setLong(4, person.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void delete(Person person) {
		try {
			PreparedStatement stmt = prepareStatement(STATEMENT_DELETE);
			stmt.setLong(1, person.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	private Person load(ResultSet rs) throws SQLException {
		long id = rs.getLong(1);
		if (loadedMap.containsKey(id)) {
			return loadedMap.get(id);
		}
		String lastName = rs.getString(2);
		String firstName = rs.getString(3);
		long numberOfDependents = rs.getLong(4);
		Person person = new Person(id, lastName, firstName, numberOfDependents);
		loadedMap.put(id, person);
		return person;
	}

	protected List<Person> loadAll(ResultSet rs) throws SQLException {
		List<Person> results = new ArrayList<Person>();
		while (rs.next()) {
			results.add(load(rs));
		}
		return results;
	}

}
