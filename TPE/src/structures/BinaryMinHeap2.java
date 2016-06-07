package structures;

import java.lang.reflect.Array;
import java.util.NoSuchElementException;

public class BinaryMinHeap2<T> implements PriorityQueue<T>{

	private final static int HASH_CAPACITY_FACTOR = 3;

	private PQNode<T>[] array;
	private SimpleMap<T,Integer> indexMap;

	private int size;
	
	
	@SuppressWarnings("unchecked")
	public BinaryMinHeap2(int capacity) {
		array = (PQNode<T>[]) Array.newInstance(PQNode.class, capacity);
		indexMap = new ClosedHash<T,Integer>(HASH_CAPACITY_FACTOR * capacity);
	}

	@Override
	public void enqueue(T elem, double priority) {
		PQNode<T> node = new PQNode<T>(elem, priority);
		int index = size();
		size++;
		insert(node, index);
		moveUp(index);

	}

	@Override
	public T dequeue() {
		if (isEmpty())
			throw new NoSuchElementException("Cola vacia");
		
		T elem = array[0].value;
		int size = size();
		indexMap.remove(elem);
		this.size--; 

		if (size == 1)
			array[0] = null;
		else {
			int lastIndex = size - 1;
			PQNode<T> last = array[lastIndex];
			insert(last, 0); // se sube el último elemento
			array[lastIndex] = null;
			moveDown(0);  // se baja
		}
	
		return elem;
	}

	public T head() {
		if (isEmpty())
			throw new NoSuchElementException("Cola vacia");
		
		return array[0].value;
	}

	
	@Override
	public double getPriority(T elem) {
		Integer index = indexMap.get(elem);
		if (index == null)
			throw new IllegalArgumentException("No existe el elemento");
		return array[index].priority;
	}

	@Override
	public void decreasePriority(T elem, double priority) {
		Integer index = indexMap.get(elem);
		if (index == null)
			return;
		if (array[index].priority <= priority){
			array[index].priority = priority;
			moveDown(index);
		}
		array[index].priority = priority;
		moveUp(index);
	}

	@Override
	public boolean isEmpty() {
		return indexMap.isEmpty();
	}

	@Override
	public int size() {
		return size;
		//return indexMap.size();
	}

	private void insert(PQNode<T> pqNode, int index) {
		array[index] = pqNode;
		indexMap.put(pqNode.value, index);
	}

	private void moveUp(int i) {
		int parent = getParentIndex(i);
		PQNode<T> node = array[i];
		while (i != 0 && node.compareTo(array[parent]) < 0) {
			insert(array[parent], i);
			i = parent;
			parent = getParentIndex(i);
		}
		insert(node, i);
	}

	private void moveDown(int i) {
		int min = getMinChildIndex(i);
		if (min == -1)
			return;
		PQNode<T> node = array[i];
		while (min != -1 && node.compareTo(array[min]) > 0) { // tiene hijos y es mayor a alguno
			insert(array[min], i); // subo el hijo
			i = min;
			min = getMinChildIndex(i);
		}
		insert(node, i);
	}

	private int getMinChildIndex(int i) {
		int leftChild = getLeftIndex(i);
		int rightChild = getRightIndex(i);
		if (leftChild >= size()) // array[i] es hoja
			return -1;
		if (rightChild >= size()) // solo tiene hijo izquierdo
			return leftChild;
		return array[leftChild].compareTo(array[rightChild]) < 0 ? leftChild : rightChild;
	}

	private int getParentIndex(int i) {
		return (i-1)/2;
	}

	private int getLeftIndex(int i) {
		return i*2+1;
	}

	private int getRightIndex(int i) {
		return i*2+2;
	}

	private static class PQNode<T> implements Comparable<PQNode<T>> {
		private T value;
		private double priority;

		public PQNode(T value, double priority) {
			this.value = value;
			this.priority = priority;
		}

		@Override
		public int compareTo(PQNode<T> o) {
			return Double.compare(priority, o.priority);
		}
	}

	public Double minWeight() {
		if(isEmpty()){
			throw new IllegalStateException("Empty queue");
		}
		return array[0].priority;
	}

}
