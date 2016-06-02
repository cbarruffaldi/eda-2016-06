package structures;

import java.util.Iterator;

public class Test {

	public static void main(String[] args) {

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

		SimpleMap<String, Integer> map = new ClosedHash<>(50);

		map.put("AAA", 1);
		map.put("ABC", 1);
		map.put("ASD", 1);
		map.put("CBV", 1);
		map.put("BUE", 1);
		map.put("BBB", 1);
		map.put("BBB", 2);

		Iterator<String> iter = map.keyIterator();

		while (iter.hasNext())
			System.out.println(iter.next());
	}
}
