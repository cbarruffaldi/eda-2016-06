package flightassistant;

import structures.AVLHashMap;
import structures.SimpleMap;
import utils.Day;
import utils.Moment;
import utils.Time;

import java.util.Iterator;
import java.util.List;

public class FlightAssistant {

    private static final int AIRPORTS_SIZE = 1000;
    private static final int FLIGHTS_SIZE = 6000;
    
    /** Coleccion de Aeropuertos */
    private SimpleMap<String, Airport> airports;
 
    /** Coleccion de vuelos */
    private SimpleMap<FlightId, Flight> flights;

    /**
     * Construye un nuevo FlightAssistant
     */
    public FlightAssistant () {
        airports = new AVLHashMap<>(AIRPORTS_SIZE);
        flights = new AVLHashMap<>(FLIGHTS_SIZE);
    }

    
    /**
     * Inserta un aeropuerto al universo
     * @param id el id del aeropuerto, que lo identifica
     * @param latitude la latitud del aeropuerto
     * @param longitude la longitud del aeropuerto
     */
    public void insertAirport (String id, double latitude, double longitude) {
        if (!airports.containsKey(id))
            airports.put(id, new Airport(id, latitude, longitude));
    }

    
    /**
     * Crea un vuelo y lo asocia a los aeropuertos correspondientes mediante
     * rutas
     * @param airline - nombre de la aerolinea
     * @param number - numero de vuelo
     * @param price - precio del vuelo
     * @param departures - una lista de Moments en los que sale el vuelo
     * @param duration - lo que dura el vuelo
     * @param origin ID - del aeropuerto origen
     * @param destination - ID del aeropuerto destino
     * 
     * @see Moment
     * @see Route
     * @see Flight
     */
    public void insertFlight (String airline, int number, double price, List<Moment> departures,
        Time duration, String origin, String destination) {

        Airport origAir = airports.get(origin);
        Airport destAir = airports.get(destination);
        if (origAir == null || destAir == null) {
            return; 
        }
        // Crea la ruta en caso de que todavía no exista
        if (!origAir.routeExistsTo(destAir)) {
            Route r = new Route(origAir, destAir);
            origAir.addRoute(destAir, r);
            destAir.addRoute(origAir, r);
        }
        Flight newFlight = new Flight(airline, number, price, departures, duration, origAir, destAir);
        if(!flights.containsKey(newFlight.getId())){
        	flights.put(newFlight.getId(), newFlight);
        	origAir.addFlight(newFlight);
        }
    }

    /**
     * 	Elimina un vuelo de la ruta correspondiente.
     * 	Si la ruta esta vacia, la elimina.
     */
    public void removeFlight (String airline, int number) {
        FlightId flightId = new FlightId(airline, number);
        Flight flight = flights.get(flightId);
        if (flight != null) {
            Airport origAir = flight.getOrigin();
            Airport destAir = flight.getDestination();
            origAir.removeFlight(flight);
            flights.remove(flightId);

            // Borra la ruta entre los aeropuertos si no quedan mas vuelos entre ellos.
            Route flightRoute = origAir.getRouteTo(destAir);
            if (!flightRoute.hasFlights()) {
                origAir.removeRouteTo(destAir);
                destAir.removeRouteTo(origAir);
            }
        }
    }

    /**
     * Elimina un aeropuerto del unvierso
     * @param id - El ID del aeropuerto a eliminar
     */
    public void removeAirport (String id) {
        Airport airport = airports.get(id);
        if (airport != null) {
            removeRoutesTo(airport);
            airports.remove(id);
        }
    }

    /**
     * Elimina todos los aeropuertos del universo
     */
    public void removeAllAirports () {
        airports.clear();
        flights.clear();
    }

    /**
     * Elimina todos los vuelos entre aeropuertos
     * y las rutas correspondientes
     */
    public void removeAllFlights () {
        flights.clear();
        Iterator<Airport> iter = airports.valueIterator();
        while (iter.hasNext()) {
            Airport airport = iter.next();
            airport.removeAllRoutes();
        }
    }

    /**
     * Devuelve una lista de Tickets con el camino mas rapido (menor tiempo total)
     */
    public List<Ticket> findQuickestPath (String orig, String dest, List<Day> days) {
        return findPath(orig, dest, days, AirtimeWeighter.WEIGHTER, new OriginAirtimeWeighter(days));
    }

    /**
     * Devuelve una lista de Tickets que representa el camino mas barato entre dos aeropuertos
     * 
     * @param orig - ID del aeropuerto origen
     * @param dest - ID del aeropuerto destino
     * @param days - lista de los posibles dias de salida
     * 
     * @see Ticket
     */
    public List<Ticket> findCheapestPath (String orig, String dest, List<Day> days) {
        return findPath(orig, dest, days, PriceWeighter.WEIGHTER, new OriginPriceWeighter(days));
    }

    /**
     * Devuelve una lista de Tickets que representa el camino mas barato entre dos aeropuertos
     * 
     * @param orig - ID del aeropuerto origen
     * @param dest - ID del aeropuerto destino
     * @param days - lista de los posibles dias de salida
     * 
     * @see Ticket
	*/
    public List<Ticket> findShortestTotalTimeRoute(String orig, String dest, List<Day> days) {
    	Airport origin = airports.get(orig);
    	Airport destination = airports.get(dest);
    	if(origin == null || destination == null || origin.equals(destination))
    		return null;
    	
        return InfinityDijkstra.minPathTotalTime(this, airports.get(orig), airports.get(dest), days);
    }

    
    private List<Ticket> findPath (String orig, String dest, List<Day> days, Weighter weighter,
        Weighter originWeighter) {
        Airport origin = airports.get(orig);
        Airport destination = airports.get(dest);
        if (origin == null || destination == null || origin.equals(destination)) {
            return null;
        }
        refreshAirportsNodeProperties();
        if (days.isEmpty()) {
            return InfinityDijkstra.minPath(airports, origin, destination, weighter);
        }
        return InfinityDijkstra.minPath(airports, origin, destination, weighter, originWeighter, days);

    }

    private void removeRoutesTo (Airport airport) {
        Iterator<Airport> iter = airport.connectedAirportsIterator();
        while (iter.hasNext())
            iter.next().removeRouteTo(airport);
    }

    public void refreshAirportsNodeProperties () {
        Iterator<Airport> airportsIter = airports.valueIterator();
        while (airportsIter.hasNext())
            airportsIter.next().nodeRefresh();
    }

    public SimpleMap<String, Airport> getAirports () {
        return airports;
    }

    public SimpleMap<FlightId, Flight> getFlights () {
        return flights;
    }
}
