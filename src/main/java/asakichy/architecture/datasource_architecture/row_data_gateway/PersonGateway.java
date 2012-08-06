package asakichy.architecture.datasource_architecture.row_data_gateway;

import static asakichy.architecture.datasource_architecture.DB.*;
import static asakichy.architecture.datasource_architecture.row_data_gateway.Registry.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * テーブル「Person」の行データゲートウェイ.
 */

public class PersonGateway {
	private static final String STATEMENT_INSERT = "INSERT INTO persons VALUES ( ?, ?, ?, ? )";
	private static final String STATEMENT_UPDATE = "UPDATE persons SET lastname = ?, firstname = ?, dependents = ? WHERE id = ?";
	private static final String STATEMENT_DELETE = "DELETE FROM persons WHERE id = ?";

	public static PersonGateway load(ResultSet rs) throws SQLException {
		long id = rs.getLong("id");
		PersonGateway person = getPerson(id);
		if (person != null) {
			return person;
		}
		String lastName = rs.getString("lastname");
		String firstName = rs.getString("firstname");
		long numberOfDependents = rs.getLong("dependents");
		person = new PersonGateway(id, lastName, firstName, numberOfDependents);
		addPerson(person);
		return person;
	}

	private long id;
	private String lastName;
	private String firstName;
	private long numberOfDependents;

	public PersonGateway(long id, String lastName, String firstName, long numberOfDependents) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.numberOfDependents = numberOfDependents;
	}

	public PersonGateway(long id) {
		this(id, null, null, 0);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public long getNumberOfDependents() {
		return numberOfDependents;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setNumberOfDependents(long numberOfDependents) {
		this.numberOfDependents = numberOfDependents;
	}

	public void insert() throws SQLException {
		PreparedStatement stmt = prepareStatement(STATEMENT_INSERT);
		stmt.setLong(1, id);
		stmt.setString(2, lastName);
		stmt.setString(3, firstName);
		stmt.setLong(4, numberOfDependents);
		stmt.executeUpdate();
		addPerson(this);
	}

	public void update() throws SQLException {
		PreparedStatement stmt = prepareStatement(STATEMENT_UPDATE);
		stmt.setString(1, lastName);
		stmt.setString(2, firstName);
		stmt.setLong(3, numberOfDependents);
		stmt.setLong(4, id);
		stmt.executeUpdate();
		addPerson(this);
	}

	public void delete() throws SQLException {
		PreparedStatement stmt = prepareStatement(STATEMENT_DELETE);
		stmt.setLong(1, id);
		stmt.executeUpdate();
		removePerson(this);
	}
}
