package asakichy.object_relational.structural.inheritance_mappers.concrete_table_inheritance;

import static asakichy.object_relational.structural.DB.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import asakichy.object_relational.structural.AppRuntimeException;
import asakichy.object_relational.structural.inheritance_mappers.concrete_table_inheritance.Player.TYPE;

public class FootballerMapper extends AbstractPlayerMapper {
	private static String TABLE_NAME = "footballers";

	@Override
	protected DomainObject createDomainObject() {
		return new FootBaller();
	}

	@Override
	protected void updateRow(DomainObject domainObject) {
		try {
			String stmt = String.format(STATEMENT_UPDATE, TABLE_NAME, "name=?,club=?");
			FootBaller footBaller = (FootBaller) domainObject;
			PreparedStatement pstmt = prepareStatement(stmt);
			pstmt.setString(1, footBaller.getName());
			pstmt.setString(2, footBaller.getClub());
			pstmt.setLong(3, footBaller.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	@Override
	protected void deleteRow(DomainObject domainObject) {
		deleteRowByTable(domainObject, TABLE_NAME);
	}

	@Override
	protected void insertRow(DomainObject domainObject) {
		try {
			String stmt = String.format(STATEMENT_INSERT, TABLE_NAME, "?,?,?");
			PreparedStatement pstmt = prepareStatement(stmt);
			FootBaller footBaller = (FootBaller) domainObject;
			pstmt.setLong(1, footBaller.getId());
			pstmt.setString(2, footBaller.getName());
			pstmt.setString(3, footBaller.getClub());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	@Override
	protected void fillObject(DomainObject domainObject, long id) {
		super.fillObject(domainObject, id);
		ResultSet rs = findRowByTable(domainObject.getId(), TABLE_NAME);
		FootBaller footBaller = (FootBaller) domainObject;
		try {
			String name = rs.getString("name");
			String club = rs.getString("club");
			footBaller.setName(name);
			footBaller.setType(TYPE.FOOTBALL);
			footBaller.setClub(club);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}
}
