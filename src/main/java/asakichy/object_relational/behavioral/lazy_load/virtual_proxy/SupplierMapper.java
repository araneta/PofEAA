package asakichy.object_relational.behavioral.lazy_load.virtual_proxy;

import static asakichy.object_relational.behavioral.DB.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import asakichy.object_relational.behavioral.AppRuntimeException;

/**
 * テーブル「suppliers」のデータマッパー.
 * 
 * SupplierにセットするProductには仮想プロキシーを使用します.
 */

public class SupplierMapper {
	private static final String STATEMENT_FIND = "SELECT * FROM suppliers WHERE id = ?";
	public static SupplierMapper instance = new SupplierMapper();

	private SupplierMapper() {
	}

	public static class ProductLoader implements VirtualListLoader<Product> {
		private long id;

		public ProductLoader(long id) {
			this.id = id;
		}

		@Override
		public List<Product> load() {
			return ProductMapper.instance.findForSupplier(id);
		}
	}

	public Supplier find(long id) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_FIND);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			String name = rs.getString(2);
			Supplier supplier = new Supplier(id, name);
			supplier.setProduct(new VirtualList<Product>(new ProductLoader(id)));
			return supplier;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

}
