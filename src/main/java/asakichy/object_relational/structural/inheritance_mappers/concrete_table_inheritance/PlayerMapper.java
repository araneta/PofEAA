package asakichy.object_relational.structural.inheritance_mappers.concrete_table_inheritance;

import asakichy.object_relational.structural.AppRuntimeException;
import asakichy.object_relational.structural.NotFoundRuntimeException;

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
		try {
			return footballerMapper.findRow(id);
		} catch (NotFoundRuntimeException continuance) {
		}

		try {
			return cricketerMapper.findRow(id);
		} catch (NotFoundRuntimeException continuance) {
		}

		try {
			return bowlerMapper.findRow(id);
		} catch (NotFoundRuntimeException continuance) {
		}

		return null;
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
