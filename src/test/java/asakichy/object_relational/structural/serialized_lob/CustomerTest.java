package asakichy.object_relational.structural.serialized_lob;

import static asakichy.object_relational.structural.DB.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class CustomerTest {
	
	@Test
	public void データ操作() throws Exception {
		Department area = new Department("area");
		Department area1 = new Department("area1");
		Department area1_1 = new Department("area1_1");
		area1.addSubsidiary(area1_1);
		area.addSubsidiary(area1);
		Customer taro = new Customer(0, "山田太郎", area);
		taro.insert();
		
		Customer foundTaro = Customer.find(0);
		assertThat(foundTaro.getName(), is("山田太郎"));

		assertThat(foundTaro.getDepartment().getName(), is("area"));
		Department foundArea1 = foundTaro.getDepartment().getSubsidiaries().get(0);
		assertThat(foundArea1.getName(), is("area1"));
		Department foundArea1_1 = foundArea1.getSubsidiaries().get(0);
		assertThat(foundArea1_1.getName(), is("area1_1"));
	}
	
	@Before
	public void 準備() throws SQLException {
		Statement stmt = createStatement();
		stmt.execute("DROP TABLE IF EXISTS customers");
		stmt.execute("CREATE TABLE customers ( id BIGINT PRIMARY KEY, name VARCHAR(32), departments CLOB )");
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
