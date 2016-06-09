package testing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.Day;
import utils.Moment;
import utils.Time;
import utils.TimeConstants;;
public class MomentTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}
	

	@Test
	public void testHowMuchTime() {
		Moment m1 = new Moment(Day.LU, new Time("00:01"));
		Moment m2 = new Moment(Day.DO, new Time("23:59"));
		
		Time d12 = m1.howMuchUntil(m2);
		Time d21 = m2.howMuchUntil(m1);
		
		assertEquals(d12, new Time(10078));
		assertEquals(d21, new Time(2));
		assertEquals(m2.howMuchUntil(m2), new Time(0));
		
		m1 = new Moment(Day.MA, new Time("00:00"));
		m2 = new Moment(Day.MI, new Time("00:00"));
		assertEquals(m1.howMuchUntil(m2), new Time(TimeConstants.MINUTES_PER_DAY));
		
		d21 = m2.howMuchUntil(m1);
		Time delta = new Time(TimeConstants.MINUTES_PER_WEEK - TimeConstants.MINUTES_PER_DAY);
		assertEquals(d21, delta);
		
	}
	
	@Test
	public void testAddTime() {
		Moment m1, m2, m3;
		
		m1 = new Moment(Day.LU, new Time("00:10"));
		m2 = m1.addTime(new Time(120));
		m3 = new Moment(Day.LU, new Time("2:10"));
		
		assertEquals(m2,m3);
		
		
		m1 = new Moment(Day.DO, new Time("12:00"));
		m2 = m1.addTime(new Time(TimeConstants.MINUTES_PER_DAY * 3 
				+ TimeConstants.MINUTES_PER_HOUR * 5 + 30));
		m3 = new Moment(Day.MI, new Time("17:30"));
		
		assertEquals(m2,m3);
		
		//howMuchUntil() coherente con addTime()
		m3 = m2.addTime(m2.howMuchUntil(m1));
		assertEquals(m1,m3);
		
		
		m3 = m1.addTime(m1.howMuchUntil(m2));
		assertEquals(m2,m3);

		
		m1 = new Moment(Day.LU, new Time("00:01"));
		m2 = new Moment(Day.DO, new Time("23:59"));
		
		m3 = m1.addTime(m1.howMuchUntil(m2));
		
		assertEquals(m3, m2);
		
		
	}
	



	
}
