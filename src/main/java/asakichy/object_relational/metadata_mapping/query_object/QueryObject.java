package asakichy.object_relational.metadata_mapping.query_object;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * クエリーオブジェクト.
 */

public class QueryObject<E> {
	private Class<?> klass;
	private List<Criteria> criterias = new ArrayList<Criteria>();

	public QueryObject(Class<?> klass) {
		this.klass = klass;
	}

	public void addCriteria(Criteria criteria) {
		criterias.add(criteria);
	}

	@SuppressWarnings("unchecked")
	public Set<E> execute() {
		return (Set<E>) Mapper.getMapper(klass).findObjectWhere(genearteWhereClause());
	}

	private String genearteWhereClause() {
		StringBuffer sb = new StringBuffer();
		for (Criteria criteria : criterias) {
			if (sb.length() != 0) {
				sb.append(" AND ");
			}
			sb.append(criteria.generateSql(Mapper.getMapper(klass).getDataMap()));
		}
		return sb.toString();
	}

}
