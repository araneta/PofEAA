package asakichy.object_relational.metadata_mapping.repository;

/**
 * レジストリ.
 */
public class Resistry {

	public static PersonRepository personRepository() {
		return new PersonRepository();
	}
}
