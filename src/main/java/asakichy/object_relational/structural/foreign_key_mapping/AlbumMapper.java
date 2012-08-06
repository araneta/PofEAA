package asakichy.object_relational.structural.foreign_key_mapping;

import static asakichy.object_relational.structural.DB.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import asakichy.object_relational.structural.AppRuntimeException;

/**
 * テーブル「albums」のデータマッパー.
 * 
 * Artist情報に関して、外部キーマッピングを使用しています.
 */

public class AlbumMapper {
	private static final String STATEMENT_FIND = "SELECT * FROM albums WHERE id = ?";
	private static final String STATEMENT_INSERT = "INSERT INTO albums VALUES ( ?, ?, ? )";
	private static final String STATEMENT_UPDATE = "UPDATE albums SET title = ?, artist_id = ? WHERE id = ?";
	private static final String STATEMENT_DELETE = "DELETE FROM albums WHERE id = ?";
	public static AlbumMapper instance = new AlbumMapper();

	private AlbumMapper() {
	}

	private Map<Long, Album> loadedMap = new HashMap<Long, Album>();

	public Album find(long id) {
		Album album = loadedMap.get(id);
		if (album != null) {
			return album;
		}

		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_FIND);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			String title = rs.getString(2);

			long artistId = rs.getLong(3);
			Artist artist = ArtistMapper.instance.find(artistId);

			album = new Album(id, title, artist);
			loadedMap.put(id, album);
			return album;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void insert(Album album) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_INSERT);
			pstmt.setLong(1, album.getId());
			pstmt.setString(2, album.getTitle());
			pstmt.setLong(3, album.getArtist().getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void update(Album album) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_UPDATE);
			pstmt.setString(1, album.getTitle());
			pstmt.setLong(2, album.getArtist().getId());
			pstmt.setLong(3, album.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void delete(Album album) {
		try {
			PreparedStatement stmt = prepareStatement(STATEMENT_DELETE);
			stmt.setLong(1, album.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

}
