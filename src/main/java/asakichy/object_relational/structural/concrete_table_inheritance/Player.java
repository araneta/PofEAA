package asakichy.object_relational.structural.concrete_table_inheritance;

/**
 * Playerドメインオブジェクト.
 */

public abstract class Player extends DomainObject {
	public enum TYPE {
		FOOTBALL, CRICKET, BOWLING
	}

	private String name;
	private TYPE type;

	public Player(long id, TYPE type) {
		super(id);
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

}
