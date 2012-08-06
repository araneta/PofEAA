package asakichy.object_relational.behavioral.lazy_load.value_holder;

import java.util.List;

/**
 * バリューホルダー.
 */

public class ValueHolder<E> {
	List<E> values;
	ValueLoader<E> loader;

	public ValueHolder(ValueLoader<E> loader) {
		this.loader = loader;
	}

	public List<E> getValues() {
		if (values == null) {
			values = loader.load();
		}
		return values;
	}

	public interface ValueLoader<E> {
		List<E> load();
	}

}
