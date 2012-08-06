package asakichy.offline_concurrency.pessimistic_offline_lock;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import asakichy.offline_concurrency.AppRuntimeException;
import asakichy.offline_concurrency.ConcurrencyRuntimeException;
import asakichy.offline_concurrency.DB;

/**
 * 重オフラインロック機能クラス（Exclusive Read Lock）.
 */

public class ExclusiveReadLockManager {

	private static final String SQL_INSERT = "INSERT INTO locks VALUES(?, ?)";
	private static final String SQL_DELETE = "DELETE FROM locks WHERE lockable_id = ? AND owner = ?";
	private static final String SQL_CHECK = "SELECT lockable_id FROM locks WHERE lockable_id = ? AND owner = ?";

	public static final ExclusiveReadLockManager INSTANCE = new ExclusiveReadLockManager();

	private ExclusiveReadLockManager() {
		// singleton
	}

	public void aqcuireLock(long lockable, String owner) {
		if (!hasLock(lockable, owner)) {
			try {
				PreparedStatement pstmt = DB.prepareStatement(SQL_INSERT);
				pstmt.setLong(1, lockable);
				pstmt.setString(2, owner);
				pstmt.executeUpdate();
			} catch (SQLException e) {
				throw new ConcurrencyRuntimeException("unable to lock " + lockable);
			}
		}
	}

	public void releaseLock(long lockable, String owner) {
		try {
			PreparedStatement pstmt = DB.prepareStatement(SQL_DELETE);
			pstmt.setLong(1, lockable);
			pstmt.setString(2, owner);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}

	}

	private boolean hasLock(long lockable, String owner) {
		try {
			PreparedStatement pstmt = DB.prepareStatement(SQL_CHECK);
			pstmt.setLong(1, lockable);
			pstmt.setString(2, owner);
			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

}
