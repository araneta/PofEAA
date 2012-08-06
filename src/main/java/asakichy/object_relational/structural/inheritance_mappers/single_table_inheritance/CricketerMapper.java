package asakichy.object_relational.structural.inheritance_mappers.single_table_inheritance;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import asakichy.architecture.distribution.AppRuntimeException;

public class CricketerMapper extends AbstractPlayerMapper {
	@Override
	protected DomainObject createDomainObject() {
		return new Cricketer();
	}

	@Override
	protected void fillObject(DomainObject domainObject, ResultSet rs) {
		super.fillObject(domainObject, rs);
		Cricketer cricketer = (Cricketer) domainObject;
		try {
			long battingAverage = rs.getLong("batting_average");
			cricketer.setBattingAverage(battingAverage);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	@Override
	protected int fillStatement(DomainObject domainObject, PreparedStatement pstmt, int index) {
		try {
			index = super.fillStatement(domainObject, pstmt, index);
			Cricketer cricketer = (Cricketer) domainObject;
			pstmt.setLong(++index, cricketer.getBattingAverage());
			return index;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	@Override
	protected String insertColumns() {
		return super.insertColumns() + ", batting_average";
	}

	@Override
	protected String insertValues() {
		return super.insertValues() + ", ?";
	}

	@Override
	protected String updateColumns() {
		return super.updateColumns() + ", batting_average = ?";
	}
}
