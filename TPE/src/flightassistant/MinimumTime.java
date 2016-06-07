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
	BinaryMinHeap<PQNode> pq; 
	
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
		
		PQNode node;
		while(iter.hasNext()){
			ArrivalTimesFunction g = iter.next();
			node = new PQNode(g, g.getLeftVal()); //Horrible pero necesario (ponele)
			pq.enqueue(node, node.priority());
		}
		
		refineOrigin(); //Debe hacerse al principio por el tema de que el vuelo de partida debe caer
						//en el dia especificado
	}
	
	
	
	
	private void refineOrigin() {
		PQNode deq = pq.dequeue();
		//PQNode head = pq.head();
		Airport a = deq.airport();
		Iterator<Airport> iter = a.getConnectedAirports().iterator();
		while(iter.hasNext()){
			Airport adjacent = iter.next();
			Iterator<Double> domain = deq.g.getDomain().iterator();
			
		}
		
		
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
		departures.add(new Moment(Day.LU, new Time(14, 0)));
		departures.add(new Moment(Day.SA, new Time(14, 0)));
		departures.add(new Moment(Day.DO, new Time(14, 0)));

		fa.insertFlight("AB", i++, 1, departures, new Time(2,0), "AAA", "BBB");
		
		
		departures = new ArrayList<>();
		departures.add(new Moment(Day.LU, new Time(11, 0)));
		departures.add(new Moment(Day.MA, new Time(14, 0)));
		departures.add(new Moment(Day.DO, new Time(11, 0)));

		fa.insertFlight("AB", i++, 1, departures, new Time(2,0), "AAA" , "BBB");
	
		System.out.println(DynamicTimeWeighter.WEIGHTER.weight(fa.airports.get("AAA"), fa.airports.get("BBB"), new Moment(Day.MI, new Time(9,00))));
		
		new MinimumTime(fa, fa.airports.get("AAA"), fa.airports.get("BBB"), Day.LU);
		
		

		
	}

	
	
}
