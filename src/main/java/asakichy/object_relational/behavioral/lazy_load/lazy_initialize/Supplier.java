package asakichy.object_relational.behavioral.lazy_load.lazy_initialize;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import asakichy.object_relational.behavioral.AppRuntimeException;
import asakichy.object_relational.behavioral.DB;

/**
 * Supplierドメインオブジェクト.
 * 
 * Productの取得にレイジーイニシャライズを使用しています.
 */
public class Supplier {
	private static final String STATEMENT_FIND = "SELECT * FROM suppliers WHERE id = ?";

	public static Supplier find(long id) {
		try {
			PreparedStatement pstmt = DB.prepareStatement(STATEMENT_FIND);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			return new Supplier(id, rs.getString(2));
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	private List<Product> products;
	private long id;
	private String name;

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
		if (products == null) {
			products = Product.findForSupplier(id);
		}
		return products;
	}

}
