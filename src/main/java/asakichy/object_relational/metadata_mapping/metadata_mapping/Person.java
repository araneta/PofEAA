package asakichy.object_relational.metadata_mapping.metadata_mapping;

/**
 * Personドメインオブジェクト.
 */

public class Person extends DomainObject {
	private String firstName;
	private String lastName;
	private int numberOfDependents;

	public Person(long id, String firstName, String lastName, int numberOfDependents) {
		super(id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.numberOfDependents = numberOfDependents;
	}

	public Person() {
		this(-1, null, null, -1);
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public int getNumberOfDependents() {
		return numberOfDependents;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setNumberOfDependents(int numberOfDependents) {
		this.numberOfDependents = numberOfDependents;
	}

}
