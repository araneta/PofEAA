package asakichy.object_relational.metadata_mapping.repository;

import static asakichy.object_relational.metadata_mapping.DB.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PersonTest {
	@Test
	public void データ検索() throws Exception {
		Person taro = new Person(0);
		Set<Person> results = taro.dependents();

		assertThat(results.size(), is(2));

		for (Person person : results) {
			if (person.getId() == 1) {
				assertThat(person.getFirstName(), is("山田"));
				assertThat(person.getLastName(), is("花子"));
				assertThat(person.getNumberOfDependents(), is(0));
				assertThat(person.getBenefactor(), is(0L));
			} else if (person.getId() == 2) {
				assertThat(person.getFirstName(), is("山田"));
				assertThat(person.getLastName(), is("次郎"));
				assertThat(person.getNumberOfDependents(), is(2));
				assertThat(person.getBenefactor(), is(0L));
			} else {
				fail();
			}
		}
	}

	@Before
	public void 準備() throws SQLException {
		Statement stmt = createStatement();
		stmt.execute("DROP TABLE IF EXISTS people");
		stmt.execute("CREATE TABLE people ( id BIGINT PRIMARY KEY, firstname VARCHAR(32), "
				+ "lastname VARCHAR(32), number_of_dependents INT, benefactor BIGINT)");

		// id=0が、id=1,2を扶養
		stmt.execute("INSERT INTO people VALUES(0,'山田','太郎',1,-1)");
		stmt.execute("INSERT INTO people VALUES(1,'山田','花子',0,0)");
		stmt.execute("INSERT INTO people VALUES(2,'山田','次郎',2,0)");
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
