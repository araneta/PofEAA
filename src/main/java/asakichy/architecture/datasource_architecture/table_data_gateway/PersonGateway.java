package asakichy.architecture.datasource_architecture.table_data_gateway;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * テーブル「Person」のテーブルデータゲートウェイ.
 */

public class PersonGateway {
	private static final String STATEMENT_FIND = "SELECT * FROM persons WHERE id = ?";
	private static final String STATEMENT_FIND_WITH_LASTNAME = "SELECT * FROM persons WHERE lastname = ?";
	private static final String STATEMENT_INSERT = "INSERT INTO persons VALUES ( persons_seq.nextval, ?, ?, ? )";
	private static final String STATEMENT_UPDATE = "UPDATE persons SET lastname = ?, firstname = ?, dependents = ? WHERE id = ?";
	private static final String STATEMENT_DELETE = "DELETE FROM persons WHERE id = ?";

	private Connection conn;

	public PersonGateway(Connection conn) {
		this.conn = conn;
	}

	public ResultSet find(long id) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(STATEMENT_FIND);
		stmt.setLong(1, id);
		return stmt.executeQuery();
	}

	public ResultSet findWithLastName(String lastname) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(STATEMENT_FIND_WITH_LASTNAME);
		stmt.setString(1, lastname);
		return stmt.executeQuery();
	}

	public void insert(String lastname, String firstname, long numberOfDependents) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(STATEMENT_INSERT);
		stmt.setString(1, lastname);
		stmt.setString(2, firstname);
		stmt.setLong(3, numberOfDependents);
		stmt.executeUpdate();
	}

	public void update(long id, String lastname, String firstname, long numberOfDependents) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(STATEMENT_UPDATE);
		stmt.setString(1, lastname);
		stmt.setString(2, firstname);
		stmt.setLong(3, numberOfDependents);
		stmt.setLong(4, id);
		stmt.executeUpdate();
	}

	public void delete(long id) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(STATEMENT_DELETE);
		stmt.setLong(1, id);
		stmt.executeUpdate();
	}

}
