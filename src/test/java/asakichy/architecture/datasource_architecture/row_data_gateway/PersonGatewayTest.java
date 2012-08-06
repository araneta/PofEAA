package asakichy.architecture.datasource_architecture.row_data_gateway;

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

public class PersonGatewayTest {
	@Test
	public void データ操作() throws Exception {
		// 追加
		PersonGateway taro = new PersonGateway(1, "山田", "太郎", 1);
		PersonGateway hanako = new PersonGateway(2, "山田", "花子", 0);
		PersonGateway jiro = new PersonGateway(3, "山田", "次郎", 2);
		taro.insert();
		hanako.insert();
		jiro.insert();

		PersonFinder finder = new PersonFinder();
		PersonGateway foundTaro = finder.find(1);
		assertThat(foundTaro.getId(), is(taro.getId()));
		assertThat(foundTaro.getLastName(), is(taro.getLastName()));
		assertThat(foundTaro.getFirstName(), is(taro.getFirstName()));
		assertThat(foundTaro.getNumberOfDependents(), is(taro.getNumberOfDependents()));
		List<PersonGateway> responsibles = finder.findResponsibles();
		assertThat(responsibles.size(), is(2));

		// 更新
		PersonGateway dependentsHanako = new PersonGateway(2, "山田", "花子", 10);
		dependentsHanako.update();// 花子を扶養持ちへ
		responsibles = finder.findResponsibles();
		assertThat(responsibles.size(), is(3));

		// 削除
		PersonGateway deteledHanako = new PersonGateway(2);
		deteledHanako.delete();
		responsibles = finder.findResponsibles();
		assertThat(responsibles.size(), is(2));

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
