package asakichy.object_relational.structural.inheritance_mappers.single_table_inheritance;

import java.sql.ResultSet;
import java.sql.SQLException;

import asakichy.object_relational.structural.AppRuntimeException;
import asakichy.object_relational.structural.single_table_inheritance.Player.TYPE;

public class PlayerMapper extends Mapper {
	private FootballerMapper footballerMapper;
	private BowlerMapper bowlerMapper;
	private CricketerMapper cricketerMapper;

	public PlayerMapper() {
		footballerMapper = new FootballerMapper();
		bowlerMapper = new BowlerMapper();
		cricketerMapper = new CricketerMapper();
	}

	public Player find(long id) {
		ResultSet rs = findRow(id);
		try {
			String typeString = rs.getString("type");
			TYPE type = TYPE.valueOf(typeString);
			switch (type) {
			case FOOTBALL:
				return footballerMapper.buildDomainObject(rs);
			case CRICKET:
				return cricketerMapper.buildDomainObject(rs);
			case BOWLING:
				return bowlerMapper.buildDomainObject(rs);
			default:
				throw new AppRuntimeException("unknown type");
			}
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
	}

	public void update(Player player) {
		switch (player.getType()) {
		case FOOTBALL:
			footballerMapper.update(player);
			break;
		case CRICKET:
			cricketerMapper.update(player);
			break;
		case BOWLING:
			bowlerMapper.update(player);
			break;
		default:
			throw new AppRuntimeException("unknown type");
		}
	}

	public void insert(Player player) {
		switch (player.getType()) {
		case FOOTBALL:
			footballerMapper.insert(player);
			break;
		case CRICKET:
			cricketerMapper.insert(player);
			break;
		case BOWLING:
			bowlerMapper.insert(player);
			break;
		default:
			throw new AppRuntimeException("unknown type");
		}
	}

	@Override
	protected String table() {
		return "players";
	}

	@Override
	protected DomainObject createDomainObject() {
		throw new UnsupportedOperationException();
	}

}
