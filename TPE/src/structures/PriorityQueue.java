package structures;

public interface PriorityQueue<T> {

	/**
	 * Encola un elemento.
	 * @param elem elemento a encolar.
	 * @param priority número mayor a cero que especifica la prioridad. Cuanto menor
	 * es este número mayor es la prioridad.
	 * @throws IllegalStateException Si no hay lugar en la cola en el caso de colas con tamaño fijo.
	 */
	public void enqueue(T elem, double priority);

	/**
	 * Desencola un elemento.
	 * @throws NoSuchElementException Si la cola está vacía.
	 */
	public T dequeue();

	/**
	 * Devuelve el tamaño de la cola.
	 */
	public int size();

	/**
	 * Devuelve la prioridad de un elemento.
	 * @param elem elemento a consultar prioridad.
	 * @return prioridad del elemento.
	 */
	public double getPriority(T elem);

	/**
	 * Decrece la prioridad de un elemento.
	 * @param elem elemento a decrecer prioridad.
	 * @param priority nueva prioridad menor a la prioridad actual.
	 * @throws IllegalArgumentException si la nueva prioridad es mayor a la actual.
	 */
	public void decreasePriority(T elem, double priority);

	/**
	 * Evalúa si la cola está vacía o no.
	 * @return true si está vacía, false sino.
	 */
	public boolean isEmpty();
}
