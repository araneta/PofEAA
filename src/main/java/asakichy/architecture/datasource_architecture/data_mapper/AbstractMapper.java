package asakichy.architecture.datasource_architecture.data_mapper;

import static asakichy.architecture.datasource_architecture.DB.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asakichy.architecture.datasource_architecture.AppRuntimeException;
import asakichy.architecture.datasource_architecture.DB;
import asakichy.architecture.datasource_architecture.data_mapper.domain.DomainObject;
import asakichy.architecture.datasource_architecture.data_mapper.domain.Finder;

/**
 * データマッパーの基本クラス.
 */

public abstract class AbstractMapper<E extends DomainObject> implements Finder<E> {
	protected Map<Long, E> loadedMap = new HashMap<Long, E>();

	abstract protected String findStatement();

	abstract protected String insertStatement();

	abstract protected E doLoad(long id, ResultSet rs) throws SQLException;

	abstract protected void doInsert(E domainObject, PreparedStatement pstmt) throws SQLException;

	@Override
	public E find(long id) {
		E domainObject = loadedMap.get(id);
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

	public void insert(E subject) {
		try {
			PreparedStatement pstmt = prepareStatement(insertStatement());
			pstmt.setLong(1, subject.getId());
			doInsert(subject, pstmt);
			pstmt.executeUpdate();
			loadedMap.put(subject.getId(), subject);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	protected E load(ResultSet rs) throws SQLException {
		long id = rs.getLong(1);
		if (loadedMap.containsKey(id)) {
			return loadedMap.get(id);
		}
		E domainObjcet = doLoad(id, rs);
		loadedMap.put(id, domainObjcet);
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
