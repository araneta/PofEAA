package asakichy.object_relational.metadata_mapping.repository;

/**
 * テーブル「people」のデータマッパー.
 * 
 * メタデータマッピングを使用しています.
 */

public class PersonMapper extends Mapper<Person> {

	protected void loadDataMap() {
		dataMap = new MetaDataMap(Person.class, "people");
		dataMap.addColumn("firstname", "varchar", "firstName");
		dataMap.addColumn("lastname", "varchar", "lastName");
		dataMap.addColumn("number_of_dependents", "int", "numberOfDependents");
		dataMap.addColumn("benefactor", "int", "benefactor");
	}

}
