package asakichy.offline_concurrency.coarse_grained_lock;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Customerドメインオブジェクト.
 */

public class Customer extends DomainObject {
	private static KeyGenerator keyGenerator = new KeyGenerator("customers", 5);

	public static Customer create(String name, String createdBy, Timestamp created) {
		return new Customer(keyGenerator.nextKey(), Version.create(createdBy), name);
	}

	private String name;
	private List<Address> addresses = new ArrayList<Address>();

	private Customer(long id, Version version, String name) {
		super(id, version);
		this.name = name;
	}

	public Customer(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Address addAddress(String prefecture, String city) {
		Address address = Address.create(this, prefecture, city);
		addresses.add(address);
		return address;
	}

	public List<Address> getAddress() {
		return addresses;
	}
	
	public void setAddress(List<Address> addresses) {
		this.addresses = addresses;
	}

}
