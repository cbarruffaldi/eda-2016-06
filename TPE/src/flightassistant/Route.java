package flightassistant;

import structures.AVLSet;
import utils.Day;
import utils.Day.WeekArray;
import utils.Moment;

import java.util.Comparator;
import java.util.Iterator;

/**
 * Representa una ruta entre dos aeropuertos únicos. Contiene los vuelos entre ambos,
 * en ambas direcciones.
 * @see Airport
 * @see Flight
 */
public class Route{

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

    /**
     * Añade un <tt>Ticket<tt> a la Ruta
     * @param Ticket a ser añadido
     */
    public void addTicket (Ticket ticket) {
        TicketContainer container = selectContainer(ticket.getOrigin());
        container.addTicket(ticket);
    }

    /**
     * Elimina un <tt>Ticket</tt> de la Ruta
     * @param Ticker a ser removido de la ruta
     */
    public void removeTicket (Ticket ticket) {
        TicketContainer container = selectContainer(ticket.getOrigin());
        container.removeTicket(ticket);
    }

    /**
     * Verifica si existen vuelos en esa ruta
     * @return true si existe por lo menos un vuelo, false sino
     */
    public boolean hasFlights () {
        return containerA.hasFlights() || containerB.hasFlights();
    }

    public HigherIterator iteratorOfHigherFlightsFrom (Airport from, Moment startTime) {
        if (!flightExistsFrom(from))
            throw new IllegalArgumentException("This route does not connect " + from);
        TicketContainer container = selectContainer(from);
        return container.iteratorOfHigherFlights(startTime);
    }

    /**
     * Retorna el un ticket con el vuelo mas barato desde un aeropuerto
     * @param airport desde el cual se quiere obtener el vuelo mas barato
     * @return <tt>Ticket<tt> con el vuelo mas barato
     */
    public Ticket getCheapestFrom (Airport airport) {
        return selectContainer(airport).getCheapest();
    }

    /**
     * Retorna el un ticket con el vuelo mas rapido (menor tiempo de vuelo) desde un aeropuerto
     * @param airport desde el cual se quiere obtener el vuelo mas rápido
     * @return <tt>Ticket<tt> con el vuelo mas rápido
     */
    public Ticket getQuickestFrom (Airport airport) {
        return selectContainer(airport).getQuickest();
    }

    /**
     * Retorna el un ticket con el vuelo mas barato desde un aeropuerto en un <tt>Day</tt> determinado
     * @param airport desde el cual se quiere obtener el vuelo mas barato
     * @param Dia de salida 
     * @return <tt>Ticket<tt> con el vuelo mas barato en el día recibido
     */
    public Ticket getCheapestFrom (Airport airport, Day day) {
        return selectContainer(airport).getCheapest(day);
    }
    
    /**
     * Retorna el un ticket con el vuelo mas rápido desde un aeropuerto en un <tt>Day</tt> determinado
     * @param airport desde el cual se quiere obtener el vuelo mas rápido
     * @param Dia de salida 
     * @return <tt>Ticket<tt> con el vuelo mas rápido en el día recibido
     */
    public Ticket getQuickestFrom (Airport airport, Day day) {
        return selectContainer(airport).getQuickest(day);
    }

    /**
     * Elige un {@link TicketContainer} segun si el vuelo incide en el aeropuerto base o sale hacia
     * el aeropuerto base
     * @param base <tt>Airport<tt> con el cual se quiere comparar
     * @return <tt>TicketContainer</tt> correspondiente a lo pedido
     */
    private TicketContainer selectContainer (Airport base) {
        if (base.equals(airportA))
            return containerA;
        else if (base.equals(airportB))
            return containerB;
        else
            throw new IllegalArgumentException("This route does not connect s" + base);
    }

    /**
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

    /**
     * Verifica si existen vuelo desde un aeropuerto determinado
     * @param airport desde el cual se quiere averiguar si existen vuelo
     * @return true si existe al menos un vuelo desde un aeropuerto
     */
    public boolean flightExistsFrom (Airport airport) {
        return getCheapestFrom(airport) != null; //Cheapest es null cuando no hay vuelos
    }

    /**
     * Verifica si existen vuelo desde un aeropuerto determinado en un dia determinado
     * @param airport desde el cual se quiere averiguar si existen vuelo
     * @param <tt>Day<tt> en el que sale el vuelo
     * @return true si existe al menos un vuelo desde un aeropuerto
     */
    public boolean flightExistsFrom (Airport airport, Day day) {
        return getCheapestFrom(airport, day) != null;
    }

    public Iterator<Ticket> dayFlights (Airport from, Day day) {
        TicketContainer container = selectContainer(from);
        return container.weekArray.get(day).iterator();
    }

    /**
     * Clase que contiene todos los vuelos de un aeropuerto a otro, en una sola dirección.
     * Consta de un {@link WeekArray} con el {@link Ticket} mas barato correspondiente a cada día
     * de la semana, otro con el mas rápido, y otro con todos los vuelos de cada día.
     */
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

        /**
         * Añade un Ticket en un {@link WeekArray} segun el dia de salida.
         * @param ticket a agregar
         */
        private void addTicket (Ticket ticket) {
            Day departureDay = ticket.getDeparture().getDay();
            if (cheapest.get(departureDay) == null || ticket
                .isCheaperThan(cheapest.get(departureDay)))
                cheapest.insert(departureDay, ticket);
            if (quickest.get(departureDay) == null || ticket
                .isQuickerThan(quickest.get(departureDay)))
                quickest.insert(departureDay, ticket);
            weekArray.get(ticket.getDeparture().getDay()).add(ticket);
        }

        /**
         * Elimina un ticket del {@link WeekArray}
         * @param ticket
         */
        private void removeTicket (Ticket ticket) {
            Day departureDay = ticket.getDeparture().getDay();

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

        /**
         * En caso de ser eliminado el ticket con el menor tiempo de vuelo en un dia particular
         * se busca el de menor tiempo de vuelo entre los tickets restantes de ese mismo dia.
         * @param <tt>Day<tt> del cual se debe recalcular el mas rápido
         */
        private void recalculateQuickest (Day day) {
            for (Ticket ticket : weekArray.get(day))
                if (quickest.get(day) == null || ticket.isQuickerThan(quickest.get(day)))
                    quickest.insert(day, ticket);
        }
        
        /**
         * En caso de ser eliminado el ticket con el menor precio en un dia particular
         * se busca el de menor precio entre los tickets restantes de ese mismo dia.
         * @param <tt>Day<tt> del cual se debe recalcular el de menor precio
         */
        private void recalculateCheapest (Day day) {
            for (Ticket ticket : weekArray.get(day))
                if (cheapest.get(day) == null || ticket.isCheaperThan(cheapest.get(day)))
                    cheapest.insert(day, ticket);
        }

        private HigherIterator iteratorOfHigherFlights (Moment startTime) {
            return new HigherIterator(startTime, weekArray);
        }

        /**
         * Retorna el <tt>Ticket</tt> con el vuelo mas barato de un <tt>Day</tt> particular
         * @param dia del que se quiere obtener el mas barato
         * @return Ticker con el vuelo mas barato de un dia particular
         */
        public Ticket getCheapest (Day day) {
            return cheapest.get(day);
        }
        
        /**
         * Retorna el <tt>Ticket</tt> con el vuelo mas rápido (menor tiempo de vuelo) de un <tt>Day</tt> particular
         * @param dia del que se quiere obtener el mas rápido
         * @return Ticker con el vuelo mas rápido de un dia particular
         */
        public Ticket getQuickest (Day day) {
            return quickest.get(day);
        }

        /**
         * Retorna el <tt>Ticket</tt> con el vuelo mas rápido (menor tiempo de vuelo)
         * @return Ticker con el vuelo mas rápido
         */
        public Ticket getQuickest () {
            Ticket quickestTicket = null;
            for (Ticket ticket : quickest)
                if (quickestTicket == null || (ticket != null && ticket
                    .isQuickerThan(quickestTicket)))
                    quickestTicket = ticket;
            return quickestTicket;
        }

        /**
         * Retorna el <tt>Ticket</tt> con el vuelo mas barato (menor tiempo de vuelo)
         * @return Ticker con el vuelo mas barato
         */
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
