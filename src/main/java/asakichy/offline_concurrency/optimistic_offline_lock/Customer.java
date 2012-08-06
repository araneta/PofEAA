package asakichy.offline_concurrency.optimistic_offline_lock;

import java.sql.Timestamp;

/**
 * Customerドメインオブジェクト.
 */

public class Customer extends DomainObject {
	private String name;

	public Customer(String name) {
		this.name = name;
	}

	public Customer(long id, String createdBy, Timestamp created, String modifiedBy, Timestamp modified, long version,
			String name) {
		super(id, createdBy, created, modifiedBy, modified, version);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
