package asakichy.object_relational.behavioral.lazy_load.virtual_proxy;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 仮想プロキシー.
 */

public class VirtualList<E> implements List<E> {
	private List<E> source;
	private VirtualListLoader<E> loader;

	public VirtualList(VirtualListLoader<E> loader) {
		this.loader = loader;
	}

	public List<E> getSource() {
		if (source == null) {
			source = loader.load();
		}
		return source;
	}

	@Override
	public int size() {
		return getSource().size();
	}

	@Override
	public boolean isEmpty() {
		return getSource().isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return getSource().contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return getSource().iterator();
	}

	@Override
	public Object[] toArray() {
		return getSource().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return getSource().toArray(a);
	}

	@Override
	public boolean add(E e) {
		return getSource().add(e);
	}

	@Override
	public boolean remove(Object o) {
		return getSource().remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return getSource().containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return getSource().addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return getSource().addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return getSource().removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return getSource().retainAll(c);
	}

	@Override
	public void clear() {
		getSource().clear();
	}

	@Override
	public boolean equals(Object o) {
		return getSource().equals(o);
	}

	@Override
	public int hashCode() {
		return getSource().hashCode();
	}

	@Override
	public E get(int index) {
		return getSource().get(index);
	}

	@Override
	public E set(int index, E element) {
		return getSource().set(index, element);
	}

	@Override
	public void add(int index, E element) {
		getSource().add(index, element);
	}

	@Override
	public E remove(int index) {
		return getSource().remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return getSource().indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return getSource().lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return getSource().listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return getSource().listIterator(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return getSource().subList(fromIndex, toIndex);
	}

}
