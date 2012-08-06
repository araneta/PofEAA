package asakichy.offline_concurrency.coarse_grained_lock;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import asakichy.offline_concurrency.AppRuntimeException;
import asakichy.offline_concurrency.ConcurrencyRuntimeException;
import asakichy.offline_concurrency.DB;

/**
 * 軽オフラインロック用バージョン管理クラス.
 */

public class Version {
	private static final String STATEMENT_FIND = "SELECT * FROM versions WHERE id = ?";
	private static final String STATEMENT_INSERT = "INSERT INTO versions VALUES ( ?, ?, ?, ? )";
	private static final String STATEMENT_UPDATE = "UPDATE versions SET  value = ?, modified_by = ?, modified = ? "
			+ "WHERE id = ? and value = ?";
	private static final String STATEMENT_DELETE = "DELETE FROM versions WHERE id = ?";

	private static KeyGenerator keyGenerator = new KeyGenerator("versions", 5);

		public static Version find(long id) {
		try {
			PreparedStatement pstmt = DB.prepareStatement(STATEMENT_FIND);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				long value = rs.getLong("value");
				String modifiedBy = rs.getString("modified_by");
				Timestamp modified = rs.getTimestamp("modified");
				Version version = new Version(id, value, modifiedBy, modified);
				return version;
			} else {
				throw new ConcurrencyRuntimeException("version not found.");
			}
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}

	}

	public static Version create(String modifier) {
		return new Version(keyGenerator.nextKey(), 1, modifier, new Timestamp(System.currentTimeMillis()));
	}

	private long id;
	private long value;
	private String modifiedBy;
	private Timestamp modified;

	private Version(long id, long value, String modifiedBy, Timestamp modified) {
		this.id = id;
		this.value = value;
		this.modifiedBy = modifiedBy;
		this.modified = modified;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModified() {
		return modified;
	}

	public void setModified(Timestamp modified) {
		this.modified = modified;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public void insert() {
		try {
			PreparedStatement pstmt = DB.prepareStatement(STATEMENT_INSERT);
			pstmt.setLong(1, id);
			pstmt.setLong(2, value);
			pstmt.setString(3, modifiedBy);
			pstmt.setTimestamp(4, modified);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void increment() {
		try {
			PreparedStatement pstmt = DB.prepareStatement(STATEMENT_UPDATE);
			pstmt.setLong(1, value + 1);
			pstmt.setString(2, modifiedBy);
			pstmt.setTimestamp(3, modified);
			pstmt.setLong(4, id);
			pstmt.setLong(5, value);
			int updatedCount = pstmt.executeUpdate();
			if (updatedCount == 0) {
				Version version = find(id);
				throw new ConcurrencyRuntimeException("Version modified by " + version.modifiedBy);
			}
			value++;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void delete() {
		try {
			PreparedStatement pstmt = DB.prepareStatement(STATEMENT_DELETE);
			pstmt.setLong(1, id);
			int deletedCount = pstmt.executeUpdate();
			if (deletedCount == 0) {
				throw new ConcurrencyRuntimeException("Version deleted");
			}
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

}
