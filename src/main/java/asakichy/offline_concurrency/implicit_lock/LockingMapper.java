package asakichy.offline_concurrency.implicit_lock;

/**
 * データマッパーに重オフラインロック機能をデコレートします.
 */

public class LockingMapper<E extends DomainObject> implements Mapper<E> {

	private Mapper<E> impl;
	private String locker;

	public LockingMapper(Mapper<E> impl, String locker) {
		this.impl = impl;
		this.locker = locker;
	}

	@Override
	public E find(long id) {
		ExclusiveReadLockManager.INSTANCE.aqcuireLock(id, locker);
		return impl.find(id);
	}

	@Override
	public void insert(E subject) {
		impl.insert(subject);
		ExclusiveReadLockManager.INSTANCE.releaseLock(0, locker);
	}

	@Override
	public void update(E subject) {
		impl.update(subject);
		ExclusiveReadLockManager.INSTANCE.releaseLock(0, locker);
	}

	@Override
	public void delete(E subject) {
		impl.delete(subject);
		ExclusiveReadLockManager.INSTANCE.releaseLock(0, locker);
	}

}
