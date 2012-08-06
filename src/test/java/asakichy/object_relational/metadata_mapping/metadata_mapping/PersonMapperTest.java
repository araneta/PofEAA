package asakichy.object_relational.metadata_mapping.metadata_mapping;

import static asakichy.object_relational.metadata_mapping.DB.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PersonMapperTest {

	private PersonMapper personMapper;

	@Test
	public void データ検索() throws Exception {
		Person taro = personMapper.findObject(0);
		assertThat(taro.getId(), is(0L));
		assertThat(taro.getFirstName(), is("山田"));
		assertThat(taro.getLastName(), is("太郎"));
		assertThat(taro.getNumberOfDependents(), is(1));
	}

	@Test
	public void データ更新() throws Exception {
		Person jiro = new Person(0, "田山", "次郎", 0);
		personMapper.update(jiro);

		jiro = personMapper.findObject(0);
		assertThat(jiro.getId(), is(0L));
		assertThat(jiro.getFirstName(), is("田山"));
		assertThat(jiro.getLastName(), is("次郎"));
		assertThat(jiro.getNumberOfDependents(), is(0));
	}

	@Test
	public void データ追加() throws Exception {
		Person jiro = new Person(4, "田山", "次郎", 0);
		personMapper.insert(jiro);

		jiro = personMapper.findObject(4);
		assertThat(jiro.getId(), is(4L));
		assertThat(jiro.getFirstName(), is("田山"));
		assertThat(jiro.getLastName(), is("次郎"));
		assertThat(jiro.getNumberOfDependents(), is(0));
	}

	@Test
	public void データ削除() throws Exception {
		Person taro = new Person(0, "", "", 0);
		personMapper.delete(taro);

		Statement stmt = createStatement();
		ResultSet rs = stmt.executeQuery("SELECT count(*) FROM people WHERE id=0");
		rs.next();
		int count = rs.getInt(1);
		assertThat(count, is(0));
	}

	@Before
	public void 準備() throws SQLException {
		Statement stmt = createStatement();
		stmt.execute("DROP TABLE IF EXISTS people");
		stmt.execute("CREATE TABLE people ( id BIGINT PRIMARY KEY, firstname VARCHAR(32), "
				+ "lastname VARCHAR(32), number_of_dependents INT)");

		stmt.execute("INSERT INTO people VALUES(0,'山田','太郎',1)");
		stmt.execute("INSERT INTO people VALUES(1,'山田','花子',0)");
		stmt.execute("INSERT INTO people VALUES(2,'山田','次郎',2)");
		personMapper = new PersonMapper();
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
