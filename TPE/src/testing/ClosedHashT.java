package testing;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import structures.ClosedHash;

public class ClosedHashT {

	private ClosedHash<String, Integer> hash = new ClosedHash<>(100);
	private String[] airports = new String[]{"ABC", "BBB", "CCC", "DDD", "EEE", "FFF", "GGG", "HHH", "III",
			"JJJ", "KKK", "LLL", "MMM", "NNN", "OOO", "PPP", "QQQ", "RRR",
			"SSS", "TTT", "UUU", "VVV", "WWW", "XXX", "YYY", "ZQB"};

	@Before
	public void setUp() {
		int i =0;
		for(String s: airports)
			hash.put(s, i++);
	}

	@Test
	public void clearTest() {
		int i = 0;
		hash.clear();
		assertTrue(hash.size() == 0);
		for (String s: airports) {
			assertFalse(hash.containsKey(s));
			assertFalse(hash.containsValue(i++));
		}
		setUp();
		mapTest();
	}

	@Test
	public void mapTest() {
		assertTrue(airports.length == hash.size());

		for (int i = 0; i < airports.length; i++)
			assertTrue(hash.containsValue(i));
		assertFalse(hash.containsValue(airports.length));

		for(String s: airports)
			assertTrue(hash.containsKey(s));
		assertFalse(hash.containsKey("ZZZ"));

		List<String> keyList = new LinkedList<String>();
		Iterator<String> keyIterator = hash.keyIterator();
		while (keyIterator.hasNext())
			keyList.add(keyIterator.next());
		for (String s : airports)
			assertTrue(keyList.contains(s));


		List<Integer> valueList = new LinkedList<Integer>();
		Iterator<Integer> valueIterator = hash.valueIterator();
		while (valueIterator.hasNext())
			valueList.add(valueIterator.next());
		for (int i = 0; i < airports.length; i++)
			assertTrue(valueList.contains(i));

		hash.remove("BBB");
		hash.remove("ZZZ");

		assertFalse(hash.containsKey("BBB"));
		assertFalse(hash.containsKey("ZZZ"));


		assertFalse(airports.length == hash.size());
		assertTrue(airports.length-1 == hash.size());

		for (String s: airports)
			hash.remove(s);

		assertTrue(0 == hash.size());
	}
}