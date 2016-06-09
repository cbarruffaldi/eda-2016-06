package testing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.Time;

public class TimeTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void correctInitTest() {
		Time t1 = new Time(10.5);
		Time t2 = new Time(10.7);
		
		Time t3 = new Time("10:10");
		Time t4 = new Time("42");
		Time t4b = new Time(42);
		Time t5 = new Time("102:402");
		Time t6 = new Time(102,402);
		
		assertTrue(t1.equals(t2));
		assertTrue(t4.equals(t4b));
		assertTrue(t5.equals(t6));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void incorrectIni1() {
		new Time(-10);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void incorrectIni2() {
		new Time("-000");
	}


	
	@Test
	public void addingTime(){
		Time t0 = new Time(1,30);
		Time t1 = new Time(1,30);
		
		Time t2 = t1.addMinutes(120);
		assertEquals(new Time("3:30"), t2);
		
		Time t3 = t1.addTime(t1);
		assertEquals(new Time(3,0), t3);
		
		
		assertEquals(t0, t1); //t1 nunca cambió
		
	
	}
	

	@Test
	public void compareTest(){
		Time t0 = new Time(1,30);
		Time t1 = new Time(1,30);
		
		Time t2 = t1.addMinutes(120);
		assertTrue(t2.compareTo(t1) > 0);
		
	
	}
	

}
