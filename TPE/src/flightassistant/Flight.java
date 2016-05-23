package flightassistant;

public class Flight {

	private String airline;
	private int number;
	private double price;
	// TODO: horario y día de llegada
	// TODO: horario y día de partida
	private Airport origin;
	private Airport destination;
	// private List<Day> departureDays; No se que nos conviene tener, si una
	// list u otra estructura...

	public Flight(String airline, int number, double price, String departureTime, 
			Airport origin, Airport destination) {
		this.airline = airline;
		this.price = price;
		this.origin = origin;
		this.destination = destination;
	}
	
	public double getPrice() {
		return price;
	}

	@Override
	public int hashCode() {
		// TODO: Hay que ver como combinamos el string de la aerolinea con el
		// numero de vuelo dependiendo de la estructura que vayamos a usar
		return 1;

	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		Flight other = (Flight) o;
		if (airline == null) {
			if (other.airline != null) {
				return false;
			}
		} else if (!airline.equals(other.airline)) {
			return false;
		}
		if (number != other.number) {
			return false;
		}
		return true;
	}

}
