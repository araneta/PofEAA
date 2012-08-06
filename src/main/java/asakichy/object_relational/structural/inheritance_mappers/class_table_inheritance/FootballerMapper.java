package asakichy.object_relational.structural.inheritance_mappers.class_table_inheritance;

import static asakichy.object_relational.structural.DB.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import asakichy.architecture.distribution.AppRuntimeException;

public class FootballerMapper extends AbstractPlayerMapper {
	private static String TABLE_NAME = "footballers";

	@Override
	protected DomainObject createDomainObject() {
		return new FootBaller();
	}

	@Override
	protected void updateRow(DomainObject domainObject) {
		super.updateRow(domainObject);
		try {
			String stmt = String.format(STATEMENT_UPDATE, TABLE_NAME, "club=?");
			FootBaller footBaller = (FootBaller) domainObject;
			PreparedStatement pstmt = prepareStatement(stmt);
			pstmt.setString(1, footBaller.getClub());
			pstmt.setLong(2, footBaller.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	@Override
	protected void insertRow(DomainObject domainObject) {
		super.insertRow(domainObject);
		try {
			String stmt = String.format(STATEMENT_INSERT, TABLE_NAME, "?,?");
			PreparedStatement pstmt = prepareStatement(stmt);
			FootBaller footBaller = (FootBaller) domainObject;
			pstmt.setLong(1, footBaller.getId());
			pstmt.setString(2, footBaller.getClub());
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
			String club = rs.getString("club");
			footBaller.setClub(club);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}
}
