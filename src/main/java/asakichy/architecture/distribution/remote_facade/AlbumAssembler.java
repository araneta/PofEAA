package asakichy.architecture.distribution.remote_facade;

/**
 * Albumアセンブラ.
 */

public class AlbumAssembler {

	public AlbumDTO writeDTO(Album album) {
		return new AlbumDTO(album.getTitle(), album.getArtist().getName());
	}

	public void createAlbum(String id, AlbumDTO albumDTO) {
		Artist artist = new Artist(albumDTO.getArtist(), "indies");
		Resistry.insertAlbum(id, new Album(albumDTO.getTitle(), artist));
	}

	public void updataAlbum(String id, AlbumDTO albumDTO) {
		Album album = Resistry.findAlbum(id);
		album.setTitle(albumDTO.getTitle());
		Artist artist = album.getArtist();
		artist.setName(albumDTO.getArtist());
	}
}
