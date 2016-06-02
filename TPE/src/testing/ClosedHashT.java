package testing;

import java.util.concurrent.DelayQueue;

import structures.ClosedHash;

public class ClosedHashT {
	public static void main(String[] args) {
		ClosedHash<String, Integer> hash = new ClosedHash<>(100);
		String[] airports = new String[]{"AAA", "BBB", "CCC", "DDD", "EEE", "FFF", "GGG", "HHH", "III", 
				"JJJ", "KKK", "LLL", "MMM", "NNN", "OOO", "PPP", "QQQ", "RRR",
				"SSS", "TTT", "UUU", "VVV", "WWW", "XXX", "YYY", "ZQB"};

		int i =0;
		for(String s: airports){
			hash.put(s, ++i);
		}

		hash.remove("WWW");
		hash.remove("ZQB");
		hash.remove("DDD");
		hash.remove("JJJ");
		for(String s: airports)
		{
		if(!hash.containsKey(s))
			System.out.println("ERR " + s);
		}
	}
}
