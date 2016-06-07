package flightassistant;

import structures.BinaryMinHeap;
import structures.SimpleMap;
import utils.Day;
import utils.Moment;
import utils.Time;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class InfinityDijkstra {

    public static List<Airport> minPath (SimpleMap<String, Airport> airports, Airport origin,
        Airport dest, Weighter weighter, List<Day> days) {
        return minpath(airports, origin, dest, weighter, null, days);
    }

    public static List<Airport> minpath (SimpleMap<String, Airport> airports, Airport origin,
        Airport dest, Weighter weighter, Weighter originWeighter, List<Day> days) {

        BinaryMinHeap<Airport> pq = queueAirports(airports);

        pq.decreasePriority(origin, 0);

        List<Airport> ans;
        if (originWeighter != null) {
            findPath(pq, dest, originWeighter, days, true, -1);
        }

        ans = findPath(pq, dest, weighter, null, false, -1).list;

        return ans;
    }

    public static List<Airport> minPathTotalTime (SimpleMap<String, Airport> airports, Airport origin,
        Airport dest, List<Day> days) {
        List<Airport> bestPath = null;
        BinaryMinHeap<Airport> pq = queueAirports(airports);
        double bestWeight = Double.MAX_VALUE;

        // Saco y visito el primero, peso 0.
        origin.visit();
        pq.decreasePriority(origin, 0);
        pq.dequeue();
        Iterator<Airport> adjIter = origin.connectedAirportsIterator();

        // Falta hacer que lo haga en los días dados
        while (adjIter.hasNext()) {
            Airport adj = adjIter.next();
            if (origin.flightExistsTo(adj)) {
                HigherIterator ticketIter = origin.iteratorOfHigherFlightsTo(adj, new Moment(Day.LU, new Time(0)));

                while (ticketIter.hasNext()) {
                    Ticket ticket = ticketIter.next();
                    System.out.println(ticket);
                    adj.setIncident(ticket);
                    pq.decreasePriority(adj, ticket.getDuration().getMinutes());

                    Box b = findPath(pq, dest, TotalTimeWeighter.WEIGHTER, days, false, bestWeight);

                    if (b != null && bestWeight > b.lastWeight) {
                        bestWeight = b.lastWeight;
                        bestPath = b.list;
                    }

                }
            }
        }
        return bestPath;
    }

    private static Box findPath (BinaryMinHeap<Airport> pq, Airport dest,
        Weighter weighter, List<Day> days, boolean isOrigin, double cutWeight) { // si es -1 no se tiene en cuenta

        while (!pq.isEmpty()) {
            Double minWeight = pq.minWeight();

            if (Double.compare(minWeight, Double.POSITIVE_INFINITY) == 0)
                return new Box(new LinkedList<>(), Double.POSITIVE_INFINITY); //No existe el camino, no tiene sentido seguir

            Airport airport = pq.dequeue();
            airport.visit();

            if (cutWeight > 0 && minWeight > cutWeight) {
                return null;
            }

            if (airport.equals(dest)) {
                return new Box(buildList(dest), minWeight);
            }

            Iterator<Airport> iter = airport.connectedAirportsIterator();
            while (iter.hasNext()) {
                Airport next = iter.next();
                if (!next.visited() && ((isOrigin && airport.flightExistsTo(next, days)) || (
                    !isOrigin && airport.flightExistsTo(next)))) {
                    WeightedTicket wTicket = weighter.minTicket(airport, next);

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
        return new Box(new LinkedList<>(), Double.POSITIVE_INFINITY);
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

    private static List<Airport> buildList (Airport last) {
        LinkedList<Airport> list = new LinkedList<>();

        Airport curr = last;
        Ticket t;
        while ((t = curr.getIncident()) != null) {
            list.addFirst(curr);
            curr = t.getOrigin();
        }
        list.addFirst(curr);
        return list;
    }


    private static class Box {
        List<Airport> list;
        double lastWeight;

        public Box (List<Airport> list, double lastWeight) {
            this.list = list;
            this.lastWeight = lastWeight;
        }
    }
}
