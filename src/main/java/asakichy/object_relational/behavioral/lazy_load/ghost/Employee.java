package asakichy.object_relational.behavioral.lazy_load.ghost;

/**
 * Employeeドメインオブジェクト.
 * 
 * ゴーストを使用し、データに初めてアクセスがあった時にデータをロードします.
 */

public class Employee extends DomainObject {
	private String name;

	public Employee(long id) {
		super(id);
	}

	public String getName() {
		load();
		return name;
	}

	public void setName(String name) {
		load();
		this.name = name;
	}

	private void load() {
		if (ghost()) {
			EmployeeMapper.instance.load(this);
		}
	}

}
