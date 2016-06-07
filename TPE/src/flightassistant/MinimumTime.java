package flightassistant;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import structures.AVLMap;
import structures.AVLSet;
import structures.BinaryMinHeap;
import utils.ArrivalTimesFunction;
import utils.Day;
import utils.Moment;
import utils.Time;

public class MinimumTime {
	
	
	private static class PQNode implements Comparable<PQNode>{
		ArrivalTimesFunction g;
		Double tau;
		
		public PQNode(ArrivalTimesFunction function, Double intervalStart){
			g = function;
			tau = intervalStart;
		}
		
		public double priority(){
			return g.eval(tau);
		}
		
		@Override
		public int compareTo(PQNode o) {
			return Double.compare(g.eval(tau), o.g.eval(o.tau));
		}
		
		
		public boolean equals(Object o){
			return g.equals(o);
		}
		
		public int hashCode(){
			return g.hashCode();
		}
		
		public Airport airport(){
			return g.airport();
		}
	}
	
	AVLMap<Airport, ArrivalTimesFunction> functions;
	BinaryMinHeap<ArrivalTimesFunction> pq; 
	
	FlightAssistant fa;
	Airport origin;
	Airport dest;
	Day departure;
	
	//Unico dia
	public MinimumTime(FlightAssistant fa, Airport origin, Airport dest, Day departure){
		if(fa == null){
			throw new IllegalArgumentException("null");
		}
		this.fa = fa;
		this.origin = origin;
		this.dest = dest;
		this.departure = departure;
		this.functions = new AVLMap<Airport, ArrivalTimesFunction>(new Comparator<Airport>() {

			@Override
			public int compare(Airport o1, Airport o2) {
				return o1.getId().compareTo(o2.getId());
			}
			
		});
		
		pq = new BinaryMinHeap<>(fa.airports.size());
		initialize();
	}
	
	private void initialize(){
		
		AVLSet<Double> times = origin.getFlightTimes(departure);
		
		Iterator<Airport> airs = fa.airports.valueIterator();
		Airport curr;
		while(airs.hasNext()){
			curr = airs.next();
			ArrivalTimesFunction g = new ArrivalTimesFunction(curr, times);
			functions.put(curr, g);
		}
		
		ArrivalTimesFunction originF = functions.get(origin);
		for(double t: originF.getDomain()){
			originF.updateValue(t, t); //g_e(t) = t
		}
		
		Iterator<ArrivalTimesFunction> iter = functions.valueIterator();
		
		while(iter.hasNext()){
			ArrivalTimesFunction g = iter.next();
			pq.enqueue(g, g.eval(g.refinedUpTo()));
		}
		
		refineOrigin(); //Debe hacerse al principio por el tema de que el vuelo de partida debe caer
						//en el dia especificado
	}
	
	
	
	
	private void refineOrigin() {
		ArrivalTimesFunction deq = pq.dequeue(); //Origin
		OriginDynamicWeighter weighter = new OriginDynamicWeighter(departure);
		//PQNode head = pq.head();
		Airport a = deq.airport(); 
		Iterator<Airport> iter = a.getConnectedAirports().iterator();
		while(iter.hasNext()){
			Airport adjacent = iter.next();
			Iterator<Double> domain = deq.getDomain().iterator();
			ArrivalTimesFunction adjFunction = functions.get(adjacent);
			while(domain.hasNext()){
				Double t = domain.next();
				adjFunction.minimizeValue(t, weighter.weight(a, adjacent, new Moment(departure, new Time(t.intValue()))));
			}
			pq.decreasePriority(adjFunction, adjFunction.eval(adjFunction.refinedUpTo()));	
		}
		pq.dequeue().print();
		pq.dequeue().print();
	}

	public static void main(String[] args) {
		FlightAssistant fa = new FlightAssistant();
		fa.insertAirport("AAA", 1, 1);
		fa.insertAirport("BBB", 1, 1);
		fa.insertAirport("CCC", 1, 1);
		fa.insertAirport("DDD", 1, 1);

		
		int i=0;
		List<Moment> departures = new ArrayList<>();
		departures.add(new Moment(Day.LU, new Time(10, 0)));
		departures.add(new Moment(Day.MI, new Time(10, 0)));
		departures.add(new Moment(Day.JU, new Time(10, 0)));
		
		fa.insertFlight("AB", i++, 1, departures, new Time(1,0), "AAA" , "BBB");
		
		departures = new ArrayList<>();
		departures.add(new Moment(Day.LU, new Time(11, 0)));
		departures.add(new Moment(Day.SA, new Time(11, 0)));
		departures.add(new Moment(Day.DO, new Time(11, 0)));

		fa.insertFlight("AB", i++, 1, departures, new Time(2,0), "AAA", "BBB");
		
		
		departures = new ArrayList<>();
		departures.add(new Moment(Day.LU, new Time(9, 0)));
		departures.add(new Moment(Day.MA, new Time(14, 0)));
		departures.add(new Moment(Day.DO, new Time(11, 0)));

		fa.insertFlight("AB", i++, 1, departures, new Time(2,0), "AAA" , "CCC");
	
		System.out.println(DynamicTimeWeighter.WEIGHTER.weight(fa.airports.get("AAA"), fa.airports.get("BBB"), new Moment(Day.MI, new Time(9,00))));
		
		new MinimumTime(fa, fa.airports.get("AAA"), fa.airports.get("BBB"), Day.LU);
		
		

		
	}

	
	
}
