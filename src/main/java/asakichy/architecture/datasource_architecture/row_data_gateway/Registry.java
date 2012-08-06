package asakichy.architecture.datasource_architecture.row_data_gateway;

import java.util.HashMap;
import java.util.Map;

public class Registry {

	public static Map<Long, PersonGateway> persons = new HashMap<Long, PersonGateway>();

	public static PersonGateway getPerson(long id) {
		return persons.get(id);
	}

	public static void addPerson(PersonGateway person) {
		persons.put(person.getId(), person);
	}

	public static void removePerson(PersonGateway person) {
		persons.remove(person.getId());
	}
}
