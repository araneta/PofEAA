package asakichy.object_relational.structural.inheritance_mappers.class_table_inheritance;

import static asakichy.object_relational.structural.DB.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import asakichy.object_relational.structural.AppRuntimeException;

public abstract class AbstractPlayerMapper extends Mapper {
	private static String TABLE_NAME = "players";

	@Override
	public Player findRow(long id) {
		return (Player) super.findRow(id);
	}

	@Override
	protected void updateRow(DomainObject domainObject) {
		try {
			String stmt = String.format(STATEMENT_UPDATE, TABLE_NAME, "name=?, type=?");
			Player player = (Player) domainObject;
			PreparedStatement pstmt = prepareStatement(stmt);
			pstmt.setString(1, player.getName());
			pstmt.setString(2, player.getType().toString());
			pstmt.setLong(3, player.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	@Override
	protected void deleteRow(DomainObject domainObject) {
		deleteRowByTable(domainObject, TABLE_NAME);
	}

	protected void deleteRowByTable(DomainObject domainObject, String table) {
		try {
			String stmt = String.format(STATEMENT_DELETE, table);
			PreparedStatement pstmt = prepareStatement(stmt);
			pstmt.setLong(1, domainObject.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}

	}

	@Override
	protected void insertRow(DomainObject domainObject) {
		try {
			String stmt = String.format(STATEMENT_INSERT, TABLE_NAME, "?,?,?");
			PreparedStatement pstmt = prepareStatement(stmt);
			Player player = (Player) domainObject;
			pstmt.setLong(1, player.getId());
			pstmt.setString(2, player.getName());
			pstmt.setString(3, player.getType().toString());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}
}
