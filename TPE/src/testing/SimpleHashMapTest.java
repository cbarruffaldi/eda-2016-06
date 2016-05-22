package testing;

import static org.junit.Assert.*;

import java.awt.RenderingHints.Key;
import java.util.Comparator;
import java.util.Random;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import structures.SimpleHashMap;
import structures.SimpleMap;

public class SimpleHashMapTest {

		
		public static SimpleMap<Integer, Integer> intMap;
		public static SimpleMap<String, String> stringMap;
		
		public static Random rand;
		
		public static final int CAPACITY = 10;
		public static int[] storedInts = new int[CAPACITY];
		public static int[] storedIntKeys = new int[CAPACITY];

		public static String[] storedStrings = new String[CAPACITY];
		

	    @BeforeClass
	    public static void initialize() {
	        intMap = new SimpleHashMap<>(10, new Comparator<Integer>() {

				@Override
				public int compare(Integer o1, Integer o2) {
					return o1.compareTo(o2);
				}
			});
	        
	        stringMap = new SimpleHashMap<String, String>(new Comparator<String>(){
	        	public int compare(String s1, String s2){
	        		return s1.compareTo(s2);
	        	}
	        });
	        		
	        rand = new Random(System.currentTimeMillis());
	        	
	        }
	        
	    

	    @Before
	    public void setUp() throws Exception {
	    	intMap.clear();
	    	stringMap.clear();
	    	
	    	assertTrue(intMap.isEmpty());
	    	assertTrue(stringMap.isEmpty());
	    	
	    }


	    @Test
	    public void testIntMap(){
	    	
	    	for(int i=0 ; i < CAPACITY ; i++){
	    		storedInts[i] = 1000*i + rand.nextInt()%1000;		//Asi son todos valores distintos
	    		storedIntKeys[i] = 100*i + rand.nextInt()%100; 		//Lo mismo
	    		
	    		intMap.put(storedIntKeys[i], storedInts[i]);
	    	}
	    	
	    	
	    	assertTrue(intMap.size() == CAPACITY);
	    	for(int i = 0; i < CAPACITY ; i++){
	    		  		
	    		assertTrue(intMap.containsKey(storedIntKeys[i]));
	    		assertTrue(intMap.containsValue(storedInts[i]));
	    		assertTrue(intMap.get(storedIntKeys[i]).equals(storedInts[i]));
	    	}
	    	
	    	int remK = storedIntKeys[0];
	    	int remV = storedInts[0];
	    	
	    	intMap.remove(remK);
	    	assertTrue(intMap.size() == CAPACITY - 1);
	    	
	    	assertFalse(intMap.containsKey(remK));
	    	assertFalse(intMap.containsValue(remV));
	    	
	    	//Actualizar un valor
	    	intMap.put(storedIntKeys[1], remV);
	    	assertTrue(intMap.containsKey(storedIntKeys[1]));
	    	assertTrue(intMap.containsValue(remV));
	    	
	    }
	    
	    
	    
	    
}
