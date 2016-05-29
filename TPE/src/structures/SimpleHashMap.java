package structures;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class SimpleHashMap<K, V> implements SimpleMap<K, V> {
	private static final int DEFAULT_CAPACITY = 20;

	private int size;
	private AVLMap<K,V>[] buckets;
	private int mod;
	private Comparator<K> cmp;

	@SuppressWarnings("unchecked")
	private void initiate (int capacity, Comparator<K> comparator) {
		if (comparator == null)
			throw new IllegalArgumentException("Illegal comparator: null");
		if (capacity < 1)
			throw new IllegalArgumentException("Illegal capacity < 1");
		cmp = comparator;
		mod = capacity;
		buckets = (AVLMap[]) Array.newInstance(AVLMap.class, capacity);

		for (int i = 0; i < buckets.length; i++)
			buckets[i] = new AVLMap<>(comparator);
	}

	public SimpleHashMap(int capacity) {
		initiate(capacity, getNaturalComparator());
	}

	public SimpleHashMap(int capacity, Comparator<K> comparator) {
		initiate(capacity, comparator);
	}

	public SimpleHashMap(Comparator<K> comparator) {
		initiate(DEFAULT_CAPACITY, comparator);
	}

	public SimpleHashMap() {
		initiate(DEFAULT_CAPACITY,getNaturalComparator());
	}

	@Override
	public void put(K key, V value) {
		int i = hash(key);
		int prevSize = buckets[i].size();
		buckets[i].put(key, value);
		if (prevSize != buckets[i].size())
			size += 1;
	}

	@Override
	public V get(K key) {
		int i = hash(key);
		return buckets[i].get(key);
	}

	@Override
	public boolean containsKey(K key) {
		int i = hash(key);
		return buckets[i].containsKey(key);
	}

	@Override
	public void remove(K key) {
		int i = hash(key);
		int prevSize = buckets[i].size();
		buckets[i].remove(key);
		if (prevSize != buckets[i].size())
			size -= 1;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public void clear() {
		size = 0;
		for (AVLMap<K,V> bucket : buckets)
			bucket.clear();
	}

	@Override
	public boolean containsValue(V value) {
		for (AVLMap<K,V> bucket : buckets)
			if (bucket.containsValue(value))
				return true;
		return false;
	}

	private int hash(Object key) {
		int i = key.hashCode() % mod;
		return i > 0 ? i : -i;
	}

	private Comparator<K> getNaturalComparator() {
		return new Comparator<K>(){
			@SuppressWarnings("unchecked")
			@Override
			public int compare(K o1, K o2) {
				return ((Comparable<K>)o1).compareTo(o2);
			}
		};
	}

	@Override
	public Iterator<K> keyIterator() {
		IteratorGetter<K> keyIter = new IteratorGetter<K>() {
			@Override
			public Iterator<K> getIterator(SimpleMap<K,V> map) {
				return map.keyIterator();
			}
		};
		return new MapIterator<K>(buckets, keyIter);
	}

	@Override
	public Iterator<V> valueIterator() {
		IteratorGetter<V> valueIter = new IteratorGetter<V>() {
			@Override
			public Iterator<V> getIterator(SimpleMap<K,V> map) {
				return map.valueIterator();
			}
		};
		return new MapIterator<V>(buckets, valueIter);

	}

	private class MapIterator<T> implements Iterator<T> {
		private AVLMap<K,V>[] buckets;
		private int i;
		private IteratorGetter<T> iterGetter;
		private Iterator<T> iter;

		public MapIterator(AVLMap<K,V>[] buckets, IteratorGetter<T> iterGetter) {
			this.iterGetter = iterGetter;
			this.buckets = buckets;
			i = -1;
			iter = getNextIterator();
		}

		/**
		 * Avanza el índice i y a partir de ahí busca el siguiente iterador
		 * correspondiente a un bucket no vacío.
		 */
		private Iterator<T> getNextIterator() {
			i += 1;
			while (i < buckets.length && buckets[i].isEmpty())
				i++;
			if (i < buckets.length)
				return iterGetter.getIterator(buckets[i]);
			return null;
		}

		@Override
		public boolean hasNext() {
			return i < buckets.length;
		}
		@Override
		public T next() {
			T key = iter.next();
			if (!iter.hasNext())
				iter = getNextIterator();
			return key;
		}
	}

	private abstract class IteratorGetter<T> {
		public abstract Iterator<T> getIterator(SimpleMap<K,V> map);
	}

	@Override
	public Set<K> keySet() {
		AVLSet<K> set = new AVLSet<K>(cmp);
		for (int i = 0; i < buckets.length; i++)
			if (!buckets[i].isEmpty())
				set = set.merge(buckets[i].keySet());
		return set;
	}

	@Override
	public Collection<V> values() {
		Collection<V> collection = new LinkedList<V>();
		for (int i = 0; i < buckets.length; i++)
			if (!buckets[i].isEmpty())
				collection.addAll(buckets[i].values());
		return collection;
	}
}
