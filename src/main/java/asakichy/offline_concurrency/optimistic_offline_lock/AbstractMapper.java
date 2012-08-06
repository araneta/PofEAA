package asakichy.offline_concurrency.optimistic_offline_lock;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asakichy.offline_concurrency.AppRuntimeException;
import asakichy.offline_concurrency.ConcurrencyRuntimeException;
import asakichy.offline_concurrency.DB;

/**
 * データマッパーの基本クラス.
 * 
 * 軽オフラインロック機能を持っています.
 */

public abstract class AbstractMapper<E extends DomainObject> implements Finder<E> {
	protected Map<Long, E> identityMap = new HashMap<Long, E>();

	abstract protected String findStatement();

	abstract protected String insertStatement();

	abstract protected String deleteStatement();

	abstract protected E doLoad(ResultSet rs) throws SQLException;

	abstract protected void doInsert(E domainObject, PreparedStatement pstmt) throws SQLException;

	@Override
	public E find(long id) {
		E domainObject = identityMap.get(id);
		if (domainObject != null) {
			return domainObject;
		}

		try {
			PreparedStatement pstmt = DB.prepareStatement(findStatement());
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			domainObject = load(rs);
			return domainObject;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void insert(E domainObject) {
		try {
			PreparedStatement pstmt = DB.prepareStatement(insertStatement());
			pstmt.setLong(1, domainObject.getId());
			pstmt.setString(2, domainObject.getCreatedBy());
			pstmt.setTimestamp(3, domainObject.getCreated());
			pstmt.setString(4, domainObject.getModifiedBy());
			pstmt.setTimestamp(5, domainObject.getModified());
			pstmt.setLong(6, domainObject.getVersion());
			doInsert(domainObject, pstmt);
			pstmt.executeUpdate();
			identityMap.put(domainObject.getId(), domainObject);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void delete(E domainObjcet) {
		try {
			identityMap.remove(domainObjcet.getId());
			PreparedStatement stmt = DB.prepareStatement(deleteStatement());
			stmt.setLong(1, domainObjcet.getId());
			int deletedCount = stmt.executeUpdate();
			if (deletedCount == 0) {
				throwConcurrencyException(domainObjcet);
			}
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	protected E load(ResultSet rs) throws SQLException {
		E domainObjcet = doLoad(rs);

		long id = rs.getLong("id");
		String createdBy = rs.getString("created_by");
		Timestamp created = rs.getTimestamp("created");
		String modifiedBy = rs.getString("modified_by");
		Timestamp modified = rs.getTimestamp("modified");
		long version = rs.getLong("version");

		domainObjcet.setId(id);
		domainObjcet.setCreatedBy(createdBy);
		domainObjcet.setCreated(created);
		domainObjcet.setModifiedBy(modifiedBy);
		domainObjcet.setModified(modified);
		domainObjcet.setVersion(version);

		identityMap.put(id, domainObjcet);
		return domainObjcet;
	}

	protected List<E> loadAll(ResultSet rs) throws SQLException {
		List<E> results = new ArrayList<E>();
		while (rs.next()) {
			results.add(load(rs));
		}
		return results;
	}

	protected void throwConcurrencyException(E domainObject) throws SQLException {
		PreparedStatement pstmt = DB.prepareStatement(findStatement());
		pstmt.setLong(1, domainObject.getId());
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			E dbRecord = load(rs);
			if (dbRecord.getVersion() > domainObject.getVersion()) {
				throw new ConcurrencyRuntimeException("Modified by " + dbRecord.getModifiedBy() + " at "
						+ dbRecord.getModified());
			} else {
				throw new AppRuntimeException("Unexpected !");
			}
		} else {
			throw new ConcurrencyRuntimeException("Deleted");
		}
	}

}
