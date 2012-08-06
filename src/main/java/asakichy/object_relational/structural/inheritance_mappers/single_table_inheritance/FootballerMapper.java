package asakichy.object_relational.structural.inheritance_mappers.single_table_inheritance;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import asakichy.architecture.distribution.AppRuntimeException;

public class FootballerMapper extends AbstractPlayerMapper {

	@Override
	protected DomainObject createDomainObject() {
		return new FootBaller();
	}

	@Override
	protected void fillObject(DomainObject domainObject, ResultSet rs) {
		super.fillObject(domainObject, rs);
		FootBaller footBaller = (FootBaller) domainObject;
		try {
			String club = rs.getString("club");
			footBaller.setClub(club);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	@Override
	protected int fillStatement(DomainObject domainObject, PreparedStatement pstmt, int index) {
		try {
			index = super.fillStatement(domainObject, pstmt, index);
			FootBaller footBaller = (FootBaller) domainObject;
			pstmt.setString(++index, footBaller.getClub());
			return index;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	@Override
	protected String insertColumns() {
		return super.insertColumns() + ", club";
	}

	@Override
	protected String insertValues() {
		return super.insertValues() + ", ?";
	}

	@Override
	protected String updateColumns() {
		return super.updateColumns() + ", club = ?";
	}

}
