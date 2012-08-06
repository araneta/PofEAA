package asakichy.object_relational.structural.association_table_mapping;

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
 * テーブル「employees」「employee_skills」のデータマッパー.
 * 
 * 関連テーブルマッピングを使用しています.
 */

public class EmployeeMapper {
	private static final String STATEMENT_FIND = "SELECT * FROM employees WHERE id = ?";
	private static final String STATEMENT_FIND_SKILLS = "SELECT skill_id FROM employee_skills WHERE employee_id = ?";
	private static final String STATEMENT_INSERT = "INSERT INTO employees VALUES ( ?, ?, ? )";
	private static final String STATEMENT_INSERT_SKILL = "INSERT INTO employee_skills VALUES ( ?, ? )";
	private static final String STATEMENT_UPDATE = "UPDATE employees SET firstname = ?, lastname = ? WHERE id = ?";
	private static final String STATEMENT_DELETE = "DELETE FROM employees WHERE id = ?";
	private static final String STATEMENT_DELETE_SKILL = "DELETE FROM employee_skills WHERE employee_id = ?";
	public static EmployeeMapper instance = new EmployeeMapper();

	private EmployeeMapper() {
	}

	private Map<Long, Employee> loadedMap = new HashMap<Long, Employee>();

	public Employee find(long id) {
		Employee employee = loadedMap.get(id);
		if (employee != null) {
			return employee;
		}

		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_FIND);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			String firstName = rs.getString(2);
			String lastName = rs.getString(3);
			List<Skill> skills = findSkills(id);
			employee = new Employee(id, firstName, lastName, skills);
			loadedMap.put(id, employee);
			return employee;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	private List<Skill> findSkills(long id) {
		try {
			List<Skill> skills = new ArrayList<Skill>();
			PreparedStatement pstmt = prepareStatement(STATEMENT_FIND_SKILLS);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Skill skill = SkillMapper.instance.find(rs.getLong(1));
				skills.add(skill);
			}
			return skills;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void insert(Employee employee) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_INSERT);
			pstmt.setLong(1, employee.getId());
			pstmt.setString(2, employee.getFirstName());
			pstmt.setString(3, employee.getLastName());
			pstmt.executeUpdate();
			insertSkills(employee);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	private void insertSkills(Employee employee) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_INSERT_SKILL);
			for (Skill skill : employee.getSkills()) {
				pstmt.setLong(1, employee.getId());
				pstmt.setLong(2, skill.getId());
				pstmt.executeUpdate();
			}

		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void update(Employee employee) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_UPDATE);
			pstmt.setString(1, employee.getFirstName());
			pstmt.setString(2, employee.getLastName());
			pstmt.setLong(3, employee.getId());
			pstmt.executeUpdate();
			updateSkills(employee);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	private void updateSkills(Employee employee) {
		deleteSkills(employee);
		insertSkills(employee);
	}

	public void delete(Employee employee) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_DELETE);
			pstmt.setLong(1, employee.getId());
			pstmt.executeUpdate();
			deleteSkills(employee);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	private void deleteSkills(Employee employee) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_DELETE_SKILL);
			pstmt.setLong(1, employee.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

}
