package asakichy.architecture.distribution.data_transfer_object;

import java.util.ArrayList;

import asakichy.architecture.distribution.AppRuntimeException;

/**
 * Albumアセンブラ.
 */

public class AlbumAssembler {

	public AlbumDTO writeDTO(Album album) {
		AlbumDTO albumDTO = new AlbumDTO();
		albumDTO.setTitle(album.getTitle());
		albumDTO.setArtist(album.getArtist().getName());
		writeTracks(albumDTO, album);
		return albumDTO;
	}

	private void writeTracks(AlbumDTO albumDTO, Album album) {
		ArrayList<TrackDTO> tracks = new ArrayList<TrackDTO>();
		for (Track track : album.getTrackList()) {
			TrackDTO trackDTO = new TrackDTO();
			trackDTO.setTitle(track.getTitle());
			trackDTO.setTime(track.getTime());
			tracks.add(trackDTO);
		}
		albumDTO.setTracks(tracks.toArray(new TrackDTO[0]));
	}

	public void createAlbum(String id, AlbumDTO albumDTO) {
		Artist artist = Resistry.findArtistByName(albumDTO.getArtist());
		if (artist == null) {
			throw new AppRuntimeException("artist not found.");
		}
		Album album = new Album(albumDTO.getTitle(), artist);
		createTracks(albumDTO.getTracks(), album);
		Resistry.insertAlbum(id, album);
	}

	private void createTracks(TrackDTO[] tracks, Album album) {
		for (int i = 0; i < tracks.length; i++) {
			TrackDTO trackDTO = tracks[i];
			Track track = new Track(trackDTO.getTitle(), trackDTO.getTime());
			album.addTrack(track);
		}
	}

	public void updataAlbum(String id, AlbumDTO albumDTO) {
		Album album = Resistry.findAlbum(id);
		if (album == null) {
			throw new AppRuntimeException("album not found.");
		}
		if (!album.getTitle().equals(albumDTO.getTitle())) {
			album.setTitle(albumDTO.getTitle());
		}
		if (!album.getArtist().getName().equals(albumDTO.getArtist())) {
			Artist artist = Resistry.findArtistByName(albumDTO.getArtist());
			if (artist == null) {
				throw new AppRuntimeException("artist not found.");
			}
			album.getArtist().setName(albumDTO.getArtist());
		}
		updateTracks(albumDTO.getTracks(), album);

	}

	private void updateTracks(TrackDTO[] tracks, Album album) {
		album.clearTrack();
		for (int i = 0; i < tracks.length; i++) {
			TrackDTO trackDTO = tracks[i];
			Track track = new Track(trackDTO.getTitle(), trackDTO.getTime());
			album.addTrack(track);
		}
	}
}
