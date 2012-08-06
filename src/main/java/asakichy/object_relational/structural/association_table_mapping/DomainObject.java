package asakichy.object_relational.structural.association_table_mapping;

/**
 * ドメインオブジェクトの基本クラス.
 */

public class DomainObject {

	protected long id;

	public DomainObject(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
