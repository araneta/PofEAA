package asakichy.object_relational.structural.inheritance_mappers.single_table_inheritance;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import asakichy.object_relational.structural.AppRuntimeException;

public abstract class AbstractPlayerMapper extends Mapper {
	@Override
	public Player buildDomainObject(ResultSet rs) {
		return (Player) super.buildDomainObject(rs);
	}

	@Override
	protected void fillObject(DomainObject domainObject, ResultSet rs) {
		super.fillObject(domainObject, rs);
		Player player = (Player) domainObject;
		try {
			String name = rs.getString("name");
			player.setName(name);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	@Override
	protected int fillStatement(DomainObject domainObject, PreparedStatement pstmt, int index) {
		try {
			Player player = (Player) domainObject;
			pstmt.setString(++index, player.getName());
			pstmt.setString(++index, player.getType().toString());
			return index;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	@Override
	protected String insertColumns() {
		return super.insertColumns() + ", name, type";
	}

	@Override
	protected String updateColumns() {
		return super.updateColumns() + "name = ?, type = ?";
	}

	@Override
	protected String insertValues() {
		return super.insertValues() + ", ?, ?";
	}

	@Override
	protected String table() {
		return "players";
	}

}
