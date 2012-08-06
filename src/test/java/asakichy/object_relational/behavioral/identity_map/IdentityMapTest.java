package asakichy.object_relational.behavioral.identity_map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class IdentityMapTest {
	@Test
	public void データ操作() throws Exception {
		Person taro = new Person(0, "山田", "太郎", 1);
		IdentityMap.addPerson(taro);

		Person foundTaro = IdentityMap.getPerson(0);
		assertThat(foundTaro.getId(), is(0L));
		assertThat(foundTaro.getLastName(), is("山田"));
		assertThat(foundTaro.getFirstName(), is("太郎"));
		assertThat(foundTaro.getNumberOfDependents(), is(1L));

		Person notfound = IdentityMap.getPerson(1);
		assertThat(notfound, nullValue());
	}
}
