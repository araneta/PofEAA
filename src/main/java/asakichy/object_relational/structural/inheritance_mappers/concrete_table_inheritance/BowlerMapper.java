package asakichy.object_relational.structural.inheritance_mappers.concrete_table_inheritance;

import static asakichy.object_relational.structural.DB.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import asakichy.architecture.distribution.AppRuntimeException;
import asakichy.object_relational.structural.inheritance_mappers.concrete_table_inheritance.Player.TYPE;

public class BowlerMapper extends AbstractPlayerMapper {
	private static String TABLE_NAME = "bowlers";

	@Override
	protected DomainObject createDomainObject() {
		return new Bowler();
	}

	@Override
	protected void updateRow(DomainObject domainObject) {
		try {
			String stmt = String.format(STATEMENT_UPDATE, TABLE_NAME, "name=?,batting_average=?,bowling_average=?");
			Bowler bowler = (Bowler) domainObject;
			PreparedStatement pstmt = prepareStatement(stmt);
			pstmt.setString(1, bowler.getName());
			pstmt.setLong(2, bowler.getBattingAverage());
			pstmt.setLong(3, bowler.getBowlingAverage());
			pstmt.setLong(4, bowler.getId());
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
			String stmt = String.format(STATEMENT_INSERT, TABLE_NAME, "?,?,?,?");
			PreparedStatement pstmt = prepareStatement(stmt);
			Bowler bowler = (Bowler) domainObject;
			pstmt.setLong(1, bowler.getId());
			pstmt.setString(2, bowler.getName());
			pstmt.setLong(3, bowler.getBattingAverage());
			pstmt.setLong(4, bowler.getBowlingAverage());
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
			String name = rs.getString("name");
			long battingAverage = rs.getLong("batting_average");
			long bowlingAverage = rs.getLong("bowling_average");
			bowler.setName(name);
			bowler.setType(TYPE.BOWLING);
			bowler.setBattingAverage(battingAverage);
			bowler.setBowlingAverage(bowlingAverage);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}
}
