package flightassistant;

//public class DobleDijkstraTest {
//	static FlightAssistant fa;
//
//	static String[] airports;
//	static Random random;
//
//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//		fa = new FlightAssistant();
//
//		airports = new String[]{"AAA", "BBB", "CCC", "DDD", "EEE", "FFF", "GGG", "HHH", "III",
//								"JJJ", "KKK", "LAL", "MIM", "NNN", "OOO", "PAL", "QNM", "RRR",
//								"SSS", "TTT", "UTD", "VVV", "WWW", "XXX", "YYY", "ZQB"};
//
//		int i = 0;
//
//		for(String s: airports){
//			fa.insertAirport(s, i, i);
//		}
//
//		random = new Random(System.currentTimeMillis());
//
//		i = 0;
//		while(i < 500){
//			i++;
//
//			String air1 = airports[random.nextInt(airports.length)];
//
//			String air2 = null;
//
//			do{
//				air2 = airports[random.nextInt(airports.length)];
//			} while(air2.equals(air1));
//
//
//			double price = random.nextInt(15500); //entre 1000 y 15500
//			Time duration = new Time(1 + random.nextInt(1000));
//			int hour = random.nextInt(24);
//			int minutes = random.nextInt(60);
//
//			Moment[] departures = new Moment[]{ new Moment(Day.LU, new Time(hour, minutes)) };
//
//			Character c1 = air1.toCharArray()[0];
//			Character c2 = air2.toCharArray()[0];
//
//			String s = "";
//			s = s + c1 + c2 + i;
//
//			fa.insertFlight(s, i, price, departures, duration, air1, air2);
//		}
//
//
//	}
//
//	@Before
//	public void setUp() throws Exception {
//
//		//fa.refreshAirportsNodeProperties();
//
//	}
//
//	@Test
//	public void AirTimeTest() {
//
//		int i = 0;
//		int j = 0;
//
//		while(i < 10000){
//		i++;
//		Airport air1 = fa.airports.get(airports[random.nextInt(airports.length)]);
//		Airport air2 = fa.airports.get(airports[random.nextInt(airports.length)]);
//
//		fa.refreshAirportsNodeProperties();
//
//		List<Ticket> l1 = DijsktraForReal.minPath(air1, air2, AirtimeWeighter.WEIGHTER);
//
//
//		fa.refreshAirportsNodeProperties();
//
//		List<Ticket> l2 = InfinityDijkstra.minPath(fa, air1, air2, AirtimeWeighter.WEIGHTER);
//
//		if(l1 == null){
//			assertTrue(l2  == null);
//		}
//
//
//		if(l1 != null && l2 != null){
//		int time1 = 0;
//		for(Ticket f: l1){
//			time1 += f.getDuration().getMinutes();
//		}
//
//		int time2 = 0;
//		for(Ticket f: l2){
//			time2 += f.getDuration().getMinutes();
//		}
//
//
//		if(time2 < time1){
//			System.out.println("========");
//			System.out.println("Mejor el 2");
//			System.out.println(time1);
//			System.out.println(time2);
//			System.out.println("========");
//
//				System.out.print(air1 + "  ->");
//				System.out.println(air2);
//
//				System.out.println(l1);
//				for(Ticket f: l1){
//					if(f.getDuration().getMinutes() <= 0)
//						System.err.println("ZERO!L2");
//				}
//				for(Ticket f: l2){
//					if(f.getDuration().getMinutes() <= 0)
//						System.err.println("ZERO! L2");
//				}
//
//				System.out.println(l2);
//
//
//				Iterator<Flight> iter = fa.flights.valueIterator();
//				while(iter.hasNext())
//					System.out.println(iter.next());
//
//
//
//		}
//		if(time1 < time2){
//			System.out.println("========");
//			System.out.println("Mejor el 1");
//			System.out.println(time1);
//			System.out.println(time2);
//			System.out.println("========");
//
//				System.out.print(air1 + "  ->");
//				System.out.println(air2);
//				System.out.println(l1);
//				System.out.println(l2);
//
//			j += 1;
//		}
//
//
//		assertTrue(time1 == time2);
//		}
//		}
//
//	}
//
//	@Test
//	public void priceTest() {
//
//		int i = 0;
//		while(i < 10000){
//		i++;
//		Airport air1 = fa.airports.get(airports[random.nextInt(airports.length)]);
//		Airport air2 = fa.airports.get(airports[random.nextInt(airports.length)]);
//
//		fa.refreshAirportsNodeProperties();
//
//		List<Ticket> l1 = DijsktraForReal.minPath(air1, air2, PriceWeighter.WEIGHTER);
//
//		fa.refreshAirportsNodeProperties();
//
//		List<Ticket> l2 = InfinityDijkstra.minPath(fa, air1, air2, PriceWeighter.WEIGHTER);
//
//		if(l1 == null){
//			assertTrue(l2  == null);
//		}
//
//		if(l1 != null && l2 != null){
//		double price1 = 0;
//		for(Ticket f: l1){
//			price1 += f.getPrice();
//		}
//		int price2 = 0;
//		for(Ticket f: l2){
//			price2 += f.getPrice();
//		}
//		if(price1 != price2){
//			System.out.println("Price diff " + price1 + " " + price2);
//
//			Iterator<Flight> iter = fa.flights.valueIterator();
//			while(iter.hasNext())
//				System.out.println(iter.next());
//		}
//		assertTrue(price1 == price2);
//		}
//
//		}
//
//	}
//
//
//}