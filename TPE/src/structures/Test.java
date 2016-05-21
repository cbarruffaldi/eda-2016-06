package structures;

public class Test {

	public static void main(String[] args) {
		BinaryMinHeap<Integer> heap = new BinaryMinHeap<>((a,b)->Integer.compare(a, b));

		heap.add(7);
		heap.add(1);
		heap.add(123);
		heap.add(-2);
		heap.add(1);

		heap.replace(1, 8);
		heap.replace(800, 1000);

		System.out.println(heap.removeMin());
		System.out.println(heap.removeMin());
		System.out.println(heap.removeMin());
		System.out.println(heap.removeMin());
		System.out.println(heap.removeMin());


	}
}
