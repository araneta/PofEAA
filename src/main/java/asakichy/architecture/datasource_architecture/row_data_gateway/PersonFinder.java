package asakichy.architecture.datasource_architecture.row_data_gateway;

import static asakichy.architecture.datasource_architecture.DB.*;
import static asakichy.architecture.datasource_architecture.row_data_gateway.Registry.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import asakichy.architecture.datasource_architecture.AppRuntimeException;

/**
 * テーブル「Person」の検索クラス.
 */

public class PersonFinder {
	private static final String STATEMENT_FIND = "SELECT * FROM persons WHERE id = ?";
	private static final String STATEMENT_FIND_RESPONSIBLE = "SELECT * FROM persons WHERE dependents > 0";

	public PersonGateway find(long id) {
		PersonGateway person = getPerson(id);
		if (person != null) {
			return person;
		}

		try {
			PreparedStatement stmt = prepareStatement(STATEMENT_FIND);
			stmt.setLong(1, id);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			person = PersonGateway.load(rs);
			return person;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public List<PersonGateway> findResponsibles() {
		List<PersonGateway> responsiblePersons = new ArrayList<PersonGateway>();
		try {
			PreparedStatement stmt = prepareStatement(STATEMENT_FIND_RESPONSIBLE);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				responsiblePersons.add(PersonGateway.load(rs));
			}
			return responsiblePersons;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

}
