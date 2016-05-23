package structures;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class AVLSet<T> implements Iterable<T>, Set<T>{
	private Node<T> root;
	private Comparator<T> cmp;
	private int size;

	public AVLSet(Comparator<T> c) {
		cmp = c;
	}

	public boolean add(T value) {
		int prevSize = size();
		root = add(value, root);
		return prevSize != size();
	}

	private Node<T> add(T value, Node<T> n) {
		if (n == null) {
			size += 1;
			return new Node<T>(value);
		}
		int comp = cmp.compare(value, n.value);
		if (comp > 0)
			n.right = add(value, n.right);
		else if (comp < 0)
			n.left = add(value, n.left);
		n.updateHeight();
		return rebalance(n);
	}

	public boolean addAll(Collection<? extends T> c) {
		boolean changed = false;
		for (T each : c)
			changed = add(each) || changed;
		return changed;
	}

	public void clear() {
		root = null;
	}

	@SuppressWarnings("unchecked")
	public boolean contains(Object value) {
		return contains((T) value, root);
	}

	private boolean contains(T value, Node<T> t) {
		if (t == null)
			return false;
		int comp = cmp.compare(value, t.value);
		if (comp > 0)
			return contains(value, t.right);
		else if (comp < 0)
			return contains(value, t.left);
		return true;
	}

	public boolean containsAll(Collection<?> c) {
		for (Object each : c)
			if (!contains(each))
				return false;
		return true;
	}

	public boolean isEmpty() {
		return root == null;
	}

	public Iterator<T> iterator() {
		return new InorderIterator<T>(root);
	}

	@SuppressWarnings("unchecked")
	public boolean remove(Object value) {
		int prevSize = size();
		root = remove((T) value, root);
		return prevSize != size();
	}

	public Node<T> remove(T value, Node<T> t) {
		if (t == null)
			return t;

		int comp = cmp.compare(value, t.value);

		if (comp < 0)
			t.left = remove(value, t.left);
		else if (comp > 0)
			t.right = remove(value, t.right);
		else {
			size -= 1;

			if(!t.hasChildren())
				return null;

			if (!t.hasBothChildren())
				return t.hasRightChild() ? t.right : t.left;

			if (t.right.hasLeftChild())
				t.value = removeMin(t.right.left, t.right); // devuelve el minímo mientras balancea
			else {
				t.value = t.right.value;
				t.right = t.right.right;
			}
		}

		t.updateHeight();
		return rebalance(t);
	}

	public boolean removeAll(Collection<?> c) {
		boolean changed = false;
		for (Object each : c)
			changed = remove(each) || changed;
		return changed;
	}

	public boolean retainAll(Collection<?> c) {
		boolean changed = false;
		for (Object each : this)
			if (!c.contains(each)) {
				changed = true;
				remove(each);
			}
		return changed;
	}

	public int size() {
		return size;
	}

	public Object[] toArray() {
		Object[] arr = new Object[size()];
		int i = 0;
		for (Object each : this)
			arr[i++] = each;
		return arr;
	}

	@SuppressWarnings("unchecked")
	public <E> E[] toArray(E[] a) {
		if (a.length < size())
			a = (E[]) Array.newInstance(a[0].getClass(), size());

		int i = 0;
		for (Object each : this)
			a[i++] = (E) each;

		return a;
	}

	private Node<T> rebalance(Node<T> n) {
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

	private T removeMin(Node<T> n, Node<T> prev) {
		if (!n.hasLeftChild()) {
			prev.left = n.right;
			return n.value;
		}
		T min = removeMin(n.left, n);
		n.updateHeight();
		prev.left = rebalance(n);
		return min;
	}

	private Node<T> rotateLeft(Node<T> n) {
		Node<T> right = n.right;
		n.right = right.left;
		right.left = n;
		n.updateHeight();
		right.updateHeight();
		return right; // devolvemos lo que ahora ocupa el lugar de n
	}

	private Node<T> rotateRight(Node<T> n) {
		Node<T> left = n.left;
		n.left = left.right;
		left.right = n;
		n.updateHeight();
		left.updateHeight();
		return left; // devolvemos lo que ahora ocupa el lugar de n
	}

	@Override
	public String toString() {
		return toString(root);
	}

	private String toString(Node<T> n) {
		String str = "";
		if (n != null) {
			str = str.concat(toString(n.left));
			str = str.concat(n.value.toString() + " ");
			str = str.concat(toString(n.right));
		}
		return str;
	}

	private static class Node<T> {
		private T value;
		private Node<T> left;
		private Node<T> right;
		private int height;

		public Node(T v) {
			value = v;
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

	private static class InorderIterator<T> implements Iterator<T> {
		// arriba del stack está siempre el nodo cuyo valor devolver
		private Deque<Node<T>> stack = new LinkedList<>();

		public InorderIterator(Node<T> root) {
			stack.push(root);
			Node<T> t;
			while ((t = stack.peek()).hasLeftChild())
				stack.push(t.left);
		}

		@Override
		public boolean hasNext() {
			return !stack.isEmpty();
		}

		@Override
		public T next() {
			Node<T> t = stack.pop();
			T value = t.value;

			if (t.hasRightChild()) {
				stack.push(t.right);
				while ((t = stack.peek()).hasLeftChild())
					stack.push(t.left);
			}

			return value;
		}
	}
}