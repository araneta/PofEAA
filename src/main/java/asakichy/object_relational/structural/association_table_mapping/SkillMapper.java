package asakichy.object_relational.structural.association_table_mapping;

import static asakichy.object_relational.structural.DB.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import asakichy.object_relational.structural.AppRuntimeException;

/**
 * テーブル「skills」のデータマッパー.
 */

public class SkillMapper {
	private static final String STATEMENT_FIND = "SELECT * FROM skills WHERE id = ?";
	private static final String STATEMENT_INSERT = "INSERT INTO skills VALUES ( ?, ? )";
	private static final String STATEMENT_UPDATE = "UPDATE skills SET name = ? WHERE id = ?";
	private static final String STATEMENT_DELETE = "DELETE FROM skills WHERE id = ?";
	public static SkillMapper instance = new SkillMapper();

	private SkillMapper() {
	}

	private Map<Long, Skill> loadedMap = new HashMap<Long, Skill>();

	public Skill find(long id) {
		Skill skill = loadedMap.get(id);
		if (skill != null) {
			return skill;
		}

		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_FIND);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			String name = rs.getString(2);
			skill = new Skill(id, name);
			loadedMap.put(id, skill);
			return skill;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void insert(Skill skill) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_INSERT);
			pstmt.setLong(1, skill.getId());
			pstmt.setString(2, skill.getName());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void update(Skill skill) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_UPDATE);
			pstmt.setString(1, skill.getName());
			pstmt.setLong(2, skill.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void delete(Skill skill) {
		try {
			PreparedStatement stmt = prepareStatement(STATEMENT_DELETE);
			stmt.setLong(1, skill.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

}
