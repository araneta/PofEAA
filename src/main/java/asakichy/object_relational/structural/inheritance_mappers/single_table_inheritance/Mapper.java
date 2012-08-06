package asakichy.object_relational.structural.inheritance_mappers.single_table_inheritance;

import static asakichy.object_relational.structural.DB.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import asakichy.object_relational.structural.AppRuntimeException;

public abstract class Mapper {
	private static final String STATEMENT_FIND = "SELECT * FROM %s WHERE id = ?";
	private static final String STATEMENT_UPDATE = "UPDATE %s SET %s WHERE id = ?";
	private static final String STATEMENT_DELETE = "DELETE FROM %s WHERE id = ?";
	private static final String STATEMENT_INSERT = "INSERT INTO %s(%s) VALUES(%s)";

	public void insert(DomainObject domainObject) {
		try {
			String stmtInsert = String.format(STATEMENT_INSERT, table(), insertColumns(), insertValues());
			PreparedStatement pstmt = prepareStatement(stmtInsert);
			int index = 0;
			pstmt.setLong(++index, domainObject.getId());
			fillStatement(domainObject, pstmt, index);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void update(DomainObject domainObject) {
		try {
			String stmtUpdate = String.format(STATEMENT_UPDATE, table(), updateColumns());
			PreparedStatement pstmt = prepareStatement(stmtUpdate);
			int index = 0;
			index = fillStatement(domainObject, pstmt, index);
			pstmt.setLong(++index, domainObject.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void delete(DomainObject domainObject) {
		try {
			String stmtDelete = String.format(STATEMENT_DELETE, table());
			PreparedStatement pstmt = prepareStatement(stmtDelete);
			pstmt.setLong(1, domainObject.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public DomainObject find(long id) {
		ResultSet rs = findRow(id);
		return buildDomainObject(rs);
	}

	protected ResultSet findRow(long id) {
		try {
			String stmtFind = String.format(STATEMENT_FIND, table());
			PreparedStatement pstmt = prepareStatement(stmtFind);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			return rs;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	protected DomainObject buildDomainObject(ResultSet rs) {
		DomainObject domainObject = createDomainObject();
		fillObject(domainObject, rs);
		return domainObject;
	}

	protected void fillObject(DomainObject domainObject, ResultSet rs) {
		try {
			long id = rs.getLong("id");
			domainObject.setId(id);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	protected int fillStatement(DomainObject domainObject, PreparedStatement pstmt, int index) {
		return index;
	}

	protected String insertValues() {
		return "?";
	}

	protected String insertColumns() {
		return "id";
	}

	protected String updateColumns() {
		return "";
	}

	abstract protected DomainObject createDomainObject();

	abstract protected String table();
}
