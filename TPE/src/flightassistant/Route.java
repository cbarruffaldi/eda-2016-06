package flightassistant;

import structures.AVLSet;
import utils.Day;
import utils.Day.WeekArray;
import utils.Moment;

import java.util.Comparator;


public class Route {

	//Airport A y B son nombres y no implican ningun orden.
	private Airport airportA;
	private Airport airportB;

	private FlightContainer containerA; // vuelos que salen del A
	private FlightContainer containerB; // vuelos que salen del B

	public Route(Airport airport1, Airport airport2) {
		if(airport1 == null || airport2 == null) {
			throw new IllegalArgumentException();
		}
		if(airport1.equals(airport2)) {
			throw new IllegalArgumentException();
		}
		this.airportA = airport1;
		this.airportB = airport2;

		this.containerA = new FlightContainer();
		this.containerB = new FlightContainer();
	}

	public void addFlight(Flight flight) {
		if (flight.getOrigin().equals(airportA))
			containerA.addFlight(flight);
		else if (flight.getOrigin().equals(airportB))
			containerB.addFlight(flight);
		else
			throw new IllegalArgumentException("Aeropuerto origen inválido");
	}

	public void removeFlight(Flight flight) {
		if (flight.getOrigin().equals(airportA))
			containerA.removeFlight(flight);
		else if (flight.getOrigin().equals(airportB))
			containerB.removeFlight(flight);
		else
			throw new IllegalArgumentException("Aeropuerto origen inválido");
	}

	public boolean hasFlights() {
		return containerA.hasFlights() || containerB.hasFlights();
	}

	private FlightContainer selectContainer(Airport base){
		if(base.equals(airportA))
			return containerA;
		else if(base.equals(airportB))
			return containerB;
		else
			throw new IllegalArgumentException("This route does not connect" + base);
	}

	public HigherIterator iteratorOfHigherFlightsFrom(Airport from, Moment startTime) {
		if (!flightExistsFrom(from))
			throw new IllegalArgumentException("This route does not connect " + from);
		FlightContainer container = selectContainer(from);
		return container.iteratorOfHigherFlights(startTime);
	}

	public Flight getCheapestFrom(Airport airport){
		return selectContainer(airport).cheapest;
	}


	public Flight getQuickestFrom(Airport airport) {
		return selectContainer(airport).quickest;
	}

	/*
	 * Determina la igualdad de dos Rutas, segun los dos aeropuertos visitados
	 */
	@Override
	public boolean equals(Object o) {
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
		if(!airportA.equals(other.airportA) && !airportA.equals(other.airportB)) {
			return false;
		}
		if (!airportB.equals(other.airportA) && !airportB.equals(other.airportB)) {
			return false;
		}
		return true;

	}

	public boolean flightExistsFrom(Airport airport) {
		return getCheapestFrom(airport) != null; //Cheapest es null cuando no hay vuelos
	}

	private static class FlightContainer {

		// Vuelos en el container ordenados por momento de salida.
		private static final Comparator<Flight> flightCmp = new Comparator<Flight>() {
			@Override
			public int compare(Flight o1, Flight o2) {
				int comp = o1.getDepartureTime().compareTo(o2.getDepartureTime());
				return comp == 0 ? o1.getId().compareTo(o2.getId()) : comp;
			}
		};

		private Flight cheapest;
		private Flight quickest;
		private WeekArray<AVLSet<Flight>> weekArray;

		public FlightContainer() {
			weekArray = Day.newWeekArray();
			weekArray.insert(Day.LU, new AVLSet<Flight>(flightCmp));
			for (Day day = Day.MA; !day.equals(Day.LU); day = day.getNextDay())
				weekArray.insert(day, new AVLSet<Flight>(flightCmp));
		}

		private boolean hasFlights() {
			return cheapest != null;
		}

		private void addFlight(Flight flight) {
			if (cheapest == null || flight.isCheaperThan(cheapest))
				cheapest = flight;
			if (quickest == null || flight.isQuickerThan(quickest))
				quickest = flight;
			weekArray.get(flight.getDeparture().getDay()).add(flight);
		}

		private void removeFlight(Flight flight) {
			if (flight.equals(cheapest)) {
				cheapest = null;
				recalculateCheapest();
			}
			if (flight.equals(quickest)) {
				quickest = null;
				recalculateQuickest();
			}

			for (AVLSet<Flight> dayFlights: weekArray) {
				dayFlights.remove(flight);
			}
		}

		private void recalculateQuickest() {
			for (AVLSet<Flight> each : weekArray)
				for (Flight flight : each)
					if (quickest == null || flight.isQuickerThan(quickest))
						quickest = flight;
		}

		private void recalculateCheapest() {
			for (AVLSet<Flight> each : weekArray)
				for (Flight flight : each)
					if (cheapest == null || flight.isCheaperThan(cheapest))
						cheapest = flight;
		}

		private HigherIterator iteratorOfHigherFlights(Moment startTime) {
			return new HigherIterator(startTime, weekArray);
		}

	}
}
