package asakichy.object_relational.behavioral.lazy_load.ghost;

import static asakichy.object_relational.behavioral.DB.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class EmployeeMapperTest {
	@Test
	public void ゴースト() throws Exception {
		Employee employee = EmployeeMapper.instance.find(0);
		assertThat(employee.ghost(), is(true));
		assertThat(employee.getName(), is("山田太郎"));
		assertThat(employee.loaded(), is(true));
	}

	@Before
	public void 準備() throws SQLException {
		Statement stmt = createStatement();
		stmt.execute("DROP TABLE IF EXISTS employees");
		stmt.execute("CREATE TABLE employees ( id BIGINT PRIMARY KEY, name VARCHAR(32) )");
		stmt.execute("INSERT INTO employees VALUES(0,'山田太郎')");
		stmt.execute("INSERT INTO employees VALUES(1,'山田花子')");
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
