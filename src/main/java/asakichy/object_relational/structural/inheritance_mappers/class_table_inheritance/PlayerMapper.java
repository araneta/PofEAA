package asakichy.object_relational.structural.inheritance_mappers.class_table_inheritance;

import java.sql.ResultSet;
import java.sql.SQLException;

import asakichy.object_relational.structural.AppRuntimeException;
import asakichy.object_relational.structural.inheritance_mappers.class_table_inheritance.Player.TYPE;

public class PlayerMapper extends Mapper {
	private static String TABLE_NAME = "players";

	private FootballerMapper footballerMapper;
	private BowlerMapper bowlerMapper;
	private CricketerMapper cricketerMapper;

	public PlayerMapper() {
		footballerMapper = new FootballerMapper();
		bowlerMapper = new BowlerMapper();
		cricketerMapper = new CricketerMapper();
	}

	public Player find(long id) {
		try {
			ResultSet rs = findRowByTable(id, TABLE_NAME);
			String name = rs.getString("name");
			String typeString = rs.getString("type");

			TYPE type = TYPE.valueOf(typeString);
			Player player;
			switch (type) {
			case FOOTBALL:
				player = footballerMapper.findRow(id);
				break;
			case CRICKET:
				player = cricketerMapper.findRow(id);
				break;
			case BOWLING:
				player = bowlerMapper.findRow(id);
				break;
			default:
				throw new AppRuntimeException("unknown type");
			}
			player.setName(name);
			player.setType(type);
			return player;
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

	public void delete(Player player) {
		switch (player.getType()) {
		case FOOTBALL:
			footballerMapper.delete(player);
			break;
		case CRICKET:
			cricketerMapper.delete(player);
			break;
		case BOWLING:
			bowlerMapper.delete(player);
			break;
		default:
			throw new AppRuntimeException("unknown type");
		}
	}

	@Override
	protected DomainObject createDomainObject() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void updateRow(DomainObject domainObject) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void deleteRow(DomainObject domainObject) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void insertRow(DomainObject domainObject) {
		throw new UnsupportedOperationException();
	}

}
