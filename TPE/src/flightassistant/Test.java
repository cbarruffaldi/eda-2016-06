package flightassistant;


import java.util.List;

import utils.Day;
import utils.Moment;
import utils.Time;

public class Test {
	public static void main(String[] args) {
		FlightAssistant fa = new FlightAssistant();
		fa.insertAirport("AAA", 1, 1);
		fa.insertAirport("BBB", 2, 2);
		fa.insertAirport("CCC", 3, 3);
		fa.insertAirport("DDD", 4, 4);
		fa.insertAirport("EEE", 4, 4);
		fa.insertAirport("FFF", 5, 5);

		Moment[] departures = new Moment[]{ new Moment(Day.LU, new Time(12,00)) };

		
		fa.insertFlight("AB", 1, 1000, departures, new Time(2,0), "AAA", "BBB");
		fa.insertFlight("AB", 2, 1500, departures, new Time(1,30), "AAA", "BBB");
		fa.insertFlight("AB", 3, 750, departures, new Time(4,0), "AAA", "BBB");
		fa.insertFlight("AC", 1, 3000, departures, new Time(6,10), "AAA", "CCC");
		fa.insertFlight("BC", 1, 500, departures, new Time(6,0), "BBB", "CCC");
		fa.insertFlight("CD", 1, 300, departures, new Time(1,40), "CCC", "DDD");
		fa.insertFlight("BD", 1, 1500, departures, new Time(2,20), "BBB", "DDD");
		fa.insertFlight("DE", 1, 300, departures, new Time(4,05), "DDD", "EEE");
		fa.insertFlight("CE", 1, 2000, departures, new Time(3,20), "CCC", "EEE");
		fa.insertFlight("CF", 1, 900, departures, new Time(10,0), "CCC", "FFF");
		fa.insertFlight("EF", 1, 300, departures, new Time(2,20), "EEE", "FFF");
		fa.insertFlight("EF", 2, 1000, departures, new Time(0,40), "EEE", "FFF");
		

		Airport A = fa.airports.get("AAA");
		Airport B = fa.airports.get("BBB");
		Airport C = fa.airports.get("CCC");
		Airport D = fa.airports.get("DDD");
		Airport E = fa.airports.get("EEE");
		Airport F = fa.airports.get("FFF");

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

		
		System.err.println(B.getDestinations().contains(A));
		
	//	System.out.println(A.getQuickestTo(B));
//		System.out.println(A.getCheapestTo(B));
//
//		System.out.println(A.routeExistsTo(B));
//		System.out.println(A.routeExistsTo(C));
//		System.out.println(A.routeExistsTo(D));
//		System.out.println(E.routeExistsTo(F));
		
		List<Flight> list = DijsktraForReal.minPath(A,F,new AirtimeWeighter());

		if(list == null)
			System.out.println("null");
		else{
		for(Flight f: list){
			System.out.println(f);
		}
		}
	
	}
}
