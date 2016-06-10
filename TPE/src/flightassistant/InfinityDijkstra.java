package flightassistant;

import structures.BinaryMinHeap;
import structures.SimpleMap;
import utils.Day;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class InfinityDijkstra {

    public static List<Ticket> minPath (SimpleMap<String, Airport> airports, Airport origin,
        Airport dest, Weighter weighter) {
        return minPath(airports, origin, dest, weighter, null, null);
    }

    public static List<Ticket> minPath (SimpleMap<String, Airport> airports, Airport origin,
        Airport dest, Weighter weighter, Weighter originWeighter, List<Day> days) {

        BinaryMinHeap<Airport> pq = queueAirports(airports);

        pq.decreasePriority(origin, 0);  // El orígen queda al tope de la cola

        if (originWeighter != null) {
            findPath(pq, dest, originWeighter, days, true, Double.POSITIVE_INFINITY);
        }

        Box b = findPath(pq, dest, weighter, null, false, Double.POSITIVE_INFINITY);
        return (b != null) ? b.list : new LinkedList<>(); // lista vacia si no enontró camino
    }

    public static List<Ticket> minPathTotalTime (FlightAssistant fa, Airport origin,
        Airport dest, List<Day> days) {

    	// Se crea una lista con todos los días de la semana si no desea viajar un día especifico
    	if (days.isEmpty()) {
    		days.add(Day.LU);
    		for (Day day = Day.MA; !day.equals(Day.LU); day = day.getNextDay())
    			days.add(day);
    	}

        BinaryMinHeap<Airport> pq;
        
        List<Ticket> bestPath = null; // Mejor camino encontrado
        double bestWeight = Double.POSITIVE_INFINITY;  // Peso del mejor camino

        Iterator<Airport> adjIter = origin.connectedAirportsIterator();

        // Falta hacer que lo haga en los días dados
        while (adjIter.hasNext()) {

            Airport adj = adjIter.next();
            if (origin.flightExistsTo(adj)) {
            	for (Day day : days) {
	                Iterator<Ticket> ticketIter = origin.ticketIterTo(adj, day);
	                Ticket prev = null;
	                while (ticketIter.hasNext()) {
	                    Ticket ticket = ticketIter.next();
	                    if(prev == null || !sameTicketFeatures(ticket, prev)){
		                	prev = ticket;
		                    	
		                	// Para cada vuelo distinto creo el heap de vuelta con los pesos infinitos.
		                    pq = queueAirports(fa.getAirports());
		                    pq.decreasePriority(origin, 0);
		                    pq.dequeue();
	
		                    fa.refreshAirportsNodeProperties();
	
		                    origin.visit();
		                    adj.setIncident(ticket);
		                    pq.decreasePriority(adj, ticket.getDuration().getMinutes());
	
		                    Box b = findPath(pq, dest, TotalTimeWeighter.WEIGHTER, days, false, bestWeight);
	
		                    // Si se encontró camino de menor peso se reemplaza
		                    if (b != null && Double.compare(bestWeight,b.lastWeight) > 0) {
		                        bestWeight = b.lastWeight;
		                        bestPath = b.list;
		                    }
	                    }
	                }
	            }
            }
        }
        return (bestPath != null) ? bestPath : new LinkedList<>();  // Lista vacia en caso de no encontrar camino
    }
    
    private static boolean sameTicketFeatures(Ticket ticket, Ticket other){
    	return ticket.getDeparture().equals(other.getDeparture()) 
    			&& ticket.getDuration().equals(other.getDuration());
    }

    private static Box findPath (BinaryMinHeap<Airport> pq, Airport dest,
        Weighter weighter, List<Day> days, boolean isOrigin, double cutWeight) {

        while (!pq.isEmpty()) {
            Double minWeight = pq.minPriority();

            if (minWeight.compareTo(Double.POSITIVE_INFINITY) == 0) {
                return null; // No existe el camino, no tiene sentido seguir
            }
 
            if (minWeight >= cutWeight) { // El peso acumulado es mayor al del mejor camino encontrado
            	return null;
            }
            
            Airport current = pq.dequeue();
            current.visit();

            // Camino encontrado
            if (current.equals(dest)) {
                return new Box(buildList(dest), minWeight);
            }

            Iterator<Airport> iter = current.connectedAirportsIterator();
            while (iter.hasNext()) {
                Airport next = iter.next();
                
                // Si el adyacente no está visitado y si existen vuelos hacia él entre los días
                // indicados por la lista en el caso de que se quiera partir desde el aeropuerto
                // orígen; sino que simplemente existan vuelos si no se parte desde el orígen.
                if (!next.visited() && ((isOrigin && current.flightExistsTo(next, days)) || (
                    !isOrigin && current.flightExistsTo(next)))) {
                    WeightedTicket wTicket = weighter.minTicket(current, next);
                    double nextCurrWeight = pq.getPriority(next);
                    double acumWeight = minWeight + wTicket.weight();

                    if (acumWeight < nextCurrWeight) {
                        next.setIncident(wTicket.ticket());
                        pq.decreasePriority(next, acumWeight);
                    }
                }
            }
            // Si está en el aeropuerto del origen tiene que ciclar una sola vez, y devuelve null.
            if (isOrigin)
                return null;
        }
        return null;
    }

    private static BinaryMinHeap<Airport> queueAirports (SimpleMap<String, Airport> airports) {
        int size = airports.size();

        BinaryMinHeap<Airport> heap = new BinaryMinHeap<>(size);

        Iterator<Airport> iter = airports.valueIterator();

        while (iter.hasNext()) {
            heap.enqueue(iter.next(), Double.POSITIVE_INFINITY);
        }

        return heap;
    }

    private static List<Ticket> buildList (Airport last) {
        LinkedList<Ticket> list = new LinkedList<>();

        Airport curr = last;
        Ticket t;
        while ((t = curr.getIncident()) != null) {
            list.addFirst(t);
            curr = t.getOrigin();
        }
        return list;
    }


    private static class Box {
        List<Ticket> list;
        double lastWeight;

        public Box (List<Ticket> list, double lastWeight) {
            this.list = list;
            this.lastWeight = lastWeight;
        }
    }
}
