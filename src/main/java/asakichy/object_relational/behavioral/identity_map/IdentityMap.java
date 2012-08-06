package asakichy.object_relational.behavioral.identity_map;

import java.util.HashMap;
import java.util.Map;

/**
 * 一意マッピング.
 */

public class IdentityMap {
	static private IdentityMap instance = new IdentityMap();

	private IdentityMap() {
	}

	private Map<Long, Person> pepole = new HashMap<Long, Person>();

	public static void addPerson(Person person) {
		instance.pepole.put(person.getId(), person);
	}

	public static Person getPerson(long id) {
		return instance.pepole.get(id);
	}

}
