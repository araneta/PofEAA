package asakichy.object_relational.behavioral.lazy_load.ghost;

import static asakichy.object_relational.behavioral.DB.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import asakichy.object_relational.behavioral.AppRuntimeException;

/**
 * テーブル「employees」のデータマッパー.
 * 
 * 検索時、最初はゴースト状態のオブジェクトを返します.
 */

public class EmployeeMapper {
	private static final String STATEMENT_LOAD = "SELECT * FROM employees WHERE id = ?";
	public static EmployeeMapper instance = new EmployeeMapper();

	private Map<Long, Employee> employees = new HashMap<Long, Employee>();

	private EmployeeMapper() {
	}

	public Employee find(long id) {
		if (employees.containsKey(id)) {
			return employees.get(id);
		}
		return new Employee(id);// ghost
	}

	public void load(Employee employee) {
		if (!employee.ghost()) {
			return;
		}
		employee.markLoading();
		doLoad(employee);
		employee.markLoaded();
		employees.put(employee.getId(), employee);
	}

	private void doLoad(Employee employee) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_LOAD);
			pstmt.setLong(1, employee.getId());
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			employee.setName(rs.getString(2));
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

}
