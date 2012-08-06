package asakichy.architecture.datasource_architecture.data_mapper.domain;

/**
 * データマッパーの検索インターフェイス.
 */

public interface Finder<E> {
	E find(long id);
}
