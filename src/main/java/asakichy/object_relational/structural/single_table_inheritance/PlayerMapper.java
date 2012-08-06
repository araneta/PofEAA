package asakichy.object_relational.structural.single_table_inheritance;

import static asakichy.object_relational.structural.DB.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import asakichy.object_relational.structural.AppRuntimeException;
import asakichy.object_relational.structural.single_table_inheritance.Player.TYPE;

/**
 * テーブル「players」のデータマッパー.
 * 
 * シングルテーブル継承を使用しています.
 */

public class PlayerMapper {

	private static final String STATEMENT_FIND = "SELECT * FROM players WHERE id = ?";
	private static final String STATEMENT_UPDATE = "UPDATE players SET name = ?, type = ?, club = ?, "
			+ "batting_average = ?, bowling_average = ? WHERE id = ?";
	private static final String STATEMENT_DELETE = "DELETE FROM players WHERE id = ?";
	private static final String STATEMENT_INSERT = "INSERT INTO players VALUES(?, ?, ?, ?, ?, ?)";

	public Player find(long id) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_FIND);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			return fillObject(rs);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	private Player fillObject(ResultSet rs) {
		try {
			long id = rs.getLong("id");
			String typeString = rs.getString("type");
			String name = rs.getString("name");
			String club = rs.getString("club");
			long battingAverage = rs.getLong("batting_average");
			long bowlingAverage = rs.getLong("bowling_average");

			TYPE type = TYPE.valueOf(typeString);
			switch (type) {
			case FOOTBALL:
				FootBaller footBaller = new FootBaller();
				footBaller.setId(id);
				footBaller.setName(name);
				footBaller.setClub(club);
				return footBaller;
			case CRICKET:
				Cricketer criketer = new Cricketer();
				criketer.setId(id);
				criketer.setName(name);
				criketer.setBattingAverage(battingAverage);
				return criketer;
			case BOWLING:
				Bowler bowler = new Bowler();
				bowler.setId(id);
				bowler.setName(name);
				bowler.setBattingAverage(battingAverage);
				bowler.setBowlingAverage(bowlingAverage);
				return bowler;
			default:
				throw new AppRuntimeException("unknown type");
			}
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void insert(Player player) {
		try {
			PreparedStatement stmt = prepareStatement(STATEMENT_INSERT);
			int index = 0;
			stmt.setLong(++index, player.getId());
			fillStatement(player, stmt, index);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void update(Player player) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_UPDATE);
			int index = 0;
			index = fillStatement(player, pstmt, index);
			pstmt.setLong(++index, player.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	private int fillStatement(Player player, PreparedStatement stmt, int index) {
		try {
			stmt.setString(++index, player.getName());
			switch (player.getType()) {
			case FOOTBALL:
				FootBaller footBaller = (FootBaller) player;
				stmt.setString(++index, footBaller.getType().toString());
				stmt.setString(++index, footBaller.getClub());
				stmt.setLong(++index, 0);
				stmt.setLong(++index, 0);
				break;
			case CRICKET:
				Cricketer criketer = (Cricketer) player;
				stmt.setString(++index, criketer.getType().toString());
				stmt.setString(++index, null);
				stmt.setLong(++index, criketer.getBattingAverage());
				stmt.setLong(++index, 0);
				break;
			case BOWLING:
				Bowler bowler = (Bowler) player;
				stmt.setString(++index, bowler.getType().toString());
				stmt.setString(++index, null);
				stmt.setLong(++index, bowler.getBattingAverage());
				stmt.setLong(++index, bowler.getBowlingAverage());
				break;
			default:
				throw new AppRuntimeException("unknown type");
			}
			return index;
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void delete(Player player) {
		try {
			PreparedStatement pstmt = prepareStatement(STATEMENT_DELETE);
			pstmt.setLong(1, player.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

}
