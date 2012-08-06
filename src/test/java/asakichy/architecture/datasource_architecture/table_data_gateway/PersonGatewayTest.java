package asakichy.architecture.datasource_architecture.table_data_gateway;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import asakichy.architecture.datasource_architecture.table_data_gateway.PersonGateway;

public class PersonGatewayTest {
	private static Connection conn;

	@Test
	public void データ操作() throws Exception {
		PersonGateway personGateway = new PersonGateway(conn);

		// 挿入
		personGateway.insert("山田", "太郎", 1);
		personGateway.insert("山田", "花子", 0);

		// 取得（ID指定）
		ResultSet rs = personGateway.find(1);
		rs.next();
		assertThat(rs.getString("lastname"), is("山田"));
		assertThat(rs.getString("firstname"), is("太郎"));
		assertThat(rs.getLong("dependents"), is(1L));

		rs = personGateway.find(2);
		rs.next();
		assertThat(rs.getString("lastname"), is("山田"));
		assertThat(rs.getString("firstname"), is("花子"));
		assertThat(rs.getLong("dependents"), is(0L));

		// 取得（苗字指定）
		rs = personGateway.findWithLastName("山田");
		rs.last();
		assertThat(rs.getRow(), is(2));

		// 更新
		personGateway.update(1, "山田", "太郎", 2);
		rs = personGateway.find(1);
		rs.next();
		assertThat(rs.getLong("dependents"), is(2L));

		// 削除
		personGateway.delete(1);
		rs = personGateway.findWithLastName("山田");
		rs.last();
		assertThat(rs.getRow(), is(1));
	}

	@Before
	public void 準備() throws SQLException {
		// テーブル作成(NOT NULL, FOREIGN KEYは省略)
		Statement stmt = conn.createStatement();
		stmt.execute("DROP TABLE IF EXISTS persons");
		stmt.execute("DROP SEQUENCE IF EXISTS persons_seq");
		stmt.execute("CREATE TABLE persons ( id BIGINT PRIMARY KEY, "
				+ "lastname VARCHAR(32), firstname VARCHAR(32), dependents BIGINT )");
		stmt.execute("CREATE SEQUENCE persons_seq START WITH 1");
	}

	@BeforeClass
	public static void コネクション取得() throws Exception {
		// 接続
		Class.forName("org.h2.Driver");
		conn = DriverManager.getConnection("jdbc:h2:./pofeaa.db", "sa", "");
	}

	@AfterClass
	public static void コネクション解放() throws Exception {
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
