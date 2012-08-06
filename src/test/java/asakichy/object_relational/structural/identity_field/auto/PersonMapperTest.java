package asakichy.object_relational.structural.identity_field.auto;

import static asakichy.object_relational.structural.DB.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PersonMapperTest {
	@Test
	public void IDの自動生成() throws Exception {
		PersonMapper personMapper = new PersonMapper();
		Person taro = new Person("山田", "太郎", 1);
		personMapper.insert(taro);
		Person foundTaro = personMapper.find(taro.getId());
		assertThat(foundTaro.getId(), is(1L));
		assertThat(foundTaro.getLastName(), is("山田"));
		assertThat(foundTaro.getFirstName(), is("太郎"));
		assertThat(foundTaro.getNumberOfDependents(), is(1L));
	}

	@Before
	public void 準備() throws SQLException {
		Statement stmt = createStatement();
		stmt.execute("DROP TABLE IF EXISTS persons");
		stmt.execute("CREATE TABLE persons ( id BIGINT PRIMARY KEY, "
				+ "lastname VARCHAR(32), firstname VARCHAR(32), dependents BIGINT )");

		stmt.execute("DROP SEQUENCE IF EXISTS person_id");
		stmt.execute("CREATE SEQUENCE person_id START WITH 1 INCREMENT BY 1");
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
