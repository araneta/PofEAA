package asakichy.object_relational.behavioral.identity_map;

/**
 * Personドメインオブジェクト.
 */

public class Person extends DomainObject {
	private String lastName;
	private String firstName;
	private long numberOfDependents;

	public Person(long id, String lastName, String firstName, long numberOfDependents) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.numberOfDependents = numberOfDependents;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public long getNumberOfDependents() {
		return numberOfDependents;
	}

	public void setNumberOfDependents(long numberOfDependents) {
		this.numberOfDependents = numberOfDependents;
	}

}
