package asakichy.architecture.datasource_architecture.active_record;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static asakichy.architecture.datasource_architecture.DB.*;

import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import asakichy.architecture.datasource_architecture.AppRuntimeException;
import asakichy.architecture.datasource_architecture.active_record.Person;

public class PersonTest {
	@Test
	public void データ追加更新() throws Exception {
		// 追加
		Person taro = new Person(0, "山田", "太郎", 1);
		taro.insert();

		Person foundTaro = Person.find(0);
		assertThat(foundTaro.getId(), is(0L));
		assertThat(foundTaro.getLastName(), is("山田"));
		assertThat(foundTaro.getFirstName(), is("太郎"));
		assertThat(foundTaro.getNumberOfDependents(), is(1L));

		// 更新
		taro.setNumberOfDependents(2);
		taro.update();
		foundTaro = Person.find(0);
		assertThat(foundTaro.getNumberOfDependents(), is(2L));

	}

	@Test(expected = AppRuntimeException.class)
	public void データ削除() throws Exception {
		Person taro = new Person(0, "山田", "太郎", 1);
		taro.insert();

		// 削除
		taro.delete();
		Person.find(0);
	}

	@Test
	public void 控除額() throws Exception {
		Person taro = new Person(0, "山田", "太郎", 1);
		taro.insert();
		Person foundTaro = Person.find(0);
		assertThat(foundTaro.exemption(), is(2250L));

		Person hanako = new Person(1, "山田", "花子", 0);
		hanako.insert();
		Person foundHanako = Person.find(1);
		assertThat(foundHanako.exemption(), is(1500L));
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
