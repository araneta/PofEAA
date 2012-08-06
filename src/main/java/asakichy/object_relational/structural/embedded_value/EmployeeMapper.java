package asakichy.object_relational.structural.embedded_value;

import static asakichy.object_relational.structural.DB.*;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import asakichy.object_relational.structural.AppRuntimeException;

/**
 * テーブル「employees」のデータマッパー.
 * 
 * 組込バリューを使用しています.
 */

public class EmployeeMapper {
	private static final String STATEMENT_FIND = "SELECT * FROM employees WHERE id = ?";
	private static final String STATEMENT_INSERT = "INSERT INTO employees VALUES ( ?, ?, ?, ?, ?, ? )";
	private static final String STATEMENT_UPDATE =
			"UPDATE employees SET name = ?, start = ?, end = ?, salary_amount = ?, salary_currency = ? WHERE id = ?";
	private static final String STATEMENT_DELETE = "DELETE FROM employees WHERE id = ?";
	public static EmployeeMapper instance = new EmployeeMapper();

	private EmployeeMapper() {
	}

	private Map<Long, Employee> loaded = new HashMap<Long, Employee>();

	public void refresh() {
		loaded.clear();
	}

	public Employee find(long id) {
		Employee employee = loaded.get(id);
		if (employee != null) {
			return employee;
		}

		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_FIND);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			String name = rs.getString(2);
			Date start = rs.getDate(3);
			Date end = rs.getDate(4);
			long amount = rs.getLong(5);
			String currency = rs.getString(6);

			DateRange period = new DateRange(start, end);
			Money salary = new Money(amount, currency);
			employee = new Employee(id, name, period, salary);

			loaded.put(id, employee);
			return employee;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void insert(Employee employee) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_INSERT);
			pstmt.setLong(1, employee.getId());
			pstmt.setString(2, employee.getName());
			pstmt.setDate(3, employee.getPeriod().getStart());
			pstmt.setDate(4, employee.getPeriod().getEnd());
			pstmt.setLong(5, employee.getSalary().getAmount());
			pstmt.setString(6, employee.getSalary().getCurrency());
			pstmt.executeUpdate();
			loaded.put(employee.getId(), employee);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void update(Employee employee) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_UPDATE);
			pstmt.setString(1, employee.getName());
			pstmt.setDate(2, employee.getPeriod().getStart());
			pstmt.setDate(3, employee.getPeriod().getEnd());
			pstmt.setLong(4, employee.getSalary().getAmount());
			pstmt.setString(5, employee.getSalary().getCurrency());
			pstmt.setLong(6, employee.getId());
			pstmt.executeUpdate();
			loaded.put(employee.getId(), employee);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void delete(Employee employee) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_DELETE);
			pstmt.setLong(1, employee.getId());
			pstmt.executeUpdate();
			loaded.remove(employee.getId());
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

}
