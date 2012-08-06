package asakichy.architecture.distribution.remote_facade;

import java.rmi.RemoteException;

/**
 * Albumリモートファサード・インターフェイス.
 */

public interface AlbumService {
	AlbumDTO getAlbum(String id) throws RemoteException;

	void createAlbum(String id, AlbumDTO albumDTO) throws RemoteException;

	void updateAlbum(String id, AlbumDTO albumDTO) throws RemoteException;

}
