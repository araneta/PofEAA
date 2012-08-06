package asakichy.object_relational.structural.foreign_key_mapping;

import static asakichy.object_relational.structural.DB.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import asakichy.object_relational.structural.AppRuntimeException;

/**
 * テーブル「artists」のデータマッパー.
 */

public class ArtistMapper {
	private static final String STATEMENT_FIND = "SELECT * FROM artists WHERE id = ?";
	private static final String STATEMENT_INSERT = "INSERT INTO artists VALUES ( ?, ?, ? )";
	private static final String STATEMENT_UPDATE = "UPDATE artists SET name = ?, label = ? WHERE id = ?";
	private static final String STATEMENT_DELETE = "DELETE FROM artists WHERE id = ?";
	public static ArtistMapper instance = new ArtistMapper();

	private ArtistMapper() {
	}

	private Map<Long, Artist> loadedMap = new HashMap<Long, Artist>();

	public Artist find(long id) {
		Artist artist = loadedMap.get(id);
		if (artist != null) {
			return artist;
		}

		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_FIND);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			String name = rs.getString(2);
			String label = rs.getString(3);
			artist = new Artist(id, name, label);
			loadedMap.put(id, artist);
			return artist;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void insert(Artist artist) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_INSERT);
			pstmt.setLong(1, artist.getId());
			pstmt.setString(2, artist.getName());
			pstmt.setString(3, artist.getLabel());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void update(Artist artist) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_UPDATE);
			pstmt.setString(1, artist.getName());
			pstmt.setString(2, artist.getLabel());
			pstmt.setLong(3, artist.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void delete(Artist artist) {
		try {
			PreparedStatement stmt = prepareStatement(STATEMENT_DELETE);
			stmt.setLong(1, artist.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

}
