package asakichy.object_relational.structural.serialized_lob;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * Department情報.
 * 
 * シリアライズLOBを使用し、XMLでデータベースに格納されます.
 * 
 */
@XmlRootElement(name = "department")
public class Department {
	private String name;
	private List<Department> subsidiaries = new ArrayList<Department>();

	public Department(String name, List<Department> subsidiaries) {
		this.name = name;
		this.subsidiaries = subsidiaries;
	}

	public Department() {
		this(null, null);
	}

	public Department(String name) {
		this(name, null);
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	@XmlElement
	public List<Department> getSubsidiaries() {
		return subsidiaries;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSubsidiaries(List<Department> subsidiaries) {
		this.subsidiaries = subsidiaries;
	}

	public void addSubsidiary(Department department) {
		if (subsidiaries == null) {
			subsidiaries = new ArrayList<Department>();
		}
		subsidiaries.add(department);
	}
}
