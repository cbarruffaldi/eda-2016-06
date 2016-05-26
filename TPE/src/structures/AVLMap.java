package structures;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

public class AVLMap<K, V> implements SimpleMap<K, V> {

	private Node<K,V> root;
	private Comparator<K> cmp;
	private int size;

	public AVLMap(){
		this.cmp = new Comparator<K>() {
			@SuppressWarnings("unchecked")
			@Override
			public int compare(K o1, K o2) {
				return ((Comparable<K>)o1).compareTo(o2);
			}
		};
	}

	public AVLMap(Comparator<K> cmp) {
		this.cmp = cmp;
	}

	public boolean containsKey(K key) {
		return get(key) != null;
	}

	public void put(K key, V value) {
		root = put(key, value, root);
	}

	private Node<K,V> put(K key, V value, Node<K,V> n) {
		if (n == null) {
			size += 1;
			return new Node<K,V>(key, value);
		}
		int comp = cmp.compare(key, n.key);
		if (comp > 0)
			n.right = put(key, value, n.right);
		else if (comp < 0)
			n.left = put(key, value, n.left);
		else
			n.value = value;
		n.updateHeight();
		return rebalance(n);
	}

	@Override
	public V get(K key) {
		return get((K)key, root);
	}

	private V get(K key, Node<K,V> n) {
		if (n == null)
			return null;
		int comp = cmp.compare(key, n.key);
		if (comp < 0)
			return get(key, n.left);
		if (comp > 0)
			return get(key, n.right);
		return n.value;
	}

	@Override
	public int size() {
		return size;
	}

	public void remove(K key) {
		root = remove(key, root);
	}

	private Node<K,V> remove(K key, Node<K,V> n) {
		if (n == null)
			return n;
		int comp = cmp.compare(key, n.key);

		if (comp < 0)
			n.left = remove(key, n.left);
		else if (comp > 0)
			n.right = remove(key, n.right);
		else {
			size -= 1;

			if(!n.hasChildren())
				return null;

			if (!n.hasBothChildren())
				return n.hasRightChild() ? n.right : n.left;

			if (n.right.hasLeftChild()) {
				Node <K, V> min = removeMin(n.right.left, n.right); // devuelve el minímo mientras balancea
				n.key = min.key;
				n.value = min.value;
			}
			else {
				n.key = n.right.key;
				n.value = n.right.value;
				n.right = n.right.right;
			}
		}

		n.updateHeight();
		return rebalance(n);
	}

	private Node<K, V> removeMin(Node<K,V> n, Node<K,V> prev) {
		if (!n.hasLeftChild()) { // borro el mínimo
			prev.left = n.right;
			return n;
		}
		Node<K,V> min = removeMin(n.left, n);
		n.updateHeight();
		prev.left = rebalance(n);
		return min;
	}

	@Override
	public AVLSet<K> keySet() {
		AVLSet<K> set = new AVLSet<>(cmp);
		Queue<Node<K,V>> queue = new LinkedList<>();
		if (root != null) { // Agrega por nivel así no tiene que balancear
			queue.offer(root);
			while (!queue.isEmpty()) {
				Node<K,V> current = queue.poll();
				set.add(current.key);
				if (current.hasLeftChild())
					queue.offer(current.left);
				if (current.hasRightChild())
					queue.offer(current.right);
			}
		}
		return set;
	}

	@Override
	public Collection<V> values() {
		Collection<V> collection = new LinkedList<>();
		addValueTo(collection, root);
		return null;
	}

	private void addValueTo(Collection<V> collection, Node<K,V> n) {
		if (n != null) {
			collection.add(n.value);
			addValueTo(collection, n.left);
			addValueTo(collection, n.right);
		}
	}

	private Node<K,V> rebalance(Node<K,V> n) {
		int bf = n.getBF();
		if (bf < -1) {
			if (n.right.getBF() > 0)
				n.right = rotateRight(n.right);
			n = rotateLeft(n);
		}
		else if (bf > 1) {
			if (n.left.getBF() < 0)
				n.left = rotateLeft(n.left);
			n = rotateRight(n);
		}
		return n;
	}

	private Node<K,V> rotateLeft(Node<K,V> n) {
		Node<K,V> right = n.right;
		n.right = right.left;
		right.left = n;
		n.updateHeight();
		right.updateHeight();
		return right; // devolvemos lo q ahora ocupa el lugar de n
	}

	private Node<K,V> rotateRight(Node<K,V> n) {
		Node<K,V> left = n.left;
		n.left = left.right;
		left.right = n;
		n.updateHeight();
		left.updateHeight();
		return left; // devolvemos lo q ahora ocupa el lugar de n
	}

	private static class Node<K,V> {
		private K key;
		private V value;
		private Node<K,V> left;
		private Node<K,V> right;
		private int height;

		public Node(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public void updateHeight() {
			int lh = left == null ? -1 : left.height;
			int rh = right == null ? -1 : right.height;
			height = Integer.max(lh, rh) + 1;
		}

		public boolean hasChildren() {
			return hasLeftChild() || hasRightChild();
		}

		public boolean hasLeftChild() {
			return left != null;
		}

		public boolean hasRightChild() {
			return right != null;
		}

		public boolean hasBothChildren() {
			return hasLeftChild() && hasRightChild();
		}

		public int getBF() {
			int lh = left == null ? -1 : left.height;
			int rh = right == null ? -1 : right.height;
			return lh - rh;
		}
	}

	@Override
	public boolean isEmpty() {
		return root == null;
	}

	@Override
	public void clear() {
		size = 0;
		root = null;
	}

	@Override
	public boolean containsValue(V value) {
		return containsValue(value, root);
	}

	private boolean containsValue(V value, Node<K,V> n) {
		if (n == null)
			return false;
		if (n.value.equals(value))
			return true;
		return containsValue(value, n.left) || containsValue(value, n.right);
	}
}
