package flightassistant;

import structures.AVLHashMap;
import structures.SimpleMap;
import utils.Day;
import utils.Moment;
import utils.Time;

import java.util.Iterator;
import java.util.List;

public class FlightAssistant {

    //20,50
    private static final int AIRPORTS_SIZE = 1500;
    private static final int FLIGHTS_SIZE = 6000; // TODO: pensar tamaños
    
    // Coleccion de Aeropuertos;
    private SimpleMap<String, Airport> airports;
    // Coleccion de vuelos
    private SimpleMap<FlightId, Flight> flights;

    public FlightAssistant () {
        airports = new AVLHashMap<>(AIRPORTS_SIZE);
        flights = new AVLHashMap<>(FLIGHTS_SIZE);
    }

    public void insertAirport (String id, double latitude, double longitude) {
        if (!airports.containsKey(id))
            airports.put(id, new Airport(id, latitude, longitude));
    }

    //Controla todo lo de las rutas aca de los dos aeropuerto. Queda un pcoo mas largo
    //pero para no delegar control de un aeropuerto en otro
    public void insertFlight (String airline, int number, double price, List<Moment> departures,
        Time duration, String origin, String destination) {

        Airport origAir = airports.get(origin);
        Airport destAir = airports.get(destination);
        if (origAir == null || destAir == null) {
            return; // TODO: podria devolver boolean para indicar si se pudo agregar el vuelo o no.
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

    public void removeAirport (String id) {
        Airport airport = airports.get(id);
        if (airport != null) {
            removeRoutesTo(airport);
            airports.remove(id);
        }
    }

    public void removeAllAirports () {
        airports.clear();
        flights.clear();
    }

    public void removeAllFlights () {
        flights.clear();
        Iterator<Airport> iter = airports.valueIterator();
        while (iter.hasNext()) {
            Airport airport = iter.next();
            airport.removeAllRoutes();
        }
    }

    public List<Ticket> findQuickestPath (String orig, String dest, List<Day> days) {
        return findPath(orig, dest, days, AirtimeWeighter.WEIGHTER, new OriginAirtimeWeighter(days));
    }

    public List<Ticket> findCheapestPath (String orig, String dest, List<Day> days) {
        return findPath(orig, dest, days, PriceWeighter.WEIGHTER, new OriginPriceWeighter(days));
    }

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
