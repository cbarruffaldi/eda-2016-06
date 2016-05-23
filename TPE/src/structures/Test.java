package structures;

import java.util.Iterator;

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

//		System.out.println(heap.removeMin());
//		System.out.println(heap.removeMin());
//		System.out.println(heap.removeMin());
//		System.out.println(heap.removeMin());
//		System.out.println(heap.removeMin());

		/***********************************************************/

		AVLSet<Integer> set = new AVLSet<>((a,b)->Integer.compare(a, b));

		set.add(7);
		set.add(1);
		set.add(123);
		set.add(-2);
		set.add(1);
		set.add(29);

		Iterator<Integer> iter = set.higherIterator(2);

		System.out.println(set);

		while (iter.hasNext())
			System.out.println(iter.next());

	}
}
