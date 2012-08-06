package asakichy.object_relational.structural.association_table_mapping;

/**
 * Skillドメインオブジェクト.
 */

public class Skill extends DomainObject {

	private String name;

	public Skill(long id, String name) {
		super(id);
		this.name = name;
	}

	public Skill(long id) {
		this(id, "");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
