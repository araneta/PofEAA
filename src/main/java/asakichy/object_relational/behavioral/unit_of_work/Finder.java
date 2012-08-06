package asakichy.object_relational.behavioral.unit_of_work;

/**
 * データマッパーの検索インターフェイス.
 */

public interface Finder<E> {
	E find(long id);
}
