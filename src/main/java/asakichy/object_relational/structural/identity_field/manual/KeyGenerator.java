package asakichy.object_relational.structural.identity_field.manual;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import asakichy.object_relational.structural.AppRuntimeException;

/**
 * 一意フィールドを手動生成します.
 */

public class KeyGenerator {
	private Connection conn;
	private String name;
	private long nextId;
	private long maxId;
	private long incrementBy;

	public KeyGenerator(Connection conn, String name, long incrementBy) {
		this.conn = conn;
		this.name = name;
		this.incrementBy = incrementBy;
		nextId = maxId = 0;
		try {
			this.conn.setAutoCommit(false);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
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
			PreparedStatement pstmt = conn.prepareStatement("SELECT next_id FROM keys WHERE name = ? FOR UPDATE");
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			newNextId = rs.getLong(1);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}

		long newMaxId = newNextId + incrementBy;
		try {
			PreparedStatement pstmt = conn.prepareStatement("UPDATE keys SET next_id = ? WHERE name = ?");
			pstmt.setLong(1, newMaxId);
			pstmt.setString(2, name);
			pstmt.executeUpdate();
			conn.commit();
			nextId = newNextId;
			maxId = newMaxId;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}

	}

}
