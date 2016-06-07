package flightassistant;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.DelayQueue;

import structures.AVLMap;
import structures.AVLSet;
import structures.BinaryMinHeap;
import structures.BinaryMinHeap2;
import utils.ArrivalTimesFunction;
import utils.Day;
import utils.Moment;
import utils.Time;


//TODO: Domain deberia estar afuera como una lista o algo. Mismo los bounds
public class MinimumTime {
	
//	
//	private static class PQNode implements Comparable<PQNode>{
//		ArrivalTimesFunction g;
//		Double tau;
//		
//		public PQNode(ArrivalTimesFunction function, Double intervalStart){
//			g = function;
//			tau = intervalStart;
//		}
//		
//		public double priority(){
//			return g.eval(tau);
//		}
//		
//		@Override
//		public int compareTo(PQNode o) {
//			return Double.compare(g.eval(tau), o.g.eval(o.tau));
//		}
//		
//		
//		public boolean equals(Object o){
//			return g.equals(o);
//		}
//		
//		public int hashCode(){
//			return g.hashCode();
//		}
//		
//		public Airport airport(){
//			return g.airport();
//		}
//	}
	
	AVLMap<Airport, ArrivalTimesFunction> functions;
	BinaryMinHeap2<ArrivalTimesFunction> pq; 
	
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
		
		pq = new BinaryMinHeap2<>(fa.airports.size());
	}
	
	private void run() {
		initialize();
		while(pq.size() > 1){
			ArrivalTimesFunction g_i = pq.dequeue();
			System.err.println("Dequeuing" + g_i.airport());
			Double minArrivalToAdj = pq.getPriority(pq.head());
			
			double delta = minWeightAt(minArrivalToAdj, g_i.airport());
			double bound = minArrivalToAdj + delta;
			
			double newRefined = g_i.getMaxBounded(bound);
			
			//TODO: Abstract weighter?
			
			Moment momentZero = new Moment(departure, new Time(0,0));
			Airport a = g_i.airport(); 
			Iterator<Airport> iter = a.getConnectedAirports().iterator();
			while(iter.hasNext()){
				Airport adjacent = iter.next();
				Iterator<Double> domain = g_i.getDomain().higherIterator(newRefined);
				ArrivalTimesFunction adjFunction = functions.get(adjacent);
				while(domain.hasNext()){
					Double t = domain.next();
					adjFunction.minimizeValue(t, t + 
							DynamicTimeWeighter.WEIGHTER.weight(a, adjacent, momentZero.addTime(new Time(t))));
				}
				pq.decreasePriority(adjFunction, adjFunction.eval(adjFunction.refinedUpTo()));	
			}

			g_i.setRefined(newRefined);
			if(g_i.refinedUpTo() >= g_i.getRightVal()){
				System.out.println("Done!");
				if(g_i.airport().equals(dest)){
					System.err.println("DONE DONE");
					return;
				}
				
			}
		}
		System.err.println("Apparently pq size is 1");
		pq.head().print();
		
	}

	private double minWeightAt(Double minArrivalToAdj, Airport airport) {
		Iterator<Airport> iter = airport.getConnectedAirports().iterator();
		double min = Double.POSITIVE_INFINITY;
		
		Moment momentZero = new Moment(departure, new Time(0,0));
		Moment now = momentZero.addTime(new Time(minArrivalToAdj));
		
		while(iter.hasNext()){
			Airport curr = iter.next();
			if(curr.flightExistsTo(airport)){
				double weight = DynamicTimeWeighter.WEIGHTER.weight(curr, airport, now);
				min = weight < min ? weight : min;
				}
		}
		
		System.err.println(min);
		return min;			
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
		
		//La funcion para origen arranca g(t) = t
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
				adjFunction.minimizeValue(t, t + weighter.weight(a, adjacent, new Moment(departure, new Time(t))));
			}
			pq.decreasePriority(adjFunction, adjFunction.eval(adjFunction.refinedUpTo()));	
		}
		
		deq.print();
		pq.head().print();
	}
	
	
	

	public static void main(String[] args) {
		FlightAssistant fa = new FlightAssistant();
		fa.insertAirport("AAA", 1, 1);
		fa.insertAirport("BBB", 1, 1);
		fa.insertAirport("CCC", 1, 1);
		fa.insertAirport("DDD", 1, 1);

		
		int i=0;
		List<Moment> departures = new ArrayList<>();
		departures.add(new Moment(Day.LU, new Time(300)));
		fa.insertFlight("AB", i++, 1, departures, new Time(300), "AAA" , "BBB");

		departures = new ArrayList<>();
		departures.add(new Moment(Day.LU, new Time(600)));
		fa.insertFlight("AB", i++, 1, departures, new Time(120), "AAA" , "BBB");

		departures = new ArrayList<>();
		departures.add(new Moment(Day.LU, new Time(720)));
		fa.insertFlight("AB", i++, 1, departures, new Time(300), "AAA" , "DDD");

		departures = new ArrayList<>();
		departures.add(new Moment(Day.LU, new Time(1050)));
		fa.insertFlight("DB", i++, 1, departures, new Time(30), "DDD" , "BBB");

		departures = new ArrayList<>();
		departures.add(new Moment(Day.LU, new Time(660)));
		fa.insertFlight("AB", i++, 1, departures, new Time(120), "BBB" , "CCC");

		departures = new ArrayList<>();
		departures.add(new Moment(Day.LU, new Time(780)));
		fa.insertFlight("AB", i++, 1, departures, new Time(540), "BBB" , "CCC");
		
		departures = new ArrayList<>();
		departures.add(new Moment(Day.LU, new Time(1140)));
		fa.insertFlight("AB", i++, 1, departures, new Time(120), "DDD" , "CCC");


		
		
		System.out.println(DynamicTimeWeighter.WEIGHTER.weight(fa.airports.get("AAA"), fa.airports.get("BBB"), new Moment(Day.LU, new Time(10,00))));
		
		new MinimumTime(fa, fa.airports.get("AAA"), fa.airports.get("CCC"), Day.LU).run();
		
		

		
	}

	
	
}
