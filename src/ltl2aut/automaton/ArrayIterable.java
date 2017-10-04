package ltl2aut.automaton;

import java.util.Iterator;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ArrayIterable<T> implements Iterable<T> {
	final T[] array;

	public ArrayIterable(final T[] array) {
		this.array = array;
	}

	public Iterator<T> iterator() {
		return new Iterator<T>() {
			int pos = 0;

			public boolean hasNext() {
				return pos < array.length;
			}

			public T next() {
				return array[pos++];
			}

			public void remove() {
				new NotImplementedException();
			}
		};
	}

}
