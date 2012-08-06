package asakichy.object_relational.metadata_mapping.repository;

/**
 * 検索条件オブジェクト.
 */

public class Criteria {
	private String sqlOperator;
	protected String field;
	protected Object value;

	private Criteria(String sqlOperator, String field, Object value) {
		this.sqlOperator = sqlOperator;
		this.field = field;
		this.value = value;
	}

	public static Criteria greaterThan(String field, Object value) {
		return new Criteria(" > ", field, value);
	}

	public static Criteria equal(String field, Object value) {
		return new Criteria(" = ", field, value);
	}

	public String generateSql(MetaDataMap dataMap) {
		return dataMap.getColumnForField(field) + sqlOperator + value;
	}

}
