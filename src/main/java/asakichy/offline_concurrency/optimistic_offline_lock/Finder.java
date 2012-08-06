package asakichy.offline_concurrency.optimistic_offline_lock;

/**
 * データマッパーの検索インターフェイス.
 */

public interface Finder<E> {
	E find(long id);
}
