package asakichy.architecture.distribution.remote_facade;

import java.rmi.RemoteException;

/**
 * Albumリモートファサード.
 */

public class AlbumServiceBean implements AlbumService {

	@Override
	public AlbumDTO getAlbum(String id) throws RemoteException {
		AlbumAssembler albumAssembler = new AlbumAssembler();
		Album album = Resistry.findAlbum(id);
		AlbumDTO albumDTO = albumAssembler.writeDTO(album);
		return albumDTO;
	}

	@Override
	public void createAlbum(String id, AlbumDTO albumDTO) throws RemoteException {
		AlbumAssembler albumAssembler = new AlbumAssembler();
		albumAssembler.createAlbum(id, albumDTO);
	}

	@Override
	public void updateAlbum(String id, AlbumDTO albumDTO) throws RemoteException {
		AlbumAssembler albumAssembler = new AlbumAssembler();
		albumAssembler.updataAlbum(id, albumDTO);

	}

}
