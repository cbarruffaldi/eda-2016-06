package flightassistant;

import structures.AVLSet;
import utils.Day;
import utils.Day.WeekArray;
import utils.Moment;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;

public class Route implements Serializable {

    private static final long serialVersionUID = 1L;
    //Airport A y B son nombres y no implican ningun orden.
    private Airport airportA;
    private Airport airportB;

    private TicketContainer containerA; // vuelos que salen del A
    private TicketContainer containerB; // vuelos que salen del B

    public Route (Airport airport1, Airport airport2) {
        if (airport1 == null || airport2 == null) {
            throw new IllegalArgumentException();
        }
        if (airport1.equals(airport2)) {
            throw new IllegalArgumentException();
        }
        this.airportA = airport1;
        this.airportB = airport2;

        this.containerA = new TicketContainer();
        this.containerB = new TicketContainer();
    }

    public void addTicket (Ticket ticket) {
        TicketContainer container = selectContainer(ticket.getOrigin());
        container.addTicket(ticket);
    }

    public void removeTicket (Ticket ticket) {
        TicketContainer container = selectContainer(ticket.getOrigin());
        container.removeTicket(ticket);
    }

    public boolean hasFlights () {
        return containerA.hasFlights() || containerB.hasFlights();
    }

    private TicketContainer selectContainer (Airport base) {
        if (base.equals(airportA))
            return containerA;
        else if (base.equals(airportB))
            return containerB;
        else
            throw new IllegalArgumentException("This route does not connect s" + base);
    }

    public HigherIterator iteratorOfHigherFlightsFrom (Airport from, Moment startTime) {
        if (!flightExistsFrom(from))
            throw new IllegalArgumentException("This route does not connect " + from);
        TicketContainer container = selectContainer(from);
        return container.iteratorOfHigherFlights(startTime);
    }

    public Ticket getCheapestFrom (Airport airport) {
        return selectContainer(airport).getCheapest();
    }


    public Ticket getQuickestFrom (Airport airport) {
        return selectContainer(airport).getQuickest();
    }

    public Ticket getCheapestFrom (Airport airport, Day day) {
        return selectContainer(airport).getCheapest(day);
    }

    public Ticket getQuickestFrom (Airport airport, Day day) {
        return selectContainer(airport).getQuickest(day);
    }

    /*
     * Determina la igualdad de dos Rutas, segun los dos aeropuertos visitados
     */
    @Override public boolean equals (Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Route other = (Route) o;

        //Aca se asume que no puede haber una ruta con algun aeropuerto en null,
        //y que no existe una ruta tal que los dos aeropuertos sean el mismo,
        //esto esta contemplado en el constructor
        if (!airportA.equals(other.airportA) && !airportA.equals(other.airportB)) {
            return false;
        }
        if (!airportB.equals(other.airportA) && !airportB.equals(other.airportB)) {
            return false;
        }
        return true;

    }

    public boolean flightExistsFrom (Airport airport) {
        return getCheapestFrom(airport) != null; //Cheapest es null cuando no hay vuelos
    }


    public boolean flightExistsFrom (Airport airport, Day day) {
        return getCheapestFrom(airport, day) != null;
    }

    public Iterator<Ticket> dayFlights (Airport from, Day day) {
        TicketContainer container = selectContainer(from);
        return container.weekArray.get(day).iterator();
    }

    private static class TicketContainer {

        // Vuelos en el container ordenados por momento de salida.
        private static final Comparator<Ticket> ticketCmp = new Comparator<Ticket>() {
            @Override public int compare (Ticket o1, Ticket o2) {

                int comp = o1.getDepartureTime().compareTo(o2.getDepartureTime());
                return comp == 0 ? o1.getFlightId().compareTo(o2.getFlightId()) : comp;
            }
        };

        private WeekArray<Ticket> cheapest;
        private WeekArray<Ticket> quickest;
        private WeekArray<AVLSet<Ticket>> weekArray;

        public TicketContainer () {
            cheapest = Day.newWeekArray();
            quickest = Day.newWeekArray();
            weekArray = Day.newWeekArray();
            weekArray.insert(Day.LU, new AVLSet<Ticket>(ticketCmp));
            for (Day day = Day.MA; !day.equals(Day.LU); day = day.getNextDay())
                weekArray.insert(day, new AVLSet<Ticket>(ticketCmp));
        }

        private boolean hasFlights () {
            for (Ticket f : cheapest)
                if (f != null)
                    return true;
            return false;
        }

        private void addTicket (Ticket ticket) {
            Day departureDay = ticket.getArrival().getDay();
            if (cheapest.get(departureDay) == null || ticket
                .isCheaperThan(cheapest.get(departureDay)))
                cheapest.insert(departureDay, ticket);
            if (quickest.get(departureDay) == null || ticket
                .isQuickerThan(quickest.get(departureDay)))
                quickest.insert(departureDay, ticket);
            weekArray.get(ticket.getDeparture().getDay()).add(ticket);
        }

        private void removeTicket (Ticket ticket) {
            Day departureDay = ticket.getArrival().getDay();

            weekArray.get(departureDay).remove(ticket);

            if (ticket.equals(cheapest.get(departureDay))) {
                cheapest.insert(departureDay, null);
                recalculateCheapest(departureDay);
            }
            if (ticket.equals(quickest.get(departureDay))) {
                quickest.insert(departureDay, null);
                recalculateQuickest(departureDay);
            }
        }

        private void recalculateQuickest (Day day) {
            for (Ticket ticket : weekArray.get(day))
                if (quickest.get(day) == null || ticket.isQuickerThan(quickest.get(day)))
                    quickest.insert(day, ticket);
        }

        private void recalculateCheapest (Day day) {
            for (Ticket ticket : weekArray.get(day))
                if (cheapest.get(day) == null || ticket.isCheaperThan(cheapest.get(day)))
                    cheapest.insert(day, ticket);
        }

        private HigherIterator iteratorOfHigherFlights (Moment startTime) {
            return new HigherIterator(startTime, weekArray);
        }

        public Ticket getCheapest (Day day) {
            return cheapest.get(day);
        }

        public Ticket getQuickest (Day day) {
            return quickest.get(day);
        }

        public Ticket getQuickest () {
            Ticket quickestTicket = null;
            for (Ticket ticket : quickest)
                if (quickestTicket == null || (ticket != null && ticket
                    .isQuickerThan(quickestTicket)))
                    quickestTicket = ticket;
            return quickestTicket;
        }

        public Ticket getCheapest () {
            Ticket cheapestTicket = null;
            for (Ticket ticket : cheapest)
                if (cheapestTicket == null || (ticket != null && ticket
                    .isCheaperThan(cheapestTicket)))
                    cheapestTicket = ticket;
            return cheapestTicket;
        }

    }
}
