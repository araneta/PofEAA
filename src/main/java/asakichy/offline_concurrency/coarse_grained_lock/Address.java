package asakichy.offline_concurrency.coarse_grained_lock;

/**
 * Addressドメインオブジェクト.
 */

public class Address extends DomainObject {
	private static KeyGenerator keyGenerator = new KeyGenerator("addresses", 5);

	public static Address create(Customer customer, String prefecture, String city) {
		return new Address(keyGenerator.nextKey(), customer, prefecture, city);
	}

	private long customerId;
	private String prefecture;
	private String city;

	public long getCustomerId() {
		return customerId;
	}

	public String getPrefecture() {
		return prefecture;
	}

	public String getCity() {
		return city;
	}

	private Address(long id, Customer customer, String prefecture, String city) {
		super(id, customer.getVersion());
		this.customerId = customer.getId();
		this.prefecture = prefecture;
		this.city = city;
	}

	public Address(long customerId, String prefecture, String city) {
		this.customerId = customerId;
		this.prefecture = prefecture;
		this.city = city;
	}
}
