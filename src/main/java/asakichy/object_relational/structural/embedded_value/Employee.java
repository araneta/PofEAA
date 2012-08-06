package asakichy.object_relational.structural.embedded_value;

/**
 * Employeeドメインオブジェクト.
 */

public class Employee extends DomainObject {
	private String name;
	private DateRange period;
	private Money salary;

	public Employee(long id, String name, DateRange period, Money salary) {
		super(id);
		this.name = name;
		this.period = period;
		this.salary = salary;
	}

	public Employee(long id) {
		this(id, null, null, null);
	}

	public String getName() {
		return name;
	}

	public DateRange getPeriod() {
		return period;
	}

	public Money getSalary() {
		return salary;
	}

}
