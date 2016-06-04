package testing;


import java.util.List;
import java.util.Random;

import flightassistant.Airport;
import flightassistant.AirtimeWeighter;
import flightassistant.DijsktraForReal;
import flightassistant.Flight;
import flightassistant.FlightAssistant;
import flightassistant.InfinityDijkstra;
import flightassistant.PriceWeighter;
import flightassistant.Ticket;
import utils.Day;
import utils.Moment;
import utils.Time;

public class TestEm {
	static FlightAssistant fa;

	static String[] airports;
	static Random random;

	public static void setUpBeforeClass() throws Exception {
		fa = new FlightAssistant();

		airports = new String[]{"AAA", "BBB", "CCC", "DDD", "EEE", "FFF", "GGG", "HHH", "III",
										"JJJ", "KKK", "LLL", "MMM", "NNN", "OOO", "PPP", "QQQ", "RRR",
										"SSS", "TTT", "UUU", "VVV", "WWW", "XXX", "YYY", "ABC"};

		int i = 0;

		for(String s: airports){
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

	public void setUp() throws Exception {

		//fa.refreshAirportsNodeProperties();

	}

	static public void AirTimeTest() {

		int i = 0;
		int j = 0;

		while(i < 1){
		i++;
		Airport air1 = fa.airports.get(airports[random.nextInt(airports.length)]);
		Airport air2 = fa.airports.get(airports[random.nextInt(airports.length)]);

		fa.refreshAirportsNodeProperties();

		List<Ticket> l1 = DijsktraForReal.minPath(air1, air2, AirtimeWeighter.WEIGHTER);


		fa.refreshAirportsNodeProperties();

		List<Ticket> l2 = InfinityDijkstra.minPath(fa, air1, air2, AirtimeWeighter.WEIGHTER);

		if(l1 == null){
			if(l2  != null)
				System.out.println("Error");
		}

		int time1 = 0;
		for(Ticket f: l1){
			time1 += f.getDuration().getMinutes();
		}

		int time2 = 0;
		for(Ticket f: l2){
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

	public void priceTest() {

		int i = 0;
		while(i < 1){
		i++;
		Airport air1 = fa.airports.get(airports[random.nextInt(airports.length)]);
		Airport air2 = fa.airports.get(airports[random.nextInt(airports.length)]);

		fa.refreshAirportsNodeProperties();

		List<Ticket> l1 = DijsktraForReal.minPath(air1, air2, PriceWeighter.WEIGHTER);

		fa.refreshAirportsNodeProperties();

		List<Ticket> l2 = InfinityDijkstra.minPath(fa, air1, air2, PriceWeighter.WEIGHTER);


		double price1 = 0;
		for(Ticket f: l1){
			price1 += f.getPrice();
		}
		int price2 = 0;
		for(Ticket f: l2){
			price2 += f.getPrice();
		}

		if(! (price1 == price2)){
			System.out.println("ERR");
		}

		}

	}

	public static void main(String[] args) throws Exception {
		setUpBeforeClass();
		AirTimeTest();
	}

}