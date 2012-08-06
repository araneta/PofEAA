package asakichy.architecture.datasource_architecture.data_mapper;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import asakichy.architecture.datasource_architecture.DB;
import asakichy.architecture.datasource_architecture.data_mapper.PersonMapper;
import asakichy.architecture.datasource_architecture.data_mapper.domain.Person;

public class PersonMapperTest {
	@Test
	public void データ操作() throws Exception {
		PersonMapper personMapper = new PersonMapper();
		Person taro = new Person(0, "山田", "太郎", 1);

		// 追加
		personMapper.insert(taro);
		Person foundTaro = personMapper.find(0);
		assertThat(foundTaro.getId(), is(0L));
		assertThat(foundTaro.getLastName(), is("山田"));
		assertThat(foundTaro.getFirstName(), is("太郎"));
		assertThat(foundTaro.getNumberOfDependents(), is(1L));

		// 更新
		taro.setNumberOfDependents(2);
		personMapper.update(taro);
		foundTaro = personMapper.find(0);
		assertThat(foundTaro.getNumberOfDependents(), is(2L));

		// 苗字検索
		List<Person> persons = personMapper.findByLastName("山田");
		assertThat(persons.size(), is(1));
		persons = personMapper.findByLastName("田山");
		assertThat(persons.size(), is(0));

		// 削除
		personMapper.delete(taro);
		persons = personMapper.findByLastName("山田");
		assertThat(persons.size(), is(0));
	}

	@Before
	public void 準備() throws SQLException {
		Statement stmt = DB.createStatement();
		stmt.execute("DROP TABLE IF EXISTS persons");
		stmt.execute("CREATE TABLE persons ( id BIGINT PRIMARY KEY, "
				+ "lastname VARCHAR(32), firstname VARCHAR(32), dependents BIGINT )");
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
