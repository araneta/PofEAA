package asakichy.offline_concurrency.implicit_lock;

/**
 * ドメインオブジェクトの基本クラス.
 */

public class DomainObject {
	protected long id;

	public DomainObject(long id) {
		this.id = id;
	}

	public DomainObject() {
		this(-1);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
