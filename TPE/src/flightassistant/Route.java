package flightassistant;

import structures.AVLSet;

import java.util.Comparator;

public class Route {

	private Airport airport1;

	//Airport 1 y 2 son nombres y no implican ningun orden.
	private Airport airport2;
	private FlightContainer container1; // vualos que salen del 1
	private FlightContainer container2; // vuelos que salen del 2
	public Route(Airport airport1, Airport airport2) {
		if(airport1 == null || airport2 == null) {
			throw new IllegalArgumentException();
		}
		if(airport1.equals(airport2)) {
			throw new IllegalArgumentException();
		}
		this.airport1 = airport1;
		this.airport2 = airport2;

		this.container1 = new FlightContainer();
		this.container2 = new FlightContainer();

	}

	public void addFlight(Flight flight, Airport origin) {
		if (origin.equals(airport1))
			container1.addFlight(flight);
		else if (origin.equals(airport2))
			container2.addFlight(flight);
		else
			throw new IllegalArgumentException("Aeropuerto origen inválido");
	}

	public void removeFlight(Flight flight, Airport origin) {
		if (origin.equals(airport1))
			container1.removeFlight(flight);
		else if (origin.equals(airport2))
			container2.removeFlight(flight);
		else
			throw new IllegalArgumentException("Aeropuerto origen inválido");
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
		if(!airport1.equals(other.airport1) && !airport1.equals(other.airport2)) {
			return false;
		}
		if (!airport2.equals(other.airport1) && !airport2.equals(other.airport2)) {
			return false;
		}
		return true;

	}

	private static class FlightContainer {
		// Tiene que ser un KDtree que tenga de keys momento de llegada, precio y tiempo de vuelo.
		private static final int DAYS = 7;

		private Flight cheapest;
		private Flight quickest;
		private AVLSet<Flight>[] sets; // índice 0 Lunes; índice 6 Domingo

		@SuppressWarnings("unchecked")
		public FlightContainer() {
			sets = (AVLSet<Flight>[]) new AVLSet<?>[DAYS];
			for (int i = 0; i < sets.length; i++)
				sets[i] = new AVLSet<Flight>(new Comparator<Flight>() {

					@Override
					public int compare(Flight o1, Flight o2) {
						return 0; // TODO: comparar por horario de salida, luego nombre y luego número de vuelo.
					}

				});
		}

		private void addFlight(Flight flight) {
			if (cheapest == null || flight.isCheaperThan(cheapest))
				cheapest = flight;
			if (quickest == null || flight.isQuickerThan(quickest))
				quickest = flight;
			// TODO: agregar a los sets que correspondan al dia
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
			// TODO: quitar de los sets que correspondan al día
		}

		private void recalculateQuickest() {
			for (AVLSet<Flight> each : sets)
				for (Flight flight : each)
					if (quickest == null || flight.isQuickerThan(quickest))
						quickest = flight;
		}

		private void recalculateCheapest() {
			for (AVLSet<Flight> each : sets)
				for (Flight flight : each)
					if (cheapest == null || flight.isCheaperThan(cheapest))
						cheapest = flight;
		}
	}
}
