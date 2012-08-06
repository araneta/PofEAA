package asakichy.object_relational.structural.inheritance_mappers.concrete_table_inheritance;

import static asakichy.object_relational.structural.DB.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import asakichy.object_relational.structural.inheritance_mappers.concrete_table_inheritance.Player.TYPE;

public class PlayerMapperTest {
	@Test
	public void データ検索() throws Exception {
		PlayerMapper mapper = new PlayerMapper();

		Player player = mapper.find(0);
		FootBaller footballer = (FootBaller) player;
		assertThat(footballer.getId(), is(0L));
		assertThat(footballer.getType(), is(TYPE.FOOTBALL));
		assertThat(footballer.getName(), is("山田太郎"));
		assertThat(footballer.getClub(), is("インテル"));

		player = mapper.find(1);
		Cricketer criketer = (Cricketer) player;
		assertThat(criketer.getId(), is(1L));
		assertThat(criketer.getType(), is(TYPE.CRICKET));
		assertThat(criketer.getName(), is("山田花子"));
		assertThat(criketer.getBattingAverage(), is(500L));

		player = mapper.find(2);
		Bowler bowler = (Bowler) player;
		assertThat(bowler.getId(), is(2L));
		assertThat(bowler.getType(), is(TYPE.BOWLING));
		assertThat(bowler.getName(), is("山田次郎"));
		assertThat(bowler.getBattingAverage(), is(300L));
		assertThat(bowler.getBowlingAverage(), is(200L));

		player = mapper.find(3);
		assertThat(player, nullValue());
	}

	@Test
	public void データ更新() throws Exception {
		PlayerMapper mapper = new PlayerMapper();

		FootBaller footBaller = new FootBaller(0);
		footBaller.setName("山田太郎丸");
		footBaller.setClub("マンＵ");
		mapper.update(footBaller);

		Player player = mapper.find(0);
		footBaller = (FootBaller) player;
		assertThat(footBaller.getId(), is(0L));
		assertThat(footBaller.getType(), is(TYPE.FOOTBALL));
		assertThat(footBaller.getName(), is("山田太郎丸"));
		assertThat(footBaller.getClub(), is("マンＵ"));

		Cricketer cricketer = new Cricketer(1);
		cricketer.setName("山田花");
		cricketer.setBattingAverage(250);
		mapper.update(cricketer);

		player = mapper.find(1);
		cricketer = (Cricketer) player;
		assertThat(cricketer.getId(), is(1L));
		assertThat(cricketer.getType(), is(TYPE.CRICKET));
		assertThat(cricketer.getName(), is("山田花"));
		assertThat(cricketer.getBattingAverage(), is(250L));

		Bowler bowler = new Bowler(2);
		bowler.setName("山田次郎丸");
		bowler.setBattingAverage(150);
		bowler.setBowlingAverage(100);
		mapper.update(bowler);

		player = mapper.find(2);
		bowler = (Bowler) player;
		assertThat(bowler.getId(), is(2L));
		assertThat(bowler.getType(), is(TYPE.BOWLING));
		assertThat(bowler.getName(), is("山田次郎丸"));
		assertThat(bowler.getBattingAverage(), is(150L));
		assertThat(bowler.getBowlingAverage(), is(100L));
	}

	@Test
	public void データ削除() throws Exception {
		PlayerMapper mapper = new PlayerMapper();
		mapper.delete(new FootBaller(0));
		mapper.delete(new Cricketer(1));
		mapper.delete(new Bowler(2));

		Statement stmt = createStatement();
		ResultSet rs = stmt.executeQuery("SELECT count(*) FROM footballers");
		rs.next();
		int count = rs.getInt(1);
		assertThat(count, is(0));

		stmt = createStatement();
		rs = stmt.executeQuery("SELECT count(*) FROM cricketers");
		rs.next();
		count = rs.getInt(1);
		assertThat(count, is(0));

		stmt = createStatement();
		rs = stmt.executeQuery("SELECT count(*) FROM bowlers");
		rs.next();
		count = rs.getInt(1);
		assertThat(count, is(0));
	}

	@Test
	public void データ追加() throws Exception {
		PlayerMapper mapper = new PlayerMapper();
		FootBaller footBaller = new FootBaller(4);
		footBaller.setName("山田太郎丸");
		footBaller.setClub("マンＵ");
		mapper.insert(footBaller);

		Player player = mapper.find(4);
		footBaller = (FootBaller) player;
		assertThat(footBaller.getId(), is(4L));
		assertThat(footBaller.getType(), is(TYPE.FOOTBALL));
		assertThat(footBaller.getName(), is("山田太郎丸"));
		assertThat(footBaller.getClub(), is("マンＵ"));

		Cricketer cricketer = new Cricketer(5);
		cricketer.setName("山田花");
		cricketer.setBattingAverage(250);
		mapper.insert(cricketer);

		player = mapper.find(5);
		cricketer = (Cricketer) player;
		assertThat(cricketer.getId(), is(5L));
		assertThat(cricketer.getType(), is(TYPE.CRICKET));
		assertThat(cricketer.getName(), is("山田花"));
		assertThat(cricketer.getBattingAverage(), is(250L));

		Bowler bowler = new Bowler(6);
		bowler.setName("山田次郎丸");
		bowler.setBattingAverage(150);
		bowler.setBowlingAverage(100);
		mapper.insert(bowler);

		player = mapper.find(6);
		bowler = (Bowler) player;
		assertThat(bowler.getId(), is(6L));
		assertThat(bowler.getType(), is(TYPE.BOWLING));
		assertThat(bowler.getName(), is("山田次郎丸"));
		assertThat(bowler.getBattingAverage(), is(150L));
		assertThat(bowler.getBowlingAverage(), is(100L));
	}

	@Before
	public void 準備() throws SQLException {
		Statement stmt = createStatement();

		stmt.execute("DROP TABLE IF EXISTS footballers");
		stmt.execute("CREATE TABLE footballers ( id BIGINT PRIMARY KEY, name VARCHAR(32), club VARCHAR(32) )");

		stmt.execute("DROP TABLE IF EXISTS cricketers");
		stmt.execute("CREATE TABLE cricketers ( id BIGINT PRIMARY KEY, name VARCHAR(32), batting_average BIGINT )");

		stmt.execute("DROP TABLE IF EXISTS bowlers");
		stmt.execute("CREATE TABLE bowlers ( id BIGINT PRIMARY KEY, name VARCHAR(32), batting_average BIGINT, bowling_average BIGINT )");

		stmt.execute("INSERT INTO footballers VALUES(0,'山田太郎','インテル')");
		stmt.execute("INSERT INTO cricketers VALUES(1,'山田花子',500)");
		stmt.execute("INSERT INTO bowlers VALUES(2,'山田次郎',300,200)");
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
