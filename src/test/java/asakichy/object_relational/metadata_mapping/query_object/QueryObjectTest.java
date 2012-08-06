package asakichy.object_relational.metadata_mapping.query_object;

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

public class QueryObjectTest {

	@Test
	public void データ検索() throws Exception {
		QueryObject<Person> query = new QueryObject<Person>(Person.class);
		query.addCriteria(Criteria.greaterThan("numberOfDependents", 0));
		Set<Person> results = query.execute();

		assertThat(results.size(), is(2));

		for (Person person : results) {
			if (person.getId() == 0) {
				assertThat(person.getFirstName(), is("山田"));
				assertThat(person.getLastName(), is("太郎"));
				assertThat(person.getNumberOfDependents(), is(1));
			} else if (person.getId() == 2) {
				assertThat(person.getFirstName(), is("山田"));
				assertThat(person.getLastName(), is("次郎"));
				assertThat(person.getNumberOfDependents(), is(2));
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
				+ "lastname VARCHAR(32), number_of_dependents INT)");

		stmt.execute("INSERT INTO people VALUES(0,'山田','太郎',1)");
		stmt.execute("INSERT INTO people VALUES(1,'山田','花子',0)");
		stmt.execute("INSERT INTO people VALUES(2,'山田','次郎',2)");
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
