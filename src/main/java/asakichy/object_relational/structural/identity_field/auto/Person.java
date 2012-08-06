package asakichy.object_relational.structural.identity_field.auto;

/**
 * Personドメインオブジェクト.
 */

public class Person {
	private long id;
	private String lastName;
	private String firstName;
	private long numberOfDependents;

	public Person(long id, String lastName, String firstName, long numberOfDependents) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.numberOfDependents = numberOfDependents;
	}

	public Person(String lastName, String firstName, long numberOfDependents) {
		this(-1L, lastName, firstName, numberOfDependents);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
