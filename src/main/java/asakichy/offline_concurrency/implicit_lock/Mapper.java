package asakichy.offline_concurrency.implicit_lock;

/**
 * データマッパーの基本クラス. 
 */

public interface Mapper<E extends DomainObject> {
	
	E find(long id);
	void insert(E subject);
	void update(E subject);
	void delete(E subject);

}
