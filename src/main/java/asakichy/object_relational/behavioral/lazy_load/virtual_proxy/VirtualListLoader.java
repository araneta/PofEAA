package asakichy.object_relational.behavioral.lazy_load.virtual_proxy;

import java.util.List;

/**
 * 仮想プロキシー・ローダ・インターフェイス.
 */

public interface VirtualListLoader<E> {
	List<E> load();
}
