package asakichy.object_relational.structural.class_table_inheritance;

import static asakichy.object_relational.structural.DB.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import asakichy.object_relational.structural.AppRuntimeException;
import asakichy.object_relational.structural.class_table_inheritance.Player.TYPE;

/**
 * テーブル「players」「footballers」「cricketers」「bowlers」のデータマッパー.
 * 
 * クラステーブル継承を使用しています.
 */

public class PlayerMapper {
	private static final String STATEMENT_FIND = "SELECT * FROM %s WHERE id = ?";
	private static final String STATEMENT_UPDATE = "UPDATE %s SET %s WHERE id = ?";
	private static final String STATEMENT_DELETE = "DELETE FROM %s WHERE id = ?";
	private static final String STATEMENT_INSERT = "INSERT INTO %s VALUES(%s)";

	public Player find(long id) {
		try {
			ResultSet playerRs = findRow(id, "players");
			String name = playerRs.getString("name");
			String typeString = playerRs.getString("type");

			TYPE type = TYPE.valueOf(typeString);
			switch (type) {
			case FOOTBALL: {
				ResultSet footBallRs = findRow(id, "footballers");
				FootBaller footBaller = new FootBaller();
				footBaller.setId(id);
				footBaller.setName(name);
				String club = footBallRs.getString("club");
				footBaller.setClub(club);
				return footBaller;
			}
			case CRICKET: {
				ResultSet cricketRs = findRow(id, "cricketers");
				Cricketer criketer = new Cricketer();
				criketer.setId(id);
				criketer.setName(name);
				long battingAverage = cricketRs.getLong("batting_average");
				criketer.setBattingAverage(battingAverage);
				return criketer;
			}
			case BOWLING: {
				ResultSet bowlingRs = findRow(id, "bowlers");
				ResultSet cricketRs = findRow(id, "cricketers");
				Bowler bowler = new Bowler();
				bowler.setId(id);
				bowler.setName(name);
				long battingAverage = cricketRs.getLong("batting_average");
				long bowlingAverage = bowlingRs.getLong("bowling_average");
				bowler.setBattingAverage(battingAverage);
				bowler.setBowlingAverage(bowlingAverage);
				return bowler;
			}
			default:
				throw new AppRuntimeException("unknown type");
			}
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	private ResultSet findRow(long id, String table) {
		try {
			String stmtFind = String.format(STATEMENT_FIND, table);
			PreparedStatement pstmt = prepareStatement(stmtFind);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			return rs;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void update(Player player) {
		switch (player.getType()) {
		case FOOTBALL:
			updatePlayer(player);
			updateFootBaller((FootBaller) player);
			break;
		case CRICKET:
			updatePlayer(player);
			updateCricketer((Cricketer) player);
			break;
		case BOWLING:
			updatePlayer(player);
			updateCricketer((Cricketer) player);
			updateBowler((Bowler) player);
			break;
		default:
			throw new AppRuntimeException("unknown type");
		}
	}

	private void updatePlayer(Player player) {
		try {
			String stmtUpdate = String.format(STATEMENT_UPDATE, "players", "name=?,type=?");
			PreparedStatement stmt = prepareStatement(stmtUpdate);
			stmt.setString(1, player.getName());
			stmt.setString(2, player.getType().toString());
			stmt.setLong(3, player.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	private void updateFootBaller(FootBaller footBaller) {
		try {
			String stmtUpdate = String.format(STATEMENT_UPDATE, "footballers", "club=?");
			PreparedStatement stmt = prepareStatement(stmtUpdate);
			stmt.setString(1, footBaller.getClub());
			stmt.setLong(2, footBaller.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	private void updateCricketer(Cricketer cricketer) {
		try {
			String stmtUpdate = String.format(STATEMENT_UPDATE, "cricketers", "batting_average=?");
			PreparedStatement stmt = prepareStatement(stmtUpdate);
			stmt.setLong(1, cricketer.getBattingAverage());
			stmt.setLong(2, cricketer.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	private void updateBowler(Bowler bowler) {
		try {
			String stmtUpdate = String.format(STATEMENT_UPDATE, "bowlers", "bowling_average=?");
			PreparedStatement stmt = prepareStatement(stmtUpdate);
			stmt.setLong(1, bowler.getBowlingAverage());
			stmt.setLong(2, bowler.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void insert(Player player) {
		switch (player.getType()) {
		case FOOTBALL: {
			insertPlayer(player);
			insertFootBaller((FootBaller) player);
			break;
		}
		case CRICKET: {
			insertPlayer(player);
			insertCricketer((Cricketer) player);
			break;
		}
		case BOWLING: {
			insertPlayer(player);
			insertCricketer((Cricketer) player);
			insertBowler((Bowler) player);
			break;
		}
		default:
			throw new AppRuntimeException("unknown type");
		}
	}

	private void insertPlayer(Player player) {
		try {
			String stmtInsert = String.format(STATEMENT_INSERT, "players", "?,?,?");
			PreparedStatement stmt = prepareStatement(stmtInsert);
			stmt.setLong(1, player.getId());
			stmt.setString(2, player.getName());
			stmt.setString(3, player.getType().toString());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	private void insertFootBaller(FootBaller footBaller) {
		try {
			String stmtInsert = String.format(STATEMENT_INSERT, "footballers", "?,?");
			PreparedStatement stmt = prepareStatement(stmtInsert);
			stmt.setLong(1, footBaller.getId());
			stmt.setString(2, footBaller.getClub());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	private void insertCricketer(Cricketer cricketer) {
		try {
			String stmtInsert = String.format(STATEMENT_INSERT, "cricketers", "?,?");
			PreparedStatement stmt = prepareStatement(stmtInsert);
			stmt.setLong(1, cricketer.getId());
			stmt.setLong(2, cricketer.getBattingAverage());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	private void insertBowler(Bowler bowler) {
		try {
			String stmtInsert = String.format(STATEMENT_INSERT, "bowlers", "?,?");
			PreparedStatement stmt = prepareStatement(stmtInsert);
			stmt.setLong(1, bowler.getId());
			stmt.setLong(2, bowler.getBowlingAverage());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void delete(Player player) {
		deleteRow(player, "players");
		switch (player.getType()) {
		case FOOTBALL:
			deleteRow(player, "footballers");
			break;
		case CRICKET:
			deleteRow(player, "cricketers");
			break;
		case BOWLING:
			deleteRow(player, "cricketers");
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
