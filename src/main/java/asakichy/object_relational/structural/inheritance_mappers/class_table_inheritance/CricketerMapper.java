package asakichy.object_relational.structural.inheritance_mappers.class_table_inheritance;

import static asakichy.object_relational.structural.DB.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import asakichy.architecture.distribution.AppRuntimeException;

public class CricketerMapper extends AbstractPlayerMapper {
	private static String TABLE_NAME = "cricketers";

	@Override
	protected DomainObject createDomainObject() {
		return new Cricketer();
	}

	@Override
	protected void updateRow(DomainObject domainObject) {
		super.updateRow(domainObject);
		try {
			String stmt = String.format(STATEMENT_UPDATE, TABLE_NAME, "batting_average=?");
			Cricketer cricketer = (Cricketer) domainObject;
			PreparedStatement pstmt = prepareStatement(stmt);
			pstmt.setLong(1, cricketer.getBattingAverage());
			pstmt.setLong(2, cricketer.getId());
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
			Cricketer cricketer = (Cricketer) domainObject;
			pstmt.setLong(1, cricketer.getId());
			pstmt.setLong(2, cricketer.getBattingAverage());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	@Override
	protected void fillObject(DomainObject domainObject, long id) {
		super.fillObject(domainObject, id);
		ResultSet rs = findRowByTable(id, TABLE_NAME);
		Cricketer cricketer = (Cricketer) domainObject;
		try {
			long battingAverage = rs.getLong("batting_average");
			cricketer.setBattingAverage(battingAverage);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

}
