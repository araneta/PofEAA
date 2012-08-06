package asakichy.object_relational.behavioral.lazy_load.ghost;

/**
 * ドメインオブジェクトの基本クラス.
 * 
 * ゴースト状態を管理します.
 */

public abstract class DomainObject {
	enum Status {
		GHOST, LOADING, LOADED
	}

	Status status;
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

	public boolean ghost() {
		return status == Status.GHOST;
	}

	public boolean loaded() {
		return status == Status.LOADED;
	}

	public void markLoading() {
		status = Status.LOADING;
	}

	public void markLoaded() {
		status = Status.LOADED;
	}

}
