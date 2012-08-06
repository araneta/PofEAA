package asakichy.offline_concurrency.pessimistic_offline_lock;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import asakichy.offline_concurrency.ConcurrencyRuntimeException;
import asakichy.offline_concurrency.DB;

public class ExclusiveReadLockManagerTest {

	@Test
	public void ロック取得() throws Exception {
		ExclusiveReadLockManager.INSTANCE.aqcuireLock(0, "admin01");
		Statement stmt = DB.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT count(*) FROM locks WHERE lockable_id=0");
		rs.next();
		int count = rs.getInt(1);
		assertThat(count, is(1));
	}

	@Test
	public void ロック解放() throws Exception {
		ExclusiveReadLockManager.INSTANCE.aqcuireLock(0, "admin01");
		ExclusiveReadLockManager.INSTANCE.releaseLock(0, "admin01");
		Statement stmt = DB.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT count(*) FROM locks WHERE lockable_id=0");
		rs.next();
		int count = rs.getInt(1);
		assertThat(count, is(0));
	}

	@Test
	public void ロック衝突() throws Exception {
		ExclusiveReadLockManager.INSTANCE.aqcuireLock(0, "admin01");

		// エラー期待値
		thrown.expect(ConcurrencyRuntimeException.class);
		thrown.expectMessage("unable to lock 0");

		ExclusiveReadLockManager.INSTANCE.aqcuireLock(0, "admin02");
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void 準備() throws SQLException {
		Statement stmt = DB.createStatement();
		stmt.execute("DROP TABLE IF EXISTS locks");
		stmt.execute("CREATE TABLE locks ( lockable_id BIGINT PRIMARY KEY, owner VARCHAR(32))");
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
