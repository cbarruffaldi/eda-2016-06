package flightassistant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.Day;
import utils.Moment;
import utils.Time;

public class DobleDijkstraTest {
	static FlightAssistant fa;
	static Airport A;
	static Airport B;
	static Airport C;
	static Airport D;
	static Airport E;
	static Airport F;
	
	static String[] airports;
	static Random random;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		fa = new FlightAssistant();

		airports = new String[]{"AAA", "BBB", "CCC", "DDD", "EEE", "FFF", "GGG", "HHH", "III", 
										"JJJ", "KKK", "LLL", "MMM", "NNN", "OOO", "PPP", "QQQ", "RRR",
										"SSS", "TTT", "UUU", "VVV", "WWW", "XXX", "YYY", "ZZZ"};
		
		int i = 0;
		
		for(String s: airports){
			i+=1;
			fa.insertAirport(s, i, i);
		}
		
		random = new Random(System.currentTimeMillis());
		
		i = 0;
		while(i < 1000){
			i++;
			
			String air1 = airports[random.nextInt(airports.length)];
			
			String air2 = null;
			
			do{
				air2 = airports[random.nextInt(airports.length)];
			} while(air2.equals(air1));
			
			
			double price = random.nextInt(15500) + 1000; //entre 1000 y 15500
			Time duration = new Time(random.nextInt(1000));
			int hour = random.nextInt(24);
			int minutes = random.nextInt(60);
			
			Moment[] departures = new Moment[]{ new Moment(Day.LU, new Time(hour, minutes)) };
			
			fa.insertFlight("UAI", i, price, departures, duration, air1, air2);
		}
		
		
	}

	@Before
	public void setUp() throws Exception {
		
		fa.refreshAirportsNodeProperties();
		
	}

	@Test
	public void AirTimeTest() {
		
		int i = 0;
		int j = 0;
		
		while(i < 100){
		i++;
		Airport air1 = fa.airports.get(airports[random.nextInt(airports.length)]);
		Airport air2 = fa.airports.get(airports[random.nextInt(airports.length)]);
		
		fa.refreshAirportsNodeProperties();

		List<Flight> l1 = DijsktraForReal.minPath(air1, air2, AirtimeWeighter.WEIGHTER);
		
		
		fa.refreshAirportsNodeProperties();

		List<Flight> l2 = InfinityDijkstra.minPath(fa, air1, air2, AirtimeWeighter.WEIGHTER);
		
		if(l1 == null){
			assertTrue(l2  == null);
		}
		
		int time1 = 0;
		for(Flight f: l1){
			time1 += f.getDuration().getMinutes();
		}
		
		int time2 = 0;
		for(Flight f: l2){
			time2 += f.getDuration().getMinutes();
		}
				
		if(time2 < time1){
			System.out.println("========");
			System.out.println("Mejor el 2");
			System.out.println(time1);
			System.out.println(time2);
			System.out.println("========");

			j += 1;
		}
		if(time1 < time2){
			System.out.println("========");
			System.out.println("Mejor el 1");
			System.out.println(time1);
			System.out.println(time2);
			System.out.println("========");

			j += 1;
		}		
				
		}
		System.out.println(j);

	}
	
	@Test
	public void priceTest() {
		
		int i = 0;
		while(i < 1000000){
		i++;
		Airport air1 = fa.airports.get(airports[random.nextInt(airports.length)]);
		Airport air2 = fa.airports.get(airports[random.nextInt(airports.length)]);
		
		fa.refreshAirportsNodeProperties();

		List<Flight> l1 = DijsktraForReal.minPath(air1, air2, PriceWeighter.WEIGHTER);
		fa.refreshAirportsNodeProperties();

		List<Flight> l2 = InfinityDijkstra.minPath(fa, air1, air2, PriceWeighter.WEIGHTER);
		
		if(l1 == null){
			assertTrue(l2  == null);
		}
		
		double price1 = 0;
		for(Flight f: l1){
			price1 += f.getPrice();
		}
		int price2 = 0;
		for(Flight f: l2){
			price2 += f.getPrice();
		}
		
		assertTrue(price1 == price2);
		
		}

	}


	
}