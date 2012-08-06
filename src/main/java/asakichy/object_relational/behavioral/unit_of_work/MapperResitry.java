package asakichy.object_relational.behavioral.unit_of_work;

import java.util.HashMap;
import java.util.Map;

/**
 * データマッパーのレジストリ.
 */

public class MapperResitry {
	private static Map<Class<?>, DataMapper> adapters = new HashMap<Class<?>, DataMapper>();

	static {
		adapters.put(Person.class, new DataMapper() {
			private PersonMapper mapper = new PersonMapper();

			@Override
			public void insert(DomainObject subject) {
				Person person = Person.class.cast(subject);
				mapper.insert(person);
			}

			@Override
			public void update(DomainObject subject) {
				Person person = Person.class.cast(subject);
				mapper.update(person);
			}

			@Override
			public void delete(DomainObject subject) {
				Person person = Person.class.cast(subject);
				mapper.delete(person);
			}

			@Override
			public DomainObject find(long id) {
				return mapper.find(id);
			}
		});
	}

	public static DataMapper getMapper(Class<?> type) {
		return adapters.get(type);
	}
}
