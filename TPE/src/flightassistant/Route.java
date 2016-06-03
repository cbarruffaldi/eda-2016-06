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

	public Flight getCheapestFrom(Airport airport) {
		return selectContainer(airport).getCheapest();
	}


	public Flight getQuickestFrom(Airport airport) {
		return selectContainer(airport).getQuickest();
	}

	public Flight getCheapestFrom(Airport airport, Day day) {
		FlightContainer container = selectContainer(airport);
		return container.getCheapest(day);
	}
	
	public Flight getQuickestFrom(Airport airport, Day day) {
		FlightContainer container = selectContainer(airport);
		return container.getQuickest(day);
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

		private WeekArray<Flight> cheapest;
		private WeekArray<Flight> quickest;
		private WeekArray<AVLSet<Flight>> weekArray;

		public FlightContainer() {
			cheapest = Day.newWeekArray();
			quickest = Day.newWeekArray();
			weekArray = Day.newWeekArray();
			weekArray.insert(Day.LU, new AVLSet<Flight>(flightCmp));
			for (Day day = Day.MA; !day.equals(Day.LU); day = day.getNextDay())
				weekArray.insert(day, new AVLSet<Flight>(flightCmp));
		}

		private boolean hasFlights() {
			for (Flight f : cheapest)
				if (f != null)
					return true;
			return false;
		}

		private void addFlight(Flight flight) {
			Day departureDay = flight.getArrival().getDay();
			if (cheapest.get(departureDay) == null || flight.isCheaperThan(cheapest.get(departureDay)))
				cheapest.insert(departureDay, flight);
			if (quickest.get(departureDay) == null || flight.isQuickerThan(quickest.get(departureDay)))
				quickest.insert(departureDay, flight);
			weekArray.get(flight.getDeparture().getDay()).add(flight);
		}

		private void removeFlight(Flight flight) {
			Day departureDay = flight.getArrival().getDay();
			
			weekArray.get(departureDay).remove(flight);
			
			if (flight.equals(cheapest.get(departureDay))) {
				cheapest.insert(departureDay, null);
				recalculateCheapest(departureDay);
			}
			if (flight.equals(quickest.get(departureDay))) {
				quickest.insert(departureDay, null);
				recalculateQuickest(departureDay);
			}
		}

		private void recalculateQuickest(Day day) {
			for (Flight flight : weekArray.get(day))
				if (quickest.get(day) == null || flight.isQuickerThan(quickest.get(day)))
					quickest.insert(day, flight);
		}

		private void recalculateCheapest(Day day) {
			for (Flight flight : weekArray.get(day))
				if (cheapest.get(day) == null || flight.isCheaperThan(cheapest.get(day)))
					cheapest.insert(day, flight);
		}

		private HigherIterator iteratorOfHigherFlights(Moment startTime) {
			return new HigherIterator(startTime, weekArray);
		}
		
		public Flight getCheapest(Day day) {
			return cheapest.get(day);
		}
		
		public Flight getQuickest(Day day) {
			return quickest.get(day);
		}
		
		public Flight getQuickest() {
			Flight quickestFlight = null;
			for (Flight flight : quickest)
				if (quickestFlight == null || flight.isCheaperThan(quickestFlight))
					quickestFlight = flight;
			return quickestFlight;
		}
		
		public Flight getCheapest() {
			Flight cheapestFlight = null;
			for (Flight flight : cheapest)
				if (cheapestFlight == null || flight.isCheaperThan(cheapestFlight))
					cheapestFlight = flight;
			return cheapestFlight;
		}

	}
}
