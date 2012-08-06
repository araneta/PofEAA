package asakichy.architecture.distribution.remote_facade;

import java.util.HashMap;
import java.util.Map;

public class Resistry {

	private static Map<String , Album> albums = new HashMap<String , Album>();

	public static Album findAlbum(String id) {
		return albums.get(id);
	}
	
	public static void insertAlbum(String id, Album album) {
		albums.put(id, album);
	}

	public static void clearAlbum() {
		albums.clear();
	}
}
