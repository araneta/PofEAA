package asakichy.object_relational.structural.concrete_table_inheritance;

import static asakichy.object_relational.structural.DB.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import asakichy.object_relational.structural.AppRuntimeException;
import asakichy.object_relational.structural.NotFoundRuntimeException;

/**
 * テーブル「footballers」「cricketers」「bowlers」のデータマッパー.
 * 
 * 具象テーブル継承を使用しています.
 */

public class PlayerMapper {
	private static final String STATEMENT_FIND = "SELECT * FROM %s WHERE id = ?";
	private static final String STATEMENT_UPDATE = "UPDATE %s SET %s WHERE id = ?";
	private static final String STATEMENT_DELETE = "DELETE FROM %s WHERE id = ?";
	private static final String STATEMENT_INSERT = "INSERT INTO %s VALUES(%s)";

	public Player find(long id) {
		try {
			try {
				ResultSet footBallRs = findRow(id, "footballers");
				String name = footBallRs.getString("name");
				String club = footBallRs.getString("club");
				FootBaller footBaller = new FootBaller();
				footBaller.setId(id);
				footBaller.setName(name);
				footBaller.setClub(club);
				return footBaller;
			} catch (NotFoundRuntimeException continuance) {
			}

			try {
				ResultSet cricketRs = findRow(id, "cricketers");
				String name = cricketRs.getString("name");
				long battingAverage = cricketRs.getLong("batting_average");
				Cricketer criketer = new Cricketer();
				criketer.setId(id);
				criketer.setName(name);
				criketer.setBattingAverage(battingAverage);
				return criketer;
			} catch (NotFoundRuntimeException continuance) {
			}

			try {
				ResultSet bowlingRs = findRow(id, "bowlers");
				String name = bowlingRs.getString("name");
				long battingAverage = bowlingRs.getLong("batting_average");
				long bowlingAverage = bowlingRs.getLong("bowling_average");
				Bowler bowler = new Bowler();
				bowler.setId(id);
				bowler.setName(name);
				bowler.setBattingAverage(battingAverage);
				bowler.setBowlingAverage(bowlingAverage);
				return bowler;
			} catch (NotFoundRuntimeException continuance) {
			}
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
		return null;
	}

	private ResultSet findRow(long id, String table) {
		try {
			String stmtFind = String.format(STATEMENT_FIND, table);
			PreparedStatement pstmt = prepareStatement(stmtFind);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			boolean exist = rs.next();
			if (exist == false) {
				throw new NotFoundRuntimeException();
			}
			return rs;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void update(Player player) {
		switch (player.getType()) {
		case FOOTBALL:
			updateFootBaller((FootBaller) player);
			break;
		case CRICKET:
			updateCricketer((Cricketer) player);
			break;
		case BOWLING:
			updateBowler((Bowler) player);
			break;
		default:
			throw new AppRuntimeException("unknown type");
		}
	}

	private void updateFootBaller(FootBaller footBaller) {
		try {
			String stmtUpdate = String.format(STATEMENT_UPDATE, "footballers", "name=?,club=?");
			PreparedStatement stmt = prepareStatement(stmtUpdate);
			stmt.setString(1, footBaller.getName());
			stmt.setString(2, footBaller.getClub());
			stmt.setLong(3, footBaller.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	private void updateCricketer(Cricketer cricketer) {
		try {
			String stmtUpdate = String.format(STATEMENT_UPDATE, "cricketers", "name=?,batting_average=?");
			PreparedStatement stmt = prepareStatement(stmtUpdate);
			stmt.setString(1, cricketer.getName());
			stmt.setLong(2, cricketer.getBattingAverage());
			stmt.setLong(3, cricketer.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	private void updateBowler(Bowler bowler) {
		try {
			String stmtUpdate = String.format(STATEMENT_UPDATE, "bowlers", "name=?,batting_average=?,bowling_average=?");
			PreparedStatement stmt = prepareStatement(stmtUpdate);
			stmt.setString(1, bowler.getName());
			stmt.setLong(2, bowler.getBattingAverage());
			stmt.setLong(3, bowler.getBowlingAverage());
			stmt.setLong(4, bowler.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void insert(Player player) {
		switch (player.getType()) {
		case FOOTBALL: {
			insertFootBaller((FootBaller) player);
			break;
		}
		case CRICKET: {
			insertCricketer((Cricketer) player);
			break;
		}
		case BOWLING: {
			insertBowler((Bowler) player);
			break;
		}
		default:
			throw new AppRuntimeException("unknown type");
		}
	}

	private void insertFootBaller(FootBaller footBaller) {
		try {
			String stmtInsert = String.format(STATEMENT_INSERT, "footballers", "?,?,?");
			PreparedStatement stmt = prepareStatement(stmtInsert);
			stmt.setLong(1, footBaller.getId());
			stmt.setString(2, footBaller.getName());
			stmt.setString(3, footBaller.getClub());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	private void insertCricketer(Cricketer cricketer) {
		try {
			String stmtInsert = String.format(STATEMENT_INSERT, "cricketers", "?,?,?");
			PreparedStatement stmt = prepareStatement(stmtInsert);
			stmt.setLong(1, cricketer.getId());
			stmt.setString(2, cricketer.getName());
			stmt.setLong(3, cricketer.getBattingAverage());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	private void insertBowler(Bowler bowler) {
		try {
			String stmtInsert = String.format(STATEMENT_INSERT, "bowlers", "?,?,?,?");
			PreparedStatement stmt = prepareStatement(stmtInsert);
			stmt.setLong(1, bowler.getId());
			stmt.setString(2, bowler.getName());
			stmt.setLong(3, bowler.getBattingAverage());
			stmt.setLong(4, bowler.getBowlingAverage());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void delete(Player player) {
		switch (player.getType()) {
		case FOOTBALL:
			deleteRow(player, "footballers");
			break;
		case CRICKET:
			deleteRow(player, "cricketers");
			break;
		case BOWLING:
			deleteRow(player, "bowlers");
			break;
		default:
			throw new AppRuntimeException("unknown type");
		}
	}

	private void deleteRow(Player player, String table) {
		try {
			String stmt = String.format(STATEMENT_DELETE, table);
			PreparedStatement pstmt = prepareStatement(stmt);
			pstmt.setLong(1, player.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}

	}

}
