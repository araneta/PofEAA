package asakichy.object_relational.behavioral.unit_of_work;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static asakichy.object_relational.behavioral.DB.*;

import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MapperResitryTest {

	@Test
	public void マッパーの取得とデータ操作() throws Exception {
		DomainObject taro = new Person(0, "山田", "太郎", 1);
		DataMapper mapper = MapperResitry.getMapper(taro.getClass());

		// 追加
		mapper.insert(taro);
		Person foundTaro = (Person) mapper.find(0);
		assertThat(foundTaro.getId(), is(0L));
		assertThat(foundTaro.getLastName(), is("山田"));
		assertThat(foundTaro.getFirstName(), is("太郎"));
		assertThat(foundTaro.getNumberOfDependents(), is(1L));

		// 更新
		taro = new Person(0, "山田", "太郎", 2);
		mapper.update(taro);
		foundTaro = (Person) mapper.find(0);
		assertThat(foundTaro.getNumberOfDependents(), is(2L));
	}

	@Before
	public void 準備() throws SQLException {
		Statement stmt = createStatement();
		stmt.execute("DROP TABLE IF EXISTS persons");
		stmt.execute("CREATE TABLE persons ( id BIGINT PRIMARY KEY, "
				+ "lastname VARCHAR(32), firstname VARCHAR(32), dependents BIGINT )");
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
