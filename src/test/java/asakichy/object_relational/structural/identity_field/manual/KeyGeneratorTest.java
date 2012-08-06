package asakichy.object_relational.structural.identity_field.manual;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class KeyGeneratorTest {
	@Test
	public void IDの手動生成() throws Exception {
		KeyGenerator generator = new KeyGenerator(conn, "persons", 3);
		assertThat(generator.nextKey(), is(1L));
		assertThat(generator.nextKey(), is(2L));
		assertThat(generator.nextKey(), is(3L));
		assertThat(generator.nextKey(), is(4L));
	}

	@Before
	public void 準備() throws SQLException {
		Statement stmt = conn.createStatement();
		stmt.execute("DROP TABLE IF EXISTS keys");
		stmt.execute("CREATE TABLE keys ( name VARCHAR(32) PRIMARY KEY, next_id BIGINT)");
		stmt.execute("INSERT INTO keys VALUES('persons', 1)");
	}

	private static Connection conn;

	@BeforeClass
	public static void DB前処理() throws Exception {
		Class.forName("org.h2.Driver");
		conn = DriverManager.getConnection("jdbc:h2:./pofeaa.db", "sa", "");

	}

	@AfterClass
	public static void DB後処理() throws Exception {
		if (conn == null) {
			return;
		}
		try {
			if (conn.isClosed() == false) {
				conn.close();
			}
		} catch (SQLException ignore) {
		}
	}

}
