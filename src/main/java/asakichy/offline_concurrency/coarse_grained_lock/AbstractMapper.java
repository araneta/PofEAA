package asakichy.offline_concurrency.coarse_grained_lock;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import asakichy.offline_concurrency.AppRuntimeException;
import asakichy.offline_concurrency.DB;

/**
 * データマッパーの基本クラス.
 */

public abstract class AbstractMapper<E extends DomainObject> implements Finder<E> {
	abstract protected String findStatement();

	abstract protected String insertStatement();

	abstract protected String deleteStatement();

	abstract protected E doLoad(ResultSet rs) throws SQLException;

	abstract protected void doInsert(E domainObject, PreparedStatement pstmt, int index) throws SQLException;

	@Override
	public E find(long id) {
		try {
			PreparedStatement pstmt = DB.prepareStatement(findStatement());
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			E domainObject = load(rs);
			return domainObject;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void insert(E domainObject) {
		try {
			PreparedStatement pstmt = DB.prepareStatement(insertStatement());
			pstmt.setLong(1, domainObject.getId());
			doInsert(domainObject, pstmt, 2);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void delete(E domainObjcet) {
		try {
			PreparedStatement stmt = DB.prepareStatement(deleteStatement());
			stmt.setLong(1, domainObjcet.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	protected E load(ResultSet rs) throws SQLException {
		E domainObjcet = doLoad(rs);
		long id = rs.getLong("id");
		domainObjcet.setId(id);
		return domainObjcet;
	}

	protected List<E> loadAll(ResultSet rs) throws SQLException {
		List<E> results = new ArrayList<E>();
		while (rs.next()) {
			results.add(load(rs));
		}
		return results;
	}
}
