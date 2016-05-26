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

		AVLSet<Integer> set = new AVLSet<>();
		AVLSet<Integer> set2 = new AVLSet<>();

		set.add(7);
		set.add(1);
		set.add(123);
		set.add(-2);
		set.add(1);
		set.add(29);

		set2.add(20);
		set2.add(-40);
		set2.add(100);
		set2.add(-2);
		set2.add(1);
		set2.add(29);

		System.out.println(set);
		System.out.println(set2);

		System.out.println(set.merge(set2));
		System.out.println(set2.merge(set));

		/************************************************************/

		SimpleMap<Integer, Integer> map = new AVLMap<>();

		map.put(1, 1);
		map.put(2, 1);
		map.put(3, 1);
		map.put(4, 1);
		map.put(5, 1);
		map.put(6, 1);
		map.put(7, 1);

		Iterator<Integer> iter = map.keyIterator();

		while (iter.hasNext())
			System.out.println(iter.next());
	}
}
