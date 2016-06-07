package flightassistant;

import structures.AVLMap;
import structures.AVLSet;
import structures.BinaryMinHeap;
import structures.SimpleMap;
import utils.ArrivalTimesFunction;
import utils.Day;
import utils.Moment;
import utils.Time;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


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
    BinaryMinHeap<ArrivalTimesFunction> pq;

    SimpleMap<String, Airport> airports;
    Airport origin;
    Airport dest;
    Day departure;

    Moment momentZero;

    //Unico dia
    public MinimumTime (SimpleMap<String, Airport> airports, Airport origin, Airport dest,
        Day departure) {
        if (airports == null) {
            throw new IllegalArgumentException("null");
        }
        this.airports = airports;
        this.origin = origin;
        this.dest = dest;
        this.departure = departure;
        momentZero = new Moment(departure, new Time(0, 0));

        this.functions = new AVLMap<Airport, ArrivalTimesFunction>(new Comparator<Airport>() {

            @Override public int compare (Airport o1, Airport o2) {
                return o1.getId().compareTo(o2.getId());
            }

        });

        pq = new BinaryMinHeap<>(airports.size());
    }

    public static void main (String[] args) {
        FlightAssistant fa = new FlightAssistant();
        fa.insertAirport("AAA", 1, 1);
        fa.insertAirport("BBB", 2, 19);
        fa.insertAirport("CCC", 12, 3);
        fa.insertAirport("DDD", 31,91);


        int i = 0;
        List<Moment> departures = new ArrayList<>();
        departures.add(new Moment(Day.LU, new Time(300)));
        fa.insertFlight("AB", i++, 1, departures, new Time(300), "AAA", "BBB");
        
        departures = new ArrayList<>();
        departures.add(new Moment(Day.LU, new Time(600)));
        fa.insertFlight("AB", i++, 1, departures, new Time(120), "AAA", "BBB");

        departures = new ArrayList<>();
        departures.add(new Moment(Day.LU, new Time(720)));
        fa.insertFlight("AD", i++, 1, departures, new Time(300), "AAA", "DDD");

        departures = new ArrayList<>();
        departures.add(new Moment(Day.LU, new Time(1050)));
        fa.insertFlight("DB", i++, 1, departures, new Time(30), "DDD", "BBB");

        departures = new ArrayList<>();
        departures.add(new Moment(Day.LU, new Time(660)));
        fa.insertFlight("AB", i++, 1, departures, new Time(300), "BBB", "CCC");

        departures = new ArrayList<>();
        departures.add(new Moment(Day.LU, new Time(780)));
        fa.insertFlight("BC", i++, 1, departures, new Time(540), "BBB", "CCC");

        departures = new ArrayList<>();
        departures.add(new Moment(Day.LU, new Time(1140)));
        fa.insertFlight("DC", i++, 1, departures, new Time(120), "DDD", "CCC");


        SimpleMap<String, Airport> airports = fa.getAirports();

        System.out.println(DynamicTimeWeighter.WEIGHTER.weight(airports.get("DDD"), airports.get("BBB"),
                new Moment(Day.LU, new Time(300))));

        new MinimumTime(airports, airports.get("AAA"), airports.get("CCC"), Day.LU).run();

    }

    private void run () {
        initialize();
        while (pq.size() > 1) {
            ArrivalTimesFunction g_i = pq.dequeue();

            Double minArrivalToAdj = pq.getPriority(pq.head());

            double delta = minWeightAt(minArrivalToAdj, g_i.airport());
            double bound = minArrivalToAdj + delta;

            double newRefined = g_i.getMaxBounded(bound);

            Airport a = g_i.airport();
            Iterator<Airport> iter = a.getConnectedAirports().iterator();

            while (iter.hasNext()) {
                Airport adjacent = iter.next();
                ArrivalTimesFunction adjFunction = functions.get(adjacent);

                Iterator<Double> domain = g_i.getDomain().higherIterator(g_i.refinedUpTo());
                Double t;
                System.out.println(newRefined);
                while (domain.hasNext() && (t=domain.next()) <= newRefined) {
                    adjFunction.minimizeValue(t, g_i.eval(t) + DynamicTimeWeighter.WEIGHTER
                        .weight(a, adjacent, momentZero.addTime(new Time(g_i.eval(t)))));
                }
                //TODO: Es ChangePriority, no decrease. Ya lo cambie
                pq.decreasePriority(adjFunction, adjFunction.eval(adjFunction.refinedUpTo()));
            }

            if(newRefined == g_i.refinedUpTo()){
            	continue;
            }
            g_i.setRefined(newRefined);
            if (g_i.refinedUpTo() >= g_i.getRightVal()) {
                System.out.println("Done!");
                if (g_i.airport().equals(dest)) {
                    System.err.println("DONE DONE");
                    return;
                }

            } else {
                pq.enqueue(g_i, g_i.eval(g_i.refinedUpTo()));
            }
        }
        System.err.println("Apparently pq size is 1");
        
        System.out.println("PR ================");
        functions.get(dest).print();
        System.out.println(functions.get(dest).getMin());

    }

    private double minWeightAt (Double minArrivalToAdj, Airport airport) {
        Iterator<Airport> iter = airport.getConnectedAirports().iterator();
        double min = Double.POSITIVE_INFINITY;

        Moment now = momentZero.addTime(new Time(minArrivalToAdj));

        double weight = Double.POSITIVE_INFINITY;
        while (iter.hasNext()) {
            Airport curr = iter.next();
            if (curr.flightExistsTo(airport)) {
                if (minArrivalToAdj < 24 * 60 && curr.equals(origin)) { //Minutes_in_a_day
                    weight = new OriginDynamicWeighter(departure).weight(curr, airport, now);
                } else {
                    weight = DynamicTimeWeighter.WEIGHTER.weight(curr, airport, now);
                }

                min = weight < min ? weight : min;
            }
        }

        System.err.println(min);
        return min;
    }

    private void initialize () {

        AVLSet<Double> times = origin.getFlightTimes(departure);
        //TODO: Empty
        
        Iterator<Airport> airs = airports.valueIterator();
        Airport curr;
        while (airs.hasNext()) {
            curr = airs.next();
            ArrivalTimesFunction g = new ArrivalTimesFunction(curr, times);
            functions.put(curr, g);
        }

        //La funcion para origen arranca g(t) = t
        ArrivalTimesFunction originF = functions.get(origin);
        for (double t : originF.getDomain()) {
            originF.updateValue(t, t); //g_e(t) = t
        }

        Iterator<ArrivalTimesFunction> iter = functions.valueIterator();

        while (iter.hasNext()) {
            ArrivalTimesFunction g = iter.next();
            pq.enqueue(g, g.eval(g.refinedUpTo()));
        }

        refineOrigin(); //Debe hacerse al principio por el tema de que el vuelo de partida debe caer
        //en el dia especificado
    }

    private void refineOrigin () {
        ArrivalTimesFunction deq = pq.dequeue(); //Origin
        OriginDynamicWeighter weighter = new OriginDynamicWeighter(departure);
        //PQNode head = pq.head();
        Airport a = deq.airport();
        Iterator<Airport> iter = a.getConnectedAirports().iterator();
        while (iter.hasNext()) {
            Airport adjacent = iter.next();
            Iterator<Double> domain = deq.getDomain().iterator();
            ArrivalTimesFunction adjFunction = functions.get(adjacent);
            while (domain.hasNext()) {
                Double t = domain.next();
                adjFunction.minimizeValue(t,
                    deq.eval(t) + weighter.weight(a, adjacent, new Moment(departure, new Time(t))));
            }
            pq.decreasePriority(adjFunction, adjFunction.eval(adjFunction.refinedUpTo()));
        }

        deq.print();
        pq.head().print();
    }



}
