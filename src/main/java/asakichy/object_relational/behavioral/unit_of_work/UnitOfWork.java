package asakichy.object_relational.behavioral.unit_of_work;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;

/**
 * ユニットオブワーク.
 */

public class UnitOfWork<E extends DomainObject> {
	private List<E> newObjects = new ArrayList<E>();
	private List<E> dirtyObjects = new ArrayList<E>();
	private List<E> removedObjects = new ArrayList<E>();

	public void resisterNew(E obj) {
		Validate.notNull(obj, "object not null");
		Validate.isTrue(!dirtyObjects.contains(obj), "object not dirty");
		Validate.isTrue(!removedObjects.contains(obj), "object not removed");
		Validate.isTrue(!newObjects.contains(obj), "object not already registered");

		newObjects.add(obj);
	}

	public void resisterDirty(E obj) {
		Validate.notNull(obj, "object not null");
		Validate.isTrue(!removedObjects.contains(obj), "object not removed");

		if (!dirtyObjects.contains(obj) && !newObjects.contains(obj)) {
			dirtyObjects.add(obj);
		}
	}

	public void resisterRemoved(E obj) {
		Validate.notNull(obj, "object not null");

		if (newObjects.remove(obj)) {
			return;
		}

		dirtyObjects.remove(obj);

		if (!removedObjects.contains(obj)) {
			removedObjects.add(obj);
		}
	}

	public void resisterClean(E obj) {
		Validate.notNull(obj, "object not null");
	}

	public void commit() {
		insert();
		update();
		delete();
	}

	private void insert() {
		for (E obj : newObjects) {
			MapperResitry.getMapper(obj.getClass()).insert(obj);
		}
	}

	private void update() {
		for (E obj : dirtyObjects) {
			MapperResitry.getMapper(obj.getClass()).update(obj);
		}
	}

	private void delete() {
		for (E obj : removedObjects) {
			MapperResitry.getMapper(obj.getClass()).delete(obj);
		}
	}

}
