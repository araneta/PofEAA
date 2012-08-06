package asakichy.object_relational.structural.inheritance_mappers.single_table_inheritance;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import asakichy.architecture.distribution.AppRuntimeException;

public class BowlerMapper extends CricketerMapper {
	@Override
	protected DomainObject createDomainObject() {
		return new Bowler();
	}

	@Override
	protected void fillObject(DomainObject domainObject, ResultSet rs) {
		super.fillObject(domainObject, rs);
		Bowler bowler = (Bowler) domainObject;
		try {
			long bowlingAverage = rs.getLong("bowling_average");
			bowler.setBowlingAverage(bowlingAverage);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	@Override
	protected int fillStatement(DomainObject domainObject, PreparedStatement pstmt, int index) {
		try {
			index = super.fillStatement(domainObject, pstmt, index);
			Bowler bowler = (Bowler) domainObject;
			pstmt.setLong(++index, bowler.getBowlingAverage());
			return index;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	@Override
	protected String insertColumns() {
		return super.insertColumns() + ", bowling_average";
	}

	@Override
	protected String insertValues() {
		return super.insertValues() + ", ?";
	}

	@Override
	protected String updateColumns() {
		return super.updateColumns() + ", bowling_average = ?";
	}
}
