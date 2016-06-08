package flightassistant;

import structures.BinaryMinHeap;
import structures.SimpleMap;
import utils.Day;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class InfinityDijkstra {

    public static List<Ticket> minPath (SimpleMap<String, Airport> airports, Airport origin,
        Airport dest, Weighter weighter, List<Day> days) {
        return minpath(airports, origin, dest, weighter, null, days);
    }

    public static List<Ticket> minpath (SimpleMap<String, Airport> airports, Airport origin,
        Airport dest, Weighter weighter, Weighter originWeighter, List<Day> days) {

        BinaryMinHeap<Airport> pq = queueAirports(airports);

        pq.decreasePriority(origin, 0);

        List<Ticket> ans;
        if (originWeighter != null) {
            findPath(pq, dest, originWeighter, days, true, -1);
        }

        Box b = findPath(pq, dest, weighter, null, false, -1);
        ans = (b != null) ? b.list : null;

        return ans;
    }

    public static List<Ticket> minPathTotalTime (FlightAssistant fa, Airport origin,
        Airport dest, List<Day> days) {

        List<Ticket> bestPath = null;
        BinaryMinHeap<Airport> pq;
        double bestWeight = Double.POSITIVE_INFINITY;

        Iterator<Airport> adjIter = origin.connectedAirportsIterator();

        // Falta hacer que lo haga en los días dados
        while (adjIter.hasNext()) {

            Airport adj = adjIter.next();
            if (origin.flightExistsTo(adj)) {
                Iterator<Ticket> ticketIter = origin.ticketIterTo(adj, Day.LU);

                while (ticketIter.hasNext()) {
                    // Para cada vuelo distinto creo el heap de vuelta con los pesos infinitos.
                    pq = queueAirports(fa.getAirports());
                    pq.decreasePriority(origin, 0);
                    pq.dequeue();
                    origin.visit();

                    Ticket ticket = ticketIter.next();
                    fa.refreshAirportsNodeProperties();

                    adj.setIncident(ticket);
                    pq.decreasePriority(adj, ticket.getDuration().getMinutes());

                    Box b = findPath(pq, dest, TotalTimeWeighter.WEIGHTER, days, false, bestWeight);

                    if (b != null && Double.compare(bestWeight,b.lastWeight) > 0) {
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
            Double minWeight = pq.minPriority();

            if (Double.compare(minWeight, Double.POSITIVE_INFINITY) == 0)
                return new Box(new LinkedList<>(), Double.POSITIVE_INFINITY); //No existe el camino, no tiene sentido seguir

            Airport current = pq.dequeue();
            current.visit();

            if (cutWeight > 0 && minWeight > cutWeight) {
                return null;
            }

            if (current.equals(dest)) {
                return new Box(buildList(dest), minWeight);
            }

            Iterator<Airport> iter = current.connectedAirportsIterator();
            while (iter.hasNext()) {
                Airport next = iter.next();
                if (!next.visited() && ((isOrigin && current.flightExistsTo(next, days)) || (
                    !isOrigin && current.flightExistsTo(next)))) {
                    WeightedTicket wTicket = weighter.minTicket(current, next);

                    //TODO Me tiró una excepcion porque no estaba next en la PQ. Ver.
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
