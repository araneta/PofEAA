package asakichy.architecture.datasource_architecture.active_record;

import java.util.HashMap;
import java.util.Map;

public class Registry {

	public static Map<Long, Person> persons = new HashMap<Long, Person>();

	public static Person getPerson(long id) {
		return persons.get(id);
	}

	public static void addPerson(Person person) {
		persons.put(person.getId(), person);
	}

	public static void removePerson(Person person) {
		persons.remove(person.getId());
	}
}
