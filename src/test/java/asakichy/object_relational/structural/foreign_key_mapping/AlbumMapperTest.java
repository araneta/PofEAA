package asakichy.object_relational.structural.foreign_key_mapping;

import static asakichy.object_relational.structural.DB.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AlbumMapperTest {

	@Test
	public void データ操作() throws Exception {
		// 登録
		Artist artist = new Artist(0, "Liszt", "indies");
		ArtistMapper.instance.insert(artist);
		Album album = new Album(0, "Liszt Best", artist);
		AlbumMapper.instance.insert(album);

		// 取得
		Album foundAlbum = AlbumMapper.instance.find(0);
		assertThat(foundAlbum.getTitle(), is("Liszt Best"));
		assertThat(foundAlbum.getArtist().getName(), is("Liszt"));
		assertThat(foundAlbum.getArtist().getLabel(), is("indies"));
	}

	@Before
	public void 準備() throws SQLException {
		Statement stmt = createStatement();
		stmt.execute("DROP TABLE IF EXISTS artists");
		stmt.execute("CREATE TABLE artists ( id BIGINT PRIMARY KEY, name VARCHAR(32), label VARCHAR(32) )");

		stmt.execute("DROP TABLE IF EXISTS albums");
		stmt.execute("CREATE TABLE albums ( id BIGINT PRIMARY KEY, title VARCHAR(32), artist_id BIGINT REFERENCES artists(id))");
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
