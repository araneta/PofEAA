package asakichy.offline_concurrency.optimistic_offline_lock;

import java.sql.Timestamp;

/**
 * ドメインオブジェクトの基本クラス.
 */

public class DomainObject {
	private long id;
	private Timestamp created;
	private String createdBy;
	private Timestamp modified;
	private String modifiedBy;
	private long version;

	public DomainObject(long id, String createdBy, Timestamp created, String modifiedBy, Timestamp modified, long version) {
		this.id = id;
		this.createdBy = createdBy;
		this.created = created;
		this.modified = modified;
		this.modifiedBy = modifiedBy;
		this.version = version;
	}

	public DomainObject() {
		this(-1, null, null, null, null, -1);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Timestamp getCreated() {
		return created;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public Timestamp getModified() {
		return modified;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public long getVersion() {
		return version;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setModified(Timestamp modified) {
		this.modified = modified;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public void setVersion(long version) {
		this.version = version;
	}
}
