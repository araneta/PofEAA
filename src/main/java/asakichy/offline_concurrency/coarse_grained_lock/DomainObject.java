package asakichy.offline_concurrency.coarse_grained_lock;

/**
 * ドメインオブジェクトの基本クラス.
 */

public class DomainObject {
	private long id;
	private Version version;

	public DomainObject(long id, Version version) {
		this.id = id;
		this.version = version;
	}

	public DomainObject() {
		this(-1, null);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}
}
