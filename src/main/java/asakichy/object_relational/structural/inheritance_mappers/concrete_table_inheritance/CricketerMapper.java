package asakichy.object_relational.structural.inheritance_mappers.concrete_table_inheritance;

import static asakichy.object_relational.structural.DB.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import asakichy.architecture.distribution.AppRuntimeException;
import asakichy.object_relational.structural.inheritance_mappers.concrete_table_inheritance.Player.TYPE;

public class CricketerMapper extends AbstractPlayerMapper {
	private static String TABLE_NAME = "cricketers";

	@Override
	protected DomainObject createDomainObject() {
		return new Cricketer();
	}

	@Override
	protected void updateRow(DomainObject domainObject) {
		try {
			String stmt = String.format(STATEMENT_UPDATE, TABLE_NAME, "name=?,batting_average=?");
			Cricketer cricketer = (Cricketer) domainObject;
			PreparedStatement pstmt = prepareStatement(stmt);
			pstmt.setString(1, cricketer.getName());
			pstmt.setLong(2, cricketer.getBattingAverage());
			pstmt.setLong(3, cricketer.getId());
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
			Cricketer cricketer = (Cricketer) domainObject;
			pstmt.setLong(1, cricketer.getId());
			pstmt.setString(2, cricketer.getName());
			pstmt.setLong(3, cricketer.getBattingAverage());
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
			String name = rs.getString("name");
			long battingAverage = rs.getLong("batting_average");
			cricketer.setName(name);
			cricketer.setType(TYPE.CRICKET);
			cricketer.setBattingAverage(battingAverage);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

}
