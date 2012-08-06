package asakichy.offline_concurrency.coarse_grained_lock;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import asakichy.offline_concurrency.ConcurrencyRuntimeException;
import asakichy.offline_concurrency.DB;

public class CustomerMapperTest {

	@Test
	public void データ操作() throws Exception {
		// 追加
		Customer yamada = Customer.create("山田", "owner", new Timestamp(System.currentTimeMillis()));
		yamada.addAddress("東京都", "品川区");
		yamada.addAddress("千葉県", "千葉市");
		mapper.insert(yamada);
		yamada = mapper.find(yamada.getId());
		assertThat(yamada.getName(), is("山田"));
		assertThat(yamada.getVersion().getId(), is(1L));
		assertThat(yamada.getVersion().getValue(), is(1L));
		List<Address> addresses = yamada.getAddress();
		Address address = addresses.get(0);
		assertThat(address.getPrefecture(), is("東京都"));
		assertThat(address.getCity(), is("品川区"));
		address = addresses.get(1);
		assertThat(address.getPrefecture(), is("千葉県"));
		assertThat(address.getCity(), is("千葉市"));

		// 更新
		yamada.setName("川田");
		yamada.getAddress().clear();
		yamada.addAddress("大阪府", "大阪市");
		mapper.update(yamada);
		Customer kawada = mapper.find(yamada.getId());
		assertThat(kawada.getName(), is("川田"));
		assertThat(kawada.getVersion().getId(), is(1L));
		assertThat(kawada.getVersion().getValue(), is(2L));// version up!
		address = addresses.get(0);
		assertThat(address.getPrefecture(), is("大阪府"));
		assertThat(address.getCity(), is("大阪市"));

		// 削除
		mapper.delete(kawada);

		Statement stmt = DB.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT count(*) FROM versions");
		rs.next();
		int count = rs.getInt(1);
		assertThat(count, is(0));

		rs = stmt.executeQuery("SELECT count(*) FROM customers");
		rs.next();
		count = rs.getInt(1);
		assertThat(count, is(0));

		rs = stmt.executeQuery("SELECT count(*) FROM addresses");
		rs.next();
		count = rs.getInt(1);
		assertThat(count, is(0));
	}

	@Test
	public void ロック検知_顧客更新() throws Exception {
		Customer yamada = Customer.create("山田", "owner", new Timestamp(System.currentTimeMillis()));
		yamada.addAddress("東京都", "品川区");
		yamada.addAddress("千葉県", "千葉市");
		mapper.insert(yamada);

		Customer kawada = mapper.find(yamada.getId());
		Customer shimoda = mapper.find(yamada.getId());

		kawada.setName("川田");
		kawada.getVersion().setModifiedBy("admin01");
		kawada.getVersion().setModified(new Timestamp(System.currentTimeMillis()));
		mapper.update(kawada);

		// 並行性エラー期待値
		thrown.expect(ConcurrencyRuntimeException.class);
		thrown.expectMessage("Version modified by admin01");

		shimoda.setName("下田");
		shimoda.getVersion().setModifiedBy("admin02");
		shimoda.getVersion().setModified(new Timestamp(System.currentTimeMillis()));
		mapper.update(shimoda);
	}

	@Test
	public void ロック検知_顧客アドレス更新() throws Exception {
		Customer yamada = Customer.create("山田", "owner", new Timestamp(System.currentTimeMillis()));
		yamada.addAddress("東京都", "品川区");
		yamada.addAddress("千葉県", "千葉市");
		mapper.insert(yamada);
		
		Customer yamada01 = mapper.find(yamada.getId());
		Customer yamada02 = mapper.find(yamada.getId());
		
		yamada01.getVersion().setModifiedBy("admin01");
		yamada01.getVersion().setModified(new Timestamp(System.currentTimeMillis()));
		yamada01.getAddress().clear();
		yamada01.addAddress("大阪府", "大阪市");
		mapper.update(yamada01);
		
		// 並行性エラー期待値
		thrown.expect(ConcurrencyRuntimeException.class);
		thrown.expectMessage("Version modified by admin01");
		
		yamada02.getVersion().setModifiedBy("admin02");
		yamada02.getVersion().setModified(new Timestamp(System.currentTimeMillis()));
		yamada02.getAddress().clear();
		yamada02.addAddress("和歌山県", "和歌山市");
		mapper.update(yamada02);
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private CustomerMapper mapper;

	@Before
	public void 準備() throws SQLException {
		Statement stmt = DB.createStatement();
		stmt.execute("DROP TABLE IF EXISTS versions");
		stmt.execute("CREATE TABLE versions ( id BIGINT PRIMARY KEY, "
				+ "value BIGINT, modified_by VARCHAR(32), modified TIMESTAMP )");

		stmt.execute("DROP TABLE IF EXISTS customers");
		stmt.execute("CREATE TABLE customers ( id BIGINT PRIMARY KEY, name VARCHAR(32), version_id BIGINT )");

		stmt.execute("DROP TABLE IF EXISTS addresses");
		stmt.execute("CREATE TABLE addresses ( id BIGINT PRIMARY KEY, customer_id BIGINT, "
				+ "prefecture VARCHAR(32), city VARCHAR(32) )");

		stmt.execute("DROP TABLE IF EXISTS keys");
		stmt.execute("CREATE TABLE keys ( name VARCHAR(32) PRIMARY KEY, next_id BIGINT)");
		stmt.execute("INSERT INTO keys VALUES('versions', 1)");
		stmt.execute("INSERT INTO keys VALUES('customers', 1)");
		stmt.execute("INSERT INTO keys VALUES('addresses', 1)");
		
		mapper = new CustomerMapper();
	}

	@BeforeClass
	public static void DB前処理() throws Exception {
		DB.init();
	}

	@AfterClass
	public static void DB後処理() throws Exception {
		DB.terminate();
	}

}
