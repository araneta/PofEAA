package asakichy.offline_concurrency.implicit_lock;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

public class LockingMapperTest {

	@Test
	public void ロック検知() throws Exception {
		@SuppressWarnings("unchecked")
		Mapper<DomainObject> mock = mock(Mapper.class);
		when(mock.find(0)).thenReturn(new DomainObject(0));

		Mapper<DomainObject> mapper = new LockingMapper<DomainObject>(mock, "admin01");
		DomainObject domainObject = mapper.find(0);
		assertThat(domainObject.getId(), is(0L));

		// エラー期待値
		thrown.expect(ConcurrencyRuntimeException.class);
		thrown.expectMessage("unable to lock 0");

		mapper = new LockingMapper<DomainObject>(mock, "admin02");
		domainObject = mapper.find(0);
	}

	@Before
	public void 準備() throws SQLException {
		Statement stmt = DB.createStatement();
		stmt.execute("DROP TABLE IF EXISTS locks");
		stmt.execute("CREATE TABLE locks ( lockable_id BIGINT PRIMARY KEY, owner VARCHAR(32))");
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@BeforeClass
	public static void DB前処理() throws Exception {
		DB.init();
	}

	@AfterClass
	public static void DB後処理() throws Exception {
		DB.terminate();
	}

}
