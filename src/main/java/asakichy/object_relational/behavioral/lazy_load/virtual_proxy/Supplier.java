package asakichy.object_relational.behavioral.lazy_load.virtual_proxy;

import java.util.List;

/**
 * Supplierドメインオブジェクト.
 */

public class Supplier {
	private long id;
	private String name;
	private List<Product> products;

	public Supplier(long id, String name, List<Product> products) {
		this.id = id;
		this.name = name;
		this.products = products;
	}

	public Supplier(long id, String name) {
		this(id, name, null);
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProduct(List<Product> products) {
		this.products = products;
	}

}
