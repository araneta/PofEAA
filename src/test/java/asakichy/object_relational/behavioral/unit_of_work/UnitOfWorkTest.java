package asakichy.object_relational.behavioral.unit_of_work;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static asakichy.object_relational.behavioral.DB.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UnitOfWorkTest {
	@Test
	public void 同一操作() throws Exception {
		// 登録
		UnitOfWork<Person> unitOfWork = new UnitOfWork<Person>();

		Person taro = new Person(0, "山田", "太郎", 1);
		Person hanako = new Person(1, "山田", "花子", 0);
		unitOfWork.resisterNew(taro);
		unitOfWork.resisterNew(hanako);

		unitOfWork.commit();

		PersonMapper mapper = new PersonMapper();
		Person foundTaro = mapper.find(0);
		assertThat(foundTaro.getId(), is(0L));
		assertThat(foundTaro.getLastName(), is("山田"));
		assertThat(foundTaro.getFirstName(), is("太郎"));
		assertThat(foundTaro.getNumberOfDependents(), is(1L));

		Person foundHanako = mapper.find(1);
		assertThat(foundHanako.getId(), is(1L));
		assertThat(foundHanako.getLastName(), is("山田"));
		assertThat(foundHanako.getFirstName(), is("花子"));
		assertThat(foundHanako.getNumberOfDependents(), is(0L));

		// 更新
		unitOfWork = new UnitOfWork<Person>();
		taro = new Person(0, "山田", "太郎", 5);
		hanako = new Person(1, "山田", "花子", 5);
		unitOfWork.resisterDirty(taro);
		unitOfWork.resisterDirty(hanako);

		unitOfWork.commit();

		mapper = new PersonMapper();
		foundTaro = mapper.find(0);
		assertThat(foundTaro.getNumberOfDependents(), is(5L));

		foundHanako = mapper.find(1);
		assertThat(foundHanako.getNumberOfDependents(), is(5L));

		// 削除
		unitOfWork = new UnitOfWork<Person>();
		taro = new Person(0, "山田", "太郎", 5);
		hanako = new Person(1, "山田", "花子", 5);
		unitOfWork.resisterRemoved(taro);
		unitOfWork.resisterRemoved(hanako);

		unitOfWork.commit();

		mapper = new PersonMapper();
		List<Person> persons = mapper.findByLastName("山田");
		assertThat(persons.size(), is(0));
	}

	@Test
	public void 複合操作() throws Exception {
		// 登録＆削除
		UnitOfWork<Person> unitOfWork = new UnitOfWork<Person>();
		Person taro = new Person(0, "山田", "太郎", 1);
		Person hanako = new Person(1, "山田", "花子", 0);
		unitOfWork.resisterNew(taro);
		unitOfWork.resisterNew(hanako);
		unitOfWork.resisterRemoved(hanako);// insert cancel

		unitOfWork.commit();

		PersonMapper mapper = new PersonMapper();
		List<Person> persons = mapper.findByLastName("山田");
		assertThat(persons.size(), is(1));

		// 更新＆削除
		unitOfWork = new UnitOfWork<Person>();
		taro = new Person(0, "山田", "太郎", 2);
		unitOfWork.resisterDirty(taro);
		unitOfWork.resisterRemoved(taro);// update cancel

		unitOfWork.commit();

		mapper = new PersonMapper();
		persons = mapper.findByLastName("山田");
		assertThat(persons.size(), is(0));
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
