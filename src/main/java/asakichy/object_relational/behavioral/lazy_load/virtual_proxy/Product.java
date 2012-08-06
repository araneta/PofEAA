package asakichy.object_relational.behavioral.lazy_load.virtual_proxy;

/**
 * Productドメインオブジェクト.
 */

public class Product {
	private long id;
	private String name;

	public Product(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

}
