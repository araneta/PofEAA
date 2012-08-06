package asakichy.object_relational.structural.inheritance_mappers.concrete_table_inheritance;

public abstract class AbstractPlayerMapper extends Mapper {

	@Override
	public Player findRow(long id) {
		return (Player) super.findRow(id);
	}

}
