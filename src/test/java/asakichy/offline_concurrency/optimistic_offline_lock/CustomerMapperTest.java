package asakichy.offline_concurrency.optimistic_offline_lock;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

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
	public void 更新済み検知() throws Exception {
		// 登録
		String creater = "owner";
		Timestamp created = new Timestamp(System.currentTimeMillis());
		Customer yamada_org = new Customer(0, creater, created, creater, created, 0, "山田");
		Customer yamada = new Customer(0, creater, created, creater, created, 0, "山田");
		mapper.insert(yamada);

		// 更新してversion=1に
		String updater_first = "admin01";
		Timestamp updated_first = new Timestamp(System.currentTimeMillis());
		yamada.setModifiedBy(updater_first);
		yamada.setModified(updated_first);
		mapper.update(yamada);
		assertThat(yamada.getVersion(), is(1L));

		// 並行性エラー期待値
		thrown.expect(ConcurrencyRuntimeException.class);
		thrown.expectMessage("Modified by admin01");

		// 別のユーザがversion=0で更新
		String updater_second = "admin02";
		Timestamp updated_second = new Timestamp(System.currentTimeMillis());
		yamada_org.setModifiedBy(updater_second);
		yamada_org.setModified(updated_second);
		mapper.update(yamada_org);// 並行性エラー

	}

	@Test
	public void 削除済み検知() throws Exception {
		// 登録
		String creater = "owner";
		Timestamp created = new Timestamp(System.currentTimeMillis());
		Customer yamada = new Customer(0, creater, created, creater, created, 0, "山田");
		mapper.insert(yamada);

		// 削除
		mapper.delete(yamada);

		// 並行性エラー期待値
		thrown.expect(ConcurrencyRuntimeException.class);
		thrown.expectMessage("Deleted");

		// 再度削除
		mapper.delete(yamada);
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private CustomerMapper mapper;

	@Before
	public void 準備() throws SQLException {
		Statement stmt = DB.createStatement();
		stmt.execute("DROP TABLE IF EXISTS customers");
		stmt.execute("CREATE TABLE customers ( id BIGINT PRIMARY KEY, "
				+ "created_by VARCHAR(32), created TIMESTAMP, modified_by VARCHAR(32), modified TIMESTAMP, "
				+ "version BIGINT, name VARCHAR(32) )");
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
