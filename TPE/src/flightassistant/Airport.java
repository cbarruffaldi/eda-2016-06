package flightassistant;

import structures.AVLSet;
import structures.SimpleHashMap;
import structures.SimpleMap;
import utils.Day;
import utils.Moment;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Un aeropouerto. El mismo se identifica segun su ID (nombre), pero tiene
 * tambien informacion de latitud y longitud.
 * Un aeropuerto se vincula a otro mediante {@link Route}s bidireciconales,
 * que guardan todos los vuelos entre los mismos.
 * 
 * @see Route
 */
public class Airport implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private double latitude;
	private double longitude;
	private SimpleMap<Airport, Route> routes;
	private Ticket incident;
	private boolean visited;

	/**
	 * Construye un aeropuerto
	 * @param id - El nombre del aeropuerto
	 * @param latitude - latitud del aeropuerto
	 * @param longitude - longitud del aeropuerto
	 */
	public Airport(String id, double latitude, double longitude) {
		if(!id.matches("[A-Za-z]{3}")){
			throw new IllegalArgumentException("ID invalido (deben ser 3 letras)");
		}
		if(!validCoordinates(latitude, longitude))
			throw new IllegalArgumentException("Coordenadas invalidas");
		
		
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;

		routes = new SimpleHashMap<>(new Comparator<Airport>() {
			@Override
			public int compare(Airport o1, Airport o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});
	}

	private boolean validCoordinates(double latitude, double longitude) {
		return Double.compare(-90, latitude) <= 0 && Double.compare(90, latitude) >= 0 
				&& Double.compare(-180, latitude) <= 0 && Double.compare(180, latitude) >= 0;
	}

	public String getId() {
		return id;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void addFlight(Flight flight) {
		if (flight == null)
			throw new NullPointerException("null flight");

		Airport destination = flight.getDestination();
		if (!routeExistsTo(destination)) { //Un acceso mas, pero queda mas claro
			throw new IllegalStateException();
		}

		Route r = routes.get(destination);
		List<Ticket> tickets = TicketMachine.makeTickets(flight);

		for (Ticket ticket : tickets)
			r.addTicket(ticket);
	}

	public void removeFlight(Flight flight) {
		if (flight == null)
			throw new NullPointerException("null flight");

		Airport destination = flight.getDestination();
		if (!routeExistsTo(destination)) {
			throw new IllegalStateException();
		}

		Route r = routes.get(destination);
		List<Ticket> tickets = TicketMachine.makeTickets(flight);

		for (Ticket ticket : tickets)
			r.removeTicket(ticket);
	}

	public Route getRouteTo(Airport airport) {
		return routes.get(airport);
	}

	public boolean routeExistsTo(Airport airport) {
		return routes.containsKey(airport);
	}

	public void addRoute(Airport airport, Route r) {
		routes.put(airport, r);
	}

	public void removeRouteTo(Airport destination) {
		routes.remove(destination);
	}

	public void removeAllRoutes() {
		routes.clear();
	}

	public HigherIterator iteratorOfHigherFlightsTo(Airport to, Moment fromMoment) {
		Route route = routes.get(to);
		if (route == null)
			return null;
		else
			return route.iteratorOfHigherFlightsFrom(this, fromMoment);
	}

	public Set<Airport> getConnectedAirports() {
		return routes.keySet();
	}

	public Iterator<Airport> connectedAirportsIterator() {
		return routes.keyIterator();
	}

	@Override
	public String toString(){
		return id;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	/*
	 * Determina la igualdad de dos Aeropuertos segun su nombre (id)
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		Airport other = (Airport) o;
		return id == null ? other.id == null : id.equals(other.id);
	}


	//Lo que sigue es modelado de Nodo, para utilizar en los algoritmos de caminos minimos

	public boolean flightExistsTo(Airport destination) {
		return	routeExistsTo(destination) && routes.get(destination).flightExistsFrom(this);
	}

	/**
	 * Devuelve verdadero si existe vuelo que parte al destino en el día deseado.
	 * @param destination - destino al que se desea volar.
	 * @param day - día en el cual se desea volar
	 * @return true si existe vuelo, false sino.
	 */
	public boolean flightExistsTo(Airport destination, Day day) {
		return	routeExistsTo(destination) && routes.get(destination).flightExistsFrom(this, day);
	}

	/**
	 * Devuelve verdadero si existe vuelo que parte al destino en alguno de los días de la lista
	 * @param destination - destino al cual volar.
	 * @param days - días en los que se desea volar.
	 * @return true si existe vuelo, false sino
	 */
	public boolean flightExistsTo(Airport destination, List<Day> days) {
		if (routeExistsTo(destination)) {
			Route r = routes.get(destination);
			for (Day day : days)
				if (r.flightExistsFrom(this, day))
					return true;
		}
		return false;
	}

	public Ticket getCheapestTo(Airport destination) {
		return routes.get(destination).getCheapestFrom(this);
	}

	public Ticket getQuickestTo(Airport destination) {
		return routes.get(destination).getQuickestFrom(this);
	}

	public Ticket getCheapestTo(Airport destination, Day day) {
		Route route = routes.get(destination);
		return route.getCheapestFrom(this, day);
	}

	public Ticket getQuickestTo(Airport destination, Day day) {
		Route route = routes.get(destination);
		return route.getQuickestFrom(this, day);
	}

	public void nodeRefresh(){
		visited = false;
		incident = null;
	}

	public void visit(){
		visited = true;
	}

	public void unvisit(){
		visited = false;
	}

	public boolean visited() {
		return visited;
	}

	public Ticket getIncident(){
		return incident;
	}

	public void setIncident(Ticket t){
		incident = t;
	}

	public AVLSet<Double> getFlightTimes(Day departure) {
		AVLSet<Double> set = new AVLSet<>();

		Iterator<Route> iter = routes.valueIterator();
		Route curr;
		while(iter.hasNext()){
			curr = iter.next();
			if(curr.flightExistsFrom(this, departure)){
				Iterator<Ticket> df = curr.dayFlights(this, departure);
				while(df.hasNext()){
					set.add((double)df.next().getDepartureTime().getMinutes());
				}
			}
		}
		return set;

	}

	public Iterator<Ticket> ticketIterTo(Airport adj, Day day) {
		return routes.get(adj).dayFlights(this, day);
	}
}
