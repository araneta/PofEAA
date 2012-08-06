package asakichy.object_relational.behavioral.unit_of_work;

/**
 * データマッパーのインターフェイス.
 */
interface DataMapper {

	void insert(DomainObject subject);

	void update(DomainObject subject);

	void delete(DomainObject subject);
	
	DomainObject find(long id);
}
