package asakichy.object_relational.metadata_mapping.repository;

import java.util.Set;

/**
 * Personドメインオブジェクト.
 */

public class Person extends DomainObject {
	private String firstName;
	private String lastName;
	private int numberOfDependents;
	private long benefactor;

	public Person(long id, String firstName, String lastName, int numberOfDependents, long benefactor) {
		super(id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.numberOfDependents = numberOfDependents;
		this.benefactor = benefactor;
	}

	public Person() {
		this(-1, null, null, -1, -1);
	}

	public Person(long id) {
		this(id, null, null, -1, -1);
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

	public long getBenefactor() {
		return benefactor;
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

	public void setBenefactor(long benefactor) {
		this.benefactor = benefactor;
	}

	public Set<Person> dependents() {
		return Resistry.personRepository().dependentsOf(this);
	}

}
