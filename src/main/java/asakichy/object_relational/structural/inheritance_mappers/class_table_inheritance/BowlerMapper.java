package asakichy.object_relational.structural.inheritance_mappers.class_table_inheritance;

import static asakichy.object_relational.structural.DB.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import asakichy.architecture.distribution.AppRuntimeException;

public class BowlerMapper extends CricketerMapper {
	private static String TABLE_NAME = "bowlers";

	@Override
	protected DomainObject createDomainObject() {
		return new Bowler();
	}

	@Override
	protected void updateRow(DomainObject domainObject) {
		super.updateRow(domainObject);
		try {
			String stmt = String.format(STATEMENT_UPDATE, TABLE_NAME, "bowling_average=?");
			Bowler blowler = (Bowler) domainObject;
			PreparedStatement pstmt = prepareStatement(stmt);
			pstmt.setLong(1, blowler.getBowlingAverage());
			pstmt.setLong(2, blowler.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	@Override
	protected void deleteRow(DomainObject domainObject) {
		super.deleteRow(domainObject);
		deleteRowByTable(domainObject, TABLE_NAME);
	}

	@Override
	protected void insertRow(DomainObject domainObject) {
		super.insertRow(domainObject);
		try {
			String stmt = String.format(STATEMENT_INSERT, TABLE_NAME, "?,?");
			PreparedStatement pstmt = prepareStatement(stmt);
			Bowler bowler = (Bowler) domainObject;
			pstmt.setLong(1, bowler.getId());
			pstmt.setLong(2, bowler.getBowlingAverage());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	@Override
	protected void fillObject(DomainObject domainObject, long id) {
		super.fillObject(domainObject, id);
		ResultSet rs = findRowByTable(id, TABLE_NAME);
		Bowler bowler = (Bowler) domainObject;
		try {
			long bowlingAverage = rs.getLong("bowling_average");
			bowler.setBowlingAverage(bowlingAverage);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}
}
