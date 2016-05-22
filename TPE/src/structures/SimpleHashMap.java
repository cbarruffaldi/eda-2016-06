package structures;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

public class SimpleHashMap<K, V> implements SimpleMap<K, V> {
	private static final int DEFAULT_CAPACITY = 20;

	private int size;
	private AVLMap<K,V>[] buckets;
	private int mod;

	@SuppressWarnings("unchecked")
	public SimpleHashMap(int capacity, Comparator<K> comparator) {
		if (capacity < 1)
			throw new IllegalArgumentException("Illegal capacity < 1");

		mod = capacity;
		buckets = (AVLMap[]) Array.newInstance(AVLMap.class, capacity);

		for (int i = 0; i < buckets.length; i++)
			buckets[i] = new AVLMap<>(comparator);
	}

	public SimpleHashMap(Comparator<K> comparator) {
		this(DEFAULT_CAPACITY, comparator);
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
	public Set<K> keySet() {
		Set<K> set = buckets[0].keySet();
		for (int i = 1; i < buckets.length; i++)
			set.addAll(buckets[i].keySet());
		return set;
	}

	@Override
	public Collection<V> values() {
		Collection<V> collection = buckets[0].values();
		for (int i = 1; i < buckets.length; i++)
			collection.addAll(buckets[i].values());
		return collection;
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
}
