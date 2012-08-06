package asakichy.object_relational.behavioral.lazy_load.virtual_proxy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import asakichy.object_relational.behavioral.AppRuntimeException;
import asakichy.object_relational.behavioral.DB;

/**
 * テーブル「products」のデータマッパー.
 */

public class ProductMapper {
	private static final String STATEMENT_FIND_FOR_SUPPLIER = "SELECT * FROM products WHERE supplier_id = ?";
	public static ProductMapper instance = new ProductMapper();

	private ProductMapper() {
	}

	public List<Product> findForSupplier(long id) {
		try {
			PreparedStatement pstmt = DB.prepareStatement(STATEMENT_FIND_FOR_SUPPLIER);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			List<Product> results = new ArrayList<Product>();
			while (rs.next()) {
				String name = rs.getString(2);
				results.add(new Product(id, name));
			}
			return results;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

}
