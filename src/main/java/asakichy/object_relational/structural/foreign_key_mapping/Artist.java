package asakichy.object_relational.structural.foreign_key_mapping;

/**
 * Artistドメインオブジェクト.
 */

public class Artist extends DomainObject {

	private String name;
	private String label;

	public Artist(long id, String name, String label) {
		super(id);
		this.name = name;
		this.label = label;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
