package asakichy.architecture.distribution.data_transfer_object;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AlbumAssemblerTest {
	private AlbumAssembler albumAssembler;

	@Test
	public void DTOでの取得() throws Exception {
		Artist artist = new Artist("Liszt", "indies");
		Album album = new Album("Liszt Best", artist);
		album.addTrack(new Track("Liebestraum", "4:00"));
		album.addTrack(new Track("Hungarian Rhapsody", "2:40"));

		AlbumDTO albumDTO = albumAssembler.writeDTO(album);
		assertThat(albumDTO.getTitle(), is("Liszt Best"));
		assertThat(albumDTO.getArtist(), is("Liszt"));

		TrackDTO[] tracks = albumDTO.getTracks();
		assertThat(tracks[0].getTitle(), is("Liebestraum"));
		assertThat(tracks[0].getTime(), is("4:00"));
		assertThat(tracks[1].getTitle(), is("Hungarian Rhapsody"));
		assertThat(tracks[1].getTime(), is("2:40"));
	}

	@Test
	public void DTOでの登録() throws Exception {
		Artist artist = new Artist("Liszt", "indies");
		Resistry.insertArtist(artist);
		AlbumDTO albumDTO = new AlbumDTO();
		albumDTO.setArtist("Liszt");
		albumDTO.setTitle("Liszt Best");
		TrackDTO[] tracks = new TrackDTO[2];
		tracks[0] = new TrackDTO();
		tracks[0].setTitle("Liebestraum");
		tracks[0].setTime("4:00");
		tracks[1] = new TrackDTO();
		tracks[1].setTitle("Hungarian Rhapsody");
		tracks[1].setTime("2:40");
		albumDTO.setTracks(tracks);

		albumAssembler.createAlbum("1", albumDTO);

		Album album = Resistry.findAlbum("1");
		assertThat(album.getTitle(), is("Liszt Best"));
		assertThat(album.getArtist().getName(), is("Liszt"));
		assertThat(album.getArtist().getLabel(), is("indies"));
		List<Track> trackList = album.getTrackList();
		Track track1 = trackList.get(0);
		assertThat(track1.getTitle(), is("Liebestraum"));
		assertThat(track1.getTime(), is("4:00"));
		Track track2 = trackList.get(1);
		assertThat(track2.getTitle(), is("Hungarian Rhapsody"));
		assertThat(track2.getTime(), is("2:40"));
	}

	@Test
	public void DTOでの更新() throws Exception {
		Artist artist = new Artist("Liszt", "indies");
		Resistry.insertArtist(artist);
		artist = new Artist("Liszt Fake", "indies");
		Resistry.insertArtist(artist);
		Album album = new Album("Liszt Best", artist);
		album.addTrack(new Track("Liebestraum", "4:00"));
		album.addTrack(new Track("Hungarian Rhapsody", "2:40"));
		Resistry.insertAlbum("1", album);

		AlbumDTO albumDTO = new AlbumDTO();
		albumDTO.setArtist("Liszt Fake");
		albumDTO.setTitle("Liszt Worst");
		TrackDTO[] tracks = new TrackDTO[2];
		tracks[0] = new TrackDTO();
		tracks[0].setTitle("Liebestraum Fake");
		tracks[0].setTime("4:40");
		tracks[1] = new TrackDTO();
		tracks[1].setTitle("Hungarian Rhapsody Fake");
		tracks[1].setTime("3:40");
		albumDTO.setTracks(tracks);

		albumAssembler.updataAlbum("1", albumDTO);

		album = Resistry.findAlbum("1");
		assertThat(album.getTitle(), is("Liszt Worst"));
		assertThat(album.getArtist().getName(), is("Liszt Fake"));

		List<Track> trackList = album.getTrackList();
		Track track1 = trackList.get(0);
		assertThat(track1.getTitle(), is("Liebestraum Fake"));
		assertThat(track1.getTime(), is("4:40"));
		Track track2 = trackList.get(1);
		assertThat(track2.getTitle(), is("Hungarian Rhapsody Fake"));
		assertThat(track2.getTime(), is("3:40"));
	}

	@Before
	public void 準備() {
		albumAssembler = new AlbumAssembler();
		Resistry.clearAlbum();
		Resistry.clearArtist();
	}

}
