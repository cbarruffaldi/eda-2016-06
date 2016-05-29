package flightassistant;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.Day;
import utils.Moment;
import utils.Time;

public class DijsktraTest {

	static FlightAssistant fa;
	static Airport A;
	static Airport B;
	static Airport C;
	static Airport D;
	static Airport E;
	static Airport F;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		fa = new FlightAssistant();
		fa.insertAirport("AAA", 1, 1);
		fa.insertAirport("BBB", 2, 2);
		fa.insertAirport("CCC", 3, 3);
		fa.insertAirport("DDD", 4, 4);
		fa.insertAirport("EEE", 4, 4);
		fa.insertAirport("FFF", 5, 5);

		Moment[] departures = new Moment[]{ new Moment(Day.LU, new Time(12,00)) };

		
		fa.insertFlight("AB", 1, 1000, departures, new Time(2,0), "AAA", "BBB");
		fa.insertFlight("AB", 2, 1500, departures, new Time(1,30), "AAA", "BBB");
		fa.insertFlight("AB", 3, 550, departures, new Time(4,0), "AAA", "BBB");
		fa.insertFlight("AC", 1, 3000, departures, new Time(6,10), "AAA", "CCC");
		fa.insertFlight("BC", 1, 500, departures, new Time(6,0), "BBB", "CCC");
		fa.insertFlight("CD", 1, 300, departures, new Time(1,40), "CCC", "DDD");
		fa.insertFlight("BD", 1, 1500, departures, new Time(2,20), "BBB", "DDD");
		fa.insertFlight("DE", 1, 300, departures, new Time(4,05), "DDD", "EEE");
		fa.insertFlight("CE", 1, 2000, departures, new Time(3,20), "CCC", "EEE");
		fa.insertFlight("CF", 1, 900, departures, new Time(10,0), "CCC", "FFF");
		fa.insertFlight("EF", 1, 300, departures, new Time(2,20), "EEE", "FFF");
		fa.insertFlight("EF", 2, 1000, departures, new Time(0,40), "EEE", "FFF");
		

		A = fa.airports.get("AAA");
		B = fa.airports.get("BBB");
		C = fa.airports.get("CCC");
		D = fa.airports.get("DDD");
		E = fa.airports.get("EEE");
		F = fa.airports.get("FFF");
	}

	@Before
	public void setUp() throws Exception {
		A.unvisit();
		B.unvisit();
		C.unvisit();
		D.unvisit();
		E.unvisit();
		F.unvisit();
		
		A.setIncident(null);
		B.setIncident(null);
		C.setIncident(null);
		D.setIncident(null);
		E.setIncident(null);
		F.setIncident(null);
		
		A.setWeight(Double.MAX_VALUE);
		B.setWeight(Double.MAX_VALUE);
		C.setWeight(Double.MAX_VALUE);
		D.setWeight(Double.MAX_VALUE);
		E.setWeight(Double.MAX_VALUE);
		F.setWeight(Double.MAX_VALUE);
	}

	@Test
	public void test() {
		assertEquals(new FlightId("AB",2), A.getQuickestTo(B).getId());
		assertEquals(new FlightId("AB", 3), A.getCheapestTo(B).getId());
		assertEquals(new FlightId("EF", 1), E.getCheapestTo(F).getId());
		assertTrue(A.routeExistsTo(B));
		assertTrue(A.flightExistsTo(B));
		assertFalse(B.flightExistsTo(B));
		assertFalse(B.flightExistsTo(A));
		assertTrue(E.flightExistsTo(F));
		
	}

	@Test
	public void dijkstraAD(){
		
		//AB3 -> BC1 -> CD1
		List<Flight> list = DijsktraForReal.minPath(A,D,new PriceWeighter());
		
		assertNotNull(list);
		assertTrue(list.size() == 3);
		assertEquals(new FlightId("AB",3), list.get(0).getId());
		assertEquals(new FlightId("BC",1), list.get(1).getId());
		assertEquals(new FlightId("CD",1), list.get(2).getId());
		
	}
	
	@Test
	public void dijkstraAD2(){
		//AB2 -> BD 1
		List<Flight >list = DijsktraForReal.minPath(A,D,new AirtimeWeighter());

		assertNotNull(list);
		assertTrue(list.size() == 2);
		assertEquals(new FlightId("AB",2), list.get(0).getId());
		assertEquals(new FlightId("BD",1), list.get(1).getId());
	}
}
