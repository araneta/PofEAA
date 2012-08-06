package asakichy.object_relational.structural.association_table_mapping;

import java.util.List;

/**
 * Employeeドメインオブジェクト.
 */

public class Employee extends DomainObject {

	private String firstName;
	private String lastName;
	private List<Skill> skills;

	public Employee(long id, String firstName, String lastName, List<Skill> skills) {
		super(id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.skills = skills;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String name) {
		this.firstName = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<Skill> getSkills() {
		return skills;
	}

	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}

}
