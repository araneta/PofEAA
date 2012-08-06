package asakichy.architecture.distribution.remote_facade;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AlbumServiceBeanTest {
	private AlbumService albumService;

	@Test
	public void アルバム登録() throws Exception {
		albumService.createAlbum("1", new AlbumDTO("Liszt Best", "Liszt"));
		Album album = Resistry.findAlbum("1");
		assertThat(album.getTitle(), is("Liszt Best"));
		assertThat(album.getArtist().getName(), is("Liszt"));
	}

	@Test
	public void アルバム取得() throws Exception {
		albumService.createAlbum("1", new AlbumDTO("Liszt Best", "Liszt"));
		AlbumDTO albumDTO = albumService.getAlbum("1");
		assertThat(albumDTO.getTitle(), is("Liszt Best"));
		assertThat(albumDTO.getArtist(), is("Liszt"));
	}

	@Test
	public void アルバム更新() throws Exception {
		albumService.createAlbum("1", new AlbumDTO("Liszt Best", "Liszt"));
		albumService.updateAlbum("1", new AlbumDTO("Liszt Worst", "Liszt Fake"));
		AlbumDTO albumDTO = albumService.getAlbum("1");
		assertThat(albumDTO.getTitle(), is("Liszt Worst"));
		assertThat(albumDTO.getArtist(), is("Liszt Fake"));
	}

	@Before
	public void 準備() {
		Resistry.clearAlbum();
		albumService = new AlbumServiceBean();
	}

}
