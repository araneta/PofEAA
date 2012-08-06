package asakichy.object_relational.behavioral.lazy_load.value_holder;

import java.util.List;

/**
 * Supplierドメインオブジェクト.
 * 
 * Productの保持にバリューホルダーを使用しています.
 */

public class Supplier {
	private long id;
	private String name;

	private ValueHolder<Product> products;

	public Supplier(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Product> getProducts() {
		return products.getValues();
	}

	public void setProducts(ValueHolder<Product> products) {
		this.products = products;
	}

}
