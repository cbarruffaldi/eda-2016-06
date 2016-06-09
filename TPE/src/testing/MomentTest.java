package testing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.Day;
import utils.Moment;
import utils.Time;

public class MomentTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Moment m1 = new Moment(Day.LU, new Time("00:01"));
		Moment m2 = new Moment(Day.DO, new Time("23:59"));
		
		Time d12 = m1.howMuchUntil(m2);
		Time d21 = m2.howMuchUntil(m1);
		
		System.out.println();d12, new Time(10078));
		
	}

}
