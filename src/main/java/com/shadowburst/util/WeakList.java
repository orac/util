package com.shadowburst.util;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * A list of non-null weak references.
 * 
 * @param <E>
 *            The element type. Only @code WeakReference<E> instances will actually be stored.
 * 
 * @warning Certain methods check for newly-dead weak references, and reshuffle other elements to
 *          adapt. These methods may have side-effects of changing the position of elements in the
 *          list and the size of the list, so be particularly careful not to store element positions
 *          across calls to such methods. These methods are marked in the documentation. In
 *          particular, it's best to avoid the methods that return or take indices/positions of
 *          elements, and stick with iterators instead.
 */
public class WeakList<E> implements List<E> {
	private class ListIterator implements java.util.ListIterator<E> {
		private java.util.ListIterator<WeakReference<E>> iter;

		/**
		 * If non-null, this is the 'cached' result of a call to iter.next(), which means that #iter
		 * points one ahead of where it ought to. This has to be a strong reference so that it can't
		 * be removed by the GC between (say) a call to #hasNext() and the following call to
		 * #next().
		 */
		private E next;

		/** Similarly, this is the result of a call to iter.nextIndex(), giving the index of {@link #next}. */
		private int nextIndex;

		protected ListIterator() {
			iter = impl.listIterator();
		}

		protected ListIterator(int location) {
			iter = impl.listIterator(location);
		}

		@Override
		public void add(E object) {
			if (object == null) {
				throw new NullPointerException("'object' argument was null");
			} else {
				iter.add(new WeakReference<E>(object));
				if (nextIndex >= 0) {
					++nextIndex;
				}
			}
		}

		/**
		 * @copyDoc Gives results consistent with next(). That is, if this function returns true, it
		 *          also prevents the next element being GC'ed, to ensure that the following call to
		 *          next() will return a non-null element.
		 * @return True iff the following call to next() will succeed.
		 */
		@Override
		public boolean hasNext() {
			advanceToNext();
			return next != null;
		}

		@Override
		public boolean hasPrevious() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public E next() {
			advanceToNext();
			if (next == null) {
				throw new NoSuchElementException();
			}
			E temp = next;
			next = null;
			nextIndex = -1;
			return temp;
		}

		@Override
		public int nextIndex() {
			if (nextIndex < 0) {
				advanceToNext();
				if (nextIndex < 0) {
					throw new NoSuchElementException();
				}
			}
			return nextIndex;
		}

		@Override
		public E previous() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int previousIndex() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void remove() {
			if (next != null) {
				throw new IllegalStateException();
			}
			iter.remove();
		}

		@Override
		public void set(E object) {
			if (next != null) {
				throw new IllegalStateException();
			}
			iter.set(new WeakReference<E>(object));
		}

		/**
		 * Advances forwards until we find a non-null element, or the underlying iterator is empty.
		 * If {@link #next} starts out non-null, this call has no effect. Otherwise, it advances
		 * forward, removing cleared weak references, until it finds a live one, in which case it
		 * sets {@link #next} to that, or until the underlying iterator runs out, in which case
		 * {@link #next} will remain null.
		 */
		private void advanceToNext() {
			while (next == null && iter.hasNext()) {
				nextIndex = iter.nextIndex();
				next = iter.next().get();
				if (next == null) {
					iter.remove();
				}
			}
			if (next == null) {
				nextIndex = -1;
			}
		}
	}

	ArrayList<WeakReference<E>> impl;

	public WeakList() {
		impl = new ArrayList<WeakReference<E>>();
	}

	public WeakList(int capacity) {
		impl = new ArrayList<WeakReference<E>>(capacity);
	}

	/**
	 * @copyDoc
	 * @warning May reshuffle elements. This means that the element you get may not be the one you
	 *          expected.
	 */
	@Override
	public E get(int index) {
		E result = null;
		while (result == null) {
			result = impl.get(index).get();
			if (result == null) {
				impl.remove(index);
			}
		}
		return result;
	}

	@Override
	public int size() {
		return impl.size();
	}

	@Override
	public boolean add(E object) {
		impl.add(new WeakReference<E>(object));
		return true;
	}

	@Override
	public void add(int location, E object) {
		impl.add(location, new WeakReference<E>(object));
	}

	/**
	 * @copyDoc
	 * @warning If @p collection is another @p WeakList, it may be reshuffled.
	 */
	@Override
	public boolean addAll(Collection<? extends E> collection) {
		boolean isEffectful = false;
		for (E element : collection) {
			impl.add(new WeakReference<E>(element));
			isEffectful = true;
		}
		return isEffectful;
	}

	@Override
	public boolean addAll(int location, Collection<? extends E> collection) {
		boolean isEffectful = false;
		for (E element : collection) {
			impl.add(location++, new WeakReference<E>(element));
			isEffectful = true;
		}
		return isEffectful;
	}

	@Override
	public void clear() {
		impl.clear();
	}

	/**
	 * @copyDoc
	 * @warning May reshuffle elements.
	 * @throws NullPointerException
	 *             if @p object is null.
	 */
	@Override
	public boolean contains(Object object) {
		if (object == null) {
			throw new NullPointerException("'object' argument was null");
		}
		for (E element : this) {
			if (object.equals(element)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @copyDoc
	 * @throws NullPointerException
	 *             if @p collection or any element thereof is null.
	 * @warning May reshuffle elements.
	 */
	@Override
	public boolean containsAll(Collection<?> collection) {
		if (collection == null) {
			throw new NullPointerException("'collection' argument was null");
		}
		for (Object o : collection) {
			if (!contains(o)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @copyDoc
	 * @warning May reshuffle elements.
	 * @throws NullPointerException
	 *             if @p o is null.
	 */
	@Override
	public int indexOf(Object o) {
		if (o == null) {
			throw new NullPointerException("'o' argument was null");
		}
		int index = 0;
		for (E element : this) {
			if (o.equals(element)) {
				return index;
			}
			index++;
		}
		return -1;
	}

	@Override
	public boolean isEmpty() {
		return impl.isEmpty();
	}

	@Override
	public java.util.Iterator<E> iterator() {
		return new ListIterator();
	}

	/**
	 * @copyDoc
	 * @warning May reshuffle elements.
	 * @throws NullPointerException
	 *             if @p o is null.
	 */
	@Override
	public int lastIndexOf(Object o) {
		if (o == null) {
			throw new NullPointerException("'o' argument was null");
		}
		int result = -1;
		int i = 0;
		for (E element : this) {
			if (o.equals(element)) {
				result = i;
			}
			++i;
		}
		return result;
	}

	@Override
	public ListIterator listIterator() {
		return new ListIterator();
	}

	@Override
	public ListIterator listIterator(int location) {
		return new ListIterator(location);
	}

	/**
	 * Not supported.
	 * 
	 * @throws UnsupportedOperationException
	 *             unconditionally
	 */
	@Override
	public E remove(int location) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @copyDoc
	 * @warning May reshuffle elements.
	 * @throws NullPointerException
	 *             if @p object is null.
	 */
	@Override
	public boolean remove(Object object) {
		if (object == null) {
			throw new NullPointerException("'object' argument was null");
		}
		for (ListIterator i = listIterator(); i.hasNext();) {
			E element = i.next();
			if (object.equals(element)) {
				i.remove();
				return true;
			}
		}
		return false;
	}

	/**
	 * Remove every element from this list that is equal to @p object.
	 * 
	 * @warning May reshuffle elements.
	 * @param object
	 *            The object to remove.
	 * @return True iff calling this method changed the list.
	 * @throws NullPointerException
	 *             if @p object is null.
	 */
	public boolean removeEvery(Object object) {
		if (object == null) {
			throw new NullPointerException("'object' argument was null");
		}

		boolean isEffectful = false;
		for (ListIterator i = listIterator(); i.hasNext();) {
			E element = i.next();
			if (object.equals(element)) {
				i.remove();
				isEffectful = true;
			}
		}
		return isEffectful;
	}

	/**
	 * @copyDoc
	 * @warning May reshuffle elements.
	 * @throws NullPointerException
	 *             if @p collection or any element thereof is null.
	 */
	@Override
	public boolean removeAll(Collection<?> collection) {
		boolean isEffectful = false;
		if (collection == null) {
			throw new NullPointerException("'collection' argument was null");
		}
		for (Object o : collection) {
			isEffectful = removeEvery(o) || isEffectful;
		}
		return false;
	}

	/**
	 * @copyDoc
	 * @warning May reshuffle elements.
	 * @throws NullPointerException
	 *             if @p collection is null.
	 */
	@Override
	public boolean retainAll(Collection<?> collection) {
		if (collection == null) {
			throw new NullPointerException("'collection' argument was null");
		}
		boolean isEffectful = false;
		for (ListIterator i = listIterator(); i.hasNext();) {
			E element = i.next();
			if (!collection.contains(element)) {
				i.remove();
				isEffectful = true;
			}
		}
		return isEffectful;
	}

	/**
	 * @copyDoc
	 * @throws NullPointerException
	 *             if @p object is null.
	 */
	@Override
	public E set(int location, E object) {
		if (object == null) {
			throw new NullPointerException("'object' argument was null");
		}
		ListIterator i = listIterator(location);
		E old = i.next();
		i.set(object);
		return old;
	}

	@Override
	public List<E> subList(int start, int end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray() {
		Object[] result = new Object[size()];
		int i = 0;
		for (E element : this) {
			result[i++] = element;
		}
		/* If no element needed filtering out, we're done. */
		if (result.length == i) {
			return result;
		}
		/* Otherwise, we need a new array of the right length. */
		Object[] second = new Object[i];
		for (i = 0; i < second.length; ++i) {
			second[i] = result[i];
		}
		return second;

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] array) {
		if (array.length < size()) {
			T[] result = (T[]) Array.newInstance(array.getClass().getComponentType(), size());
			int i = 0;
			try {
				for (E element : this) {
					result[i++] = (T) element;
				}
			} catch (ClassCastException e) {
				throw new ArrayStoreException();
			}
			/* If no element needed filtering out, we're done. */
			if (result.length == i) {
				return result;
			}
			/* Otherwise, we need a new array of the right length. */
			T[] second = (T[]) Array.newInstance(array.getClass().getComponentType(), i);
			for (i = 0; i < second.length; ++i) {
				second[i] = result[i];
			}
			return second;
		} else {
			int i = 0;
			try {
				for (E element : this) {
					array[i++] = (T) element;
				}
			} catch (ClassCastException e) {
				throw new ArrayStoreException();
			}
			if (i < array.length) {
				array[i] = null;
			}
			return array;
		}
	}

}
