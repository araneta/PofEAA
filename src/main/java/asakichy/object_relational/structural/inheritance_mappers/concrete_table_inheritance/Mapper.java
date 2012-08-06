package asakichy.object_relational.structural.inheritance_mappers.concrete_table_inheritance;

import static asakichy.object_relational.structural.DB.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import asakichy.object_relational.structural.AppRuntimeException;
import asakichy.object_relational.structural.NotFoundRuntimeException;

public abstract class Mapper {
	protected static final String STATEMENT_FIND = "SELECT * FROM %s WHERE id = ?";
	protected static final String STATEMENT_UPDATE = "UPDATE %s SET %s WHERE id = ?";
	protected static final String STATEMENT_DELETE = "DELETE FROM %s WHERE id = ?";
	protected static final String STATEMENT_INSERT = "INSERT INTO %s VALUES(%s)";

	public void insert(DomainObject domainObject) {
		insertRow(domainObject);
	}

	abstract void insertRow(DomainObject domainObject);

	public void update(DomainObject domainObject) {
		updateRow(domainObject);
	}

	abstract void updateRow(DomainObject domainObject);

	public void delete(DomainObject domainObject) {
		deleteRow(domainObject);
	}

	abstract protected void deleteRow(DomainObject domainObject);

	protected void deleteRowByTable(DomainObject domainObject, String table) {
		try {
			String stmt = String.format(STATEMENT_DELETE, table);
			PreparedStatement pstmt = prepareStatement(stmt);
			pstmt.setLong(1, domainObject.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}

	}

	public DomainObject find(long id) {
		return findRow(id);
	}

	protected DomainObject findRow(long id) {
		DomainObject domainObject = createDomainObject();
		fillObject(domainObject, id);
		return domainObject;
	}

	protected ResultSet findRowByTable(long id, String tableName) {
		try {
			String stmtFind = String.format(STATEMENT_FIND, tableName);
			PreparedStatement pstmt = prepareStatement(stmtFind);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			boolean exist = rs.next();
			if (exist == false) {
				throw new NotFoundRuntimeException();
			}
			return rs;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	protected void fillObject(DomainObject domainObject, long id) {
		domainObject.setId(id);
	}

	abstract protected DomainObject createDomainObject();
}
