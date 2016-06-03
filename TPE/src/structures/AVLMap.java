package structures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class AVLMap<K, V> implements SimpleMap<K, V> {

	private AVLSet<Node<K,V>> set;
	private Comparator<K> cmp;

	public AVLMap(){
		this.cmp = new Comparator<K>() {
			@SuppressWarnings("unchecked")
			@Override
			public int compare(K o1, K o2) {
				return ((Comparable<K>)o1).compareTo(o2);
			}
		};
		initiateSet();
	}

	public AVLMap(Comparator<K> cmp) {
		this.cmp = cmp;
		initiateSet();
	}

	private void initiateSet() {
		set = new AVLSet<>(new Comparator<Node<K,V>>() {
			@Override
			public int compare(Node<K, V> o1, Node<K, V> o2) {
				return cmp.compare(o1.key, o2.key);
			}
		});
	}

	@Override
	public void put(K key, V value) {
		set.add(new Node<K,V>(key, value));
	}

	@Override
	public V get(K key) {
		Node<K,V> searchNode = new Node<>(key, null);
		Node<K,V> node = set.find(searchNode);
		return node.value;
	}

	@Override
	public void remove(K key) {
		Node<K,V> searchNode = new Node<>(key, null);
		set.remove(searchNode);
	}

	@Override
	public boolean containsKey(K key) {
		Node <K,V> searchNode = new Node<>(key, null);
		return set.contains(searchNode);
	}

	@Override
	public boolean containsValue(V value) {
		Iterator<V> iter = valueIterator();
		while (iter.hasNext())
			if (iter.next().equals(value))
				return true;
		return false;
	}

	@Override
	public int size() {
		return set.size();
	}

	@Override
	public boolean isEmpty() {
		return set.isEmpty();
	}

	@Override
	public void clear() {
		set.clear();
	}

	@Override
	public Iterator<K> keyIterator() {
		ValueGetter<K> getter = new ValueGetter<K>() {
			@Override
			public K getValue(Node<K,V> n) {
				return n.key;
			}
		};
		return new AVLMapIterator<K>(set.iterator(), getter);
	}

	@Override
	public Iterator<V> valueIterator() {
		ValueGetter<V> getter = new ValueGetter<V>() {
			@Override
			public V getValue(Node<K,V> n) {
				return n.value;
			}
		};
		return new AVLMapIterator<V>(set.iterator(), getter);
	}

	@Override
	public AVLSet<K> keySet() {
		Iterator<K> iter = keyIterator();
		AVLSet<K> set = new AVLSet<K>(cmp);
		while (iter.hasNext())
			set.add(iter.next());
		return set;
	}

	@Override
	public Collection<V> values() {
		Iterator<V> iter = valueIterator();
		Collection<V> collection = new ArrayList<V>(size());
		while (iter.hasNext())
			collection.add(iter.next());
		return collection;
	}

	private static class Node<K,V> {
		private K key;
		private V value;

		public Node(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}

	private class AVLMapIterator<T> implements Iterator<T> {
		private Iterator<Node<K,V>> iter;
		private ValueGetter<T> valueGetter;

		public AVLMapIterator(Iterator<Node<K,V>> iter, ValueGetter<T> valueGetter) {
			this.iter = iter;
			this.valueGetter = valueGetter;
		}

		@Override
		public boolean hasNext() {
			return iter.hasNext();
		}

		@Override
		public T next() {
			return valueGetter.getValue(iter.next());
		}
	}

	private abstract class ValueGetter<T> {
		public abstract T getValue(Node<K,V> n);
	}
}
