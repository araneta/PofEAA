package asakichy.offline_concurrency.coarse_grained_lock;

/**
 * データマッパーの検索インターフェイス.
 */

public interface Finder<E> {
	E find(long id);
}
