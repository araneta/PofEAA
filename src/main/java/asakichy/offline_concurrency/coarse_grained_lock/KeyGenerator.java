package asakichy.offline_concurrency.coarse_grained_lock;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import asakichy.offline_concurrency.AppRuntimeException;
import asakichy.offline_concurrency.DB;

/**
 * 一意フィールドを手動生成します.
 */

public class KeyGenerator {
	private String name;
	private long nextId;
	private long maxId;
	private long incrementBy;

	public KeyGenerator(String name, long incrementBy) {
		this.name = name;
		this.incrementBy = incrementBy;
		nextId = maxId = 0;
	}

	public synchronized long nextKey() {
		if (nextId == maxId) {
			reserveIds();
		}
		return nextId++;
	}

	private void reserveIds() {
		long newNextId;
		try {
			PreparedStatement pstmt = DB.prepareStatement("SELECT next_id FROM keys WHERE name = ? FOR UPDATE");
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			newNextId = rs.getLong(1);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}

		long newMaxId = newNextId + incrementBy;
		try {
			PreparedStatement pstmt = DB.prepareStatement("UPDATE keys SET next_id = ? WHERE name = ?");
			pstmt.setLong(1, newMaxId);
			pstmt.setString(2, name);
			pstmt.executeUpdate();
			nextId = newNextId;
			maxId = newMaxId;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}

	}

}
