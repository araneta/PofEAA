package asakichy.object_relational.structural.dependent_mapping;

import static asakichy.object_relational.structural.DB.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AlbumMapperTest {
	@Test
	public void データ検索() throws Exception {
		Album album = AlbumMapper.instance.find(0);
		assertThat(album.getId(), is(0L));
		assertThat(album.getTitle(), is("Liszt Best"));
		Track track1 = album.getTracks().get(0);
		Track track2 = album.getTracks().get(1);
		assertThat(track1.getId(), is(0L));
		assertThat(track1.getTitle(), is("Liebestraum"));
		assertThat(track1.getTime(), is("4:00"));
		assertThat(track2.getId(), is(1L));
		assertThat(track2.getTitle(), is("Hungarian Rhapsody"));
		assertThat(track2.getTime(), is("2:40"));

		Album album_cashed = AlbumMapper.instance.find(0);
		assertThat(album_cashed, sameInstance(album));
	}

	@Test
	public void データ登録() throws Exception {
		Track track1 = new Track(0, "Liebestraum Fake", "5:00");
		Track track2 = new Track(1, "Hungarian Rhapsody Fake", "4:40");
		List<Track> tracks = Arrays.asList(new Track[] { track1, track2 });
		Album album = new Album(1, "Liszt Best Fake", tracks);
		AlbumMapper.instance.insert(album);

		Album foundAlbum = AlbumMapper.instance.find(1);
		assertThat(foundAlbum.getId(), is(1L));
		assertThat(foundAlbum.getTitle(), is("Liszt Best Fake"));
		Track foundTrack1 = foundAlbum.getTracks().get(0);
		Track foundTrack2 = foundAlbum.getTracks().get(1);
		assertThat(foundTrack1.getId(), is(0L));
		assertThat(foundTrack1.getTitle(), is("Liebestraum Fake"));
		assertThat(foundTrack1.getTime(), is("5:00"));
		assertThat(foundTrack2.getId(), is(1L));
		assertThat(foundTrack2.getTitle(), is("Hungarian Rhapsody Fake"));
		assertThat(foundTrack2.getTime(), is("4:40"));

		Album album_cashed = AlbumMapper.instance.find(1);
		assertThat(album_cashed, sameInstance(album));
	}

	@Test
	public void データ削除() throws Exception {
		Album album = new Album(0);
		AlbumMapper.instance.delete(album);

		Statement stmt = createStatement();
		ResultSet rs = stmt.executeQuery("SELECT count(*) FROM albums");
		rs.next();
		int count = rs.getInt(1);
		assertThat(count, is(0));

		rs = stmt.executeQuery("SELECT count(*) FROM tracks");
		rs.next();
		count = rs.getInt(1);
		assertThat(count, is(0));
	}

	@Test
	public void データ更新() throws Exception {
		Track updatedTrack1 = new Track(0, "Liebestraum Fake", "5:00");
		Track updatedTrack2 = new Track(1, "Hungarian Rhapsody Fake", "4:40");
		List<Track> updatedTracks = Arrays.asList(new Track[] { updatedTrack1, updatedTrack2 });
		Album updatedAlbum = new Album(0, "Liszt Best Fake", updatedTracks);
		AlbumMapper.instance.update(updatedAlbum);

		// キャッシュの確認
		Album album_cashed = AlbumMapper.instance.find(0);
		assertThat(album_cashed, sameInstance(updatedAlbum));

		// DB実データの確認
		AlbumMapper.instance.refresh();
		Album foundAlbum = AlbumMapper.instance.find(0);

		assertThat(foundAlbum.getId(), is(0L));
		assertThat(foundAlbum.getTitle(), is("Liszt Best Fake"));
		Track foundTrack1 = foundAlbum.getTracks().get(0);
		Track foundTrack2 = foundAlbum.getTracks().get(1);
		assertThat(foundTrack1.getId(), is(0L));
		assertThat(foundTrack1.getTitle(), is("Liebestraum Fake"));
		assertThat(foundTrack1.getTime(), is("5:00"));
		assertThat(foundTrack2.getId(), is(1L));
		assertThat(foundTrack2.getTitle(), is("Hungarian Rhapsody Fake"));
		assertThat(foundTrack2.getTime(), is("4:40"));
	}

	@Before
	public void 準備() throws SQLException {
		Statement stmt = createStatement();
		stmt.execute("DROP TABLE IF EXISTS albums");
		stmt.execute("CREATE TABLE albums ( id BIGINT PRIMARY KEY, title VARCHAR(32) )");

		stmt.execute("DROP TABLE IF EXISTS tracks");
		stmt.execute("CREATE TABLE tracks ( album_id BIGINT REFERENCES albums(id) ON DELETE CASCADE, "
				+ "id BIGINT, title VARCHAR(32), time VARCHAR(32), PRIMARY KEY(album_id, id) )");

		stmt.execute("INSERT INTO albums VALUES(0,'Liszt Best')");
		stmt.execute("INSERT INTO tracks VALUES(0,0,'Liebestraum','4:00')");
		stmt.execute("INSERT INTO tracks VALUES(0,1,'Hungarian Rhapsody','2:40')");
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
