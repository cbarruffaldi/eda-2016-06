package structures;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ClosedHash<K,V> implements SimpleMap<K,V> {

	private static class Data<K,V> {
		private K key;
		private V value;

		public Data(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public void delete() {
			key = null;
			value = null;
		}

		public boolean isDeleted() {
			return key == null;
		}
	}

	private static final double EXPAND_LOAD = 2.0/3; // se expande si la tabla está un dos tercios llena

	private Data<K,V>[] array;
	private int size;

	@SuppressWarnings("unchecked")
	public ClosedHash(int initialSize) {
		array = (Data<K,V>[]) Array.newInstance(Data.class, initialSize);
	}

	private int hash(K key) {
		return Math.abs(key.hashCode()) % array.length;
	}

	@Override
	public void put(K key, V value) {
		int index = hash(key);
		while (array[index] != null && !array[index].isDeleted() && !key.equals(array[index].key)) {
			index = nextIndex(index);
		}
		if (array[index] == null || array[index].isDeleted()) {
			array[index] = new Data<K,V>(key, value);
			size += 1;
			if (mustExpand())
				expand();
		}
		else
			array[index].value = value;
	}

	@Override
	public V get(K key) {
		int index = findIndex(key);
		return index != -1 ? array[index].value : null;
	}

	@Override
	public void remove(K key) {
		int index = findIndex(key);
		if (index != -1) {
			size -= 1;
			array[index].delete();
		}
	}

	@Override
	public Set<K> keySet() {
		Set<K> set = new HashSet<>();
		for (Data<K,V> data : array) {
			if (data != null && !data.isDeleted())
				set.add(data.key);
		}
		return set;
	}

	@Override
	public Collection<V> values() {
		Collection<V> values = new ArrayList<>(size());
		for (Data<K,V> data : array) {
			if (data != null && !data.isDeleted())
				values.add(data.value);
		}
		return values;
	}

	@Override
	public int size() {
		return size;
	}

	private int nextIndex(int index) {
		return index + 1 == array.length ? 0 : index + 1;
	}

	@SuppressWarnings("unchecked")
	private void expand() {
		Data<K,V>[] aux = array;
		int newArraySize = array.length * 2;
		array = (Data<K,V>[]) Array.newInstance(Data.class, newArraySize);
		reHash(aux);
	}

	private void reHash(Data<K,V>[] aux) {
		int size = size(); // guardamos size pues put lo modificará
		for(Data<K,V> data : aux)
			if (data != null && !data.isDeleted())
				put(data.key, data.value); // modificará size
		this.size = size;
	}

	private boolean mustExpand() {
		return (double) size() / array.length > EXPAND_LOAD;
	}

	private int findIndex(K key) {
		int index = hash(key);
		while(array[index] != null && !key.equals(array[index].key))
			index = nextIndex(index);
		return (array[index] != null && !array[index].isDeleted()) ? index : -1;
	}

	@Override
	public boolean containsKey(K key) {
		return get(key) != null;
	}

	@Override
	public boolean containsValue(V value) {
		for (int i = 0; i < array.length; i++)
			if (array[i] != null && array[i].value.equals(value))
				return true;
		return false;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void clear() {
		array = (Data<K,V>[]) Array.newInstance(Data.class, array.length);
		size = 0;
	}

	@Override
	public Iterator<K> keyIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<V> valueIterator() {
		// TODO Auto-generated method stub
		return null;
	}
}