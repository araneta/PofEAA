package asakichy.object_relational.metadata_mapping.repository;

import java.util.Set;

/**
 * リポジトリの基本クラス.
 */

public class Repository<E extends DomainObject> {

	public Set<E> matching(Criteria criteria) {
		QueryObject<E> query = new QueryObject<E>(Person.class);
		query.addCriteria(criteria);
		return query.execute();
	}
}
