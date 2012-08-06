package asakichy.object_relational.structural.dependent_mapping;

import static asakichy.object_relational.structural.DB.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asakichy.object_relational.structural.AppRuntimeException;

/**
 * テーブル「albums」「tracks」のデータマッパー.
 * 
 * 依存マッピングを使用しています.
 */

public class AlbumMapper {
	private static final String STATEMENT_FIND = "SELECT * FROM albums WHERE id = ?";
	private static final String STATEMENT_FIND_TRACKS = "SELECT * FROM tracks WHERE album_id = ? ORDER BY id ASC";
	private static final String STATEMENT_INSERT = "INSERT INTO albums VALUES ( ?, ?)";
	private static final String STATEMENT_INSERT_TRACK = "INSERT INTO tracks VALUES ( ?, ?, ?, ?)";
	private static final String STATEMENT_UPDATE = "UPDATE albums SET title = ? WHERE id = ?";
	private static final String STATEMENT_DELETE = "DELETE FROM albums WHERE id = ?";
	private static final String STATEMENT_DELETE_TRACKS = "DELETE FROM tracks WHERE album_id = ?";
	public static AlbumMapper instance = new AlbumMapper();

	private AlbumMapper() {
	}

	private Map<Long, Album> loaded = new HashMap<Long, Album>();
	
	public void refresh() {
		loaded.clear();
	}

	public Album find(long id) {
		Album album = loaded.get(id);
		if (album != null) {
			return album;
		}

		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_FIND);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			String title = rs.getString(2);
			List<Track> tracks = findTracks(id);
			album = new Album(id, title, tracks);
			loaded.put(id, album);
			return album;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	private List<Track> findTracks(long id) {
		try {
			List<Track> tracks = new ArrayList<Track>();
			PreparedStatement pstmt = prepareStatement(STATEMENT_FIND_TRACKS);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				long track_id = rs.getLong(2);
				String name = rs.getString(3);
				String time = rs.getString(4);
				Track track = new Track(track_id, name, time);
				tracks.add(track);
			}
			return tracks;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}

	}

	public void insert(Album album) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_INSERT);
			pstmt.setLong(1, album.getId());
			pstmt.setString(2, album.getTitle());
			pstmt.executeUpdate();
			insertTracks(album);
			loaded.put(album.getId(), album);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	private void insertTracks(Album album) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_INSERT_TRACK);
			for (Track track : album.getTracks()) {
				pstmt.setLong(1, album.getId());
				pstmt.setLong(2, track.getId());
				pstmt.setString(3, track.getTitle());
				pstmt.setString(4, track.getTime());
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}

	}

	public void update(Album album) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_UPDATE);
			pstmt.setString(1, album.getTitle());
			pstmt.setLong(2, album.getId());
			pstmt.executeUpdate();
			updatetracks(album);
			loaded.put(album.getId(), album);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	private void updatetracks(Album album) {
		deleteTracks(album);
		insertTracks(album);
	}

	public void delete(Album album) {
		try {
			PreparedStatement stmt = prepareStatement(STATEMENT_DELETE);
			stmt.setLong(1, album.getId());
			stmt.executeUpdate();
			// deleteTracks(album);=>on delete cascadeで。
			loaded.remove(album.getId());
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	private void deleteTracks(Album album) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_DELETE_TRACKS);
			pstmt.setLong(1, album.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}

	}

}
