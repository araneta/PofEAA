package asakichy.object_relational.behavioral.lazy_load.lazy_initialize;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static asakichy.object_relational.behavioral.DB.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SupplierTest {
	@Test
	public void レイジーイニシャライズ() throws Exception {
		Supplier supplier = Supplier.find(0);
		assertThat(supplier.getName(), is("山田"));

		List<Product> products = supplier.getProducts();
		assertThat(products.size(), is(3));
		List<Product> products_second = supplier.getProducts();
		assertThat(products_second.size(), is(3));
		assertThat(products_second, sameInstance(products));
	}

	@Before
	public void 準備() throws SQLException {
		Statement stmt = createStatement();
		stmt.execute("DROP TABLE IF EXISTS products");
		stmt.execute("CREATE TABLE products ( id BIGINT PRIMARY KEY, name VARCHAR(32), supplier_id BIGINT )");

		stmt.execute("DROP TABLE IF EXISTS suppliers");
		stmt.execute("CREATE TABLE suppliers ( id BIGINT PRIMARY KEY, name VARCHAR(32) )");

		stmt.execute("INSERT INTO suppliers VALUES(0,'山田')");
		stmt.execute("INSERT INTO products VALUES(0,'WordProcessor',0)");
		stmt.execute("INSERT INTO products VALUES(1,'DataBase',0)");
		stmt.execute("INSERT INTO products VALUES(2,'SpreadSheet',0)");
	}

	@BeforeClass
	public static void DB前処理() throws Exception {
		init();
	}

	@AfterClass
	public static void DB後処理() throws Exception {
		terminate();
	}

}
