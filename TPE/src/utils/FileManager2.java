package utils;

import flightassistant.Airport;
import flightassistant.Flight;
import flightassistant.FlightAssistant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

/**
 * {@code FileManager2} se encarga de guardar datos con un formato correspondiente en archivos de texto.
 * <p>
 * Existen dos tipos de archivos, uno que guarda todos los {@link Airport} de un {@link FlightAssistant}
 * y otro que guarda todos los {@link Flight}
 *
 */
public class FileManager2 {

	/**
	 * Guarda en un archivo de texto por defecto "flights.txt" todos los vuelos que almacena un <tt>FlighAssistant</tt>
	 * con el formato
	 * [aerolinea]#[nroVuelo]#[diasSemana]#[origen]#[destino]#[horaSalida]#[duracion]#[precio]
	 * @param {@link FlighAssistant} del cual se quieren guardar los <tt>Flight</tt>
	 * @throws FileNotFoundException
	 */
	public static void saveFlights(FlightAssistant fa) throws FileNotFoundException {
		PrintStream out = new PrintStream(new File("flights.txt"));
		Iterator<Flight> it = fa.getFlights().valueIterator();
		while(it.hasNext()){
			Flight flight = it.next();
			out.println(airline(flight) + "#" + flight.getId().getNumber() + "#" + departuresStr(flight) + "#" +
			originAirport(flight) + "#" + destAirport(flight) + "#" + departureTime(flight) + "#" + duration(flight) +
			"#" + flight.getPrice());
		}
		out.close();
	}

	/**
	 * Guarda en un archivo de texto "airports.txt" todos los aeropuertos que almacena un <tt>FlightAssistant</tt>
	 * con el formato
	 * [nombre]#[latitud]#[longitud]
	 * @param fa
	 * @throws FileNotFoundException
	 */
	public static void saveAirports(FlightAssistant fa) throws FileNotFoundException{
		PrintStream out = new PrintStream(new File("airports.txt"));
		Iterator<Airport> it = fa.getAirports().valueIterator();
		while(it.hasNext()) {
			Airport airport = it.next();
			out.println(airport.getId() + "#" + airport.getLatitude() + "#" + airport.getLongitude());
		}
		out.close();
	}

	/**
	 * Retorna los dias de la semana en la que sale un determinado vuelo separados por un guión "-" en caso de ser
	 * varios.
	 * @param <tt>Flight</tt> acerca del cual se quiere obtener los días de salida
	 * @return String con los días que sale un Flight determinado
	 */
	private static String departuresStr(Flight f) {
		List<Moment> departures = f.getDepartures();
		StringBuffer buffer = new StringBuffer();
		for(Moment m : departures) {
			buffer.append(m.getDay().toString() + "-");
		}
		buffer.deleteCharAt(buffer.length()-1);
		return buffer.toString();
	}

	/**
	 * Retorna la aerolinea correspondiente a un determinado vuelo
	 * @param <tt>Flight</tt> acerca del cual se quiere averiguar la aerolínea
	 * @return nombre de la aerolínea de un Flight determinado
	 */
	private static String airline(Flight f) {
		return f.getId().getAirline();
	}

	/**
	 * Retorna el nombre del {@link Airport} de origen de un <tt>Flight</tt> determinado
	 * @param <tt>Flight</tt> acerca del cual se quiere averiguar la aerolínea
	 * @return nombre del <tt>Airport</tt> de origen de un Flight determinado
	 */
	private static String originAirport(Flight f) {
		return f.getOrigin().toString();
	}

	/**
	 * Retorna el nombre del {@link Airport} de destino de un <tt>Flight</tt> determinado
	 * @param <tt>Flight</tt> acerca del cual se quiere averiguar la aerolínea
	 * @return nombre del <tt>Airport</tt> al que se dirige un Flight determinado
	 */
	private static String destAirport(Flight f) {
		return f.getDestination().toString();
	}

	/**
	 * Retorna la duración de un <tt>Flight</tt> determinado
	 * @param <tt>Flight</tt> acerca del cual se quiere averiguar la duración
	 * @return string representando la duracion de un vuelo en formato [xxh]yym
	 */
	private static String duration(Flight f) {
		return f.getDuration().toString();
	}

	/**
	 * Retorna la hora de salida de un <tt>Flight</tt> determinado
	 * @param <tt>Flight</tt> acerca del cual se quiere obtener el horario de salida
	 * @return string que representa la hora de partida de un vuelo el formato 24 hs.
	 */
	private static String departureTime(Flight f) {
		Time time = f.getDepartureTime();
		int minutes = time.getMinutes();
		int hours = minutes/ TimeConstants.MINUTES_PER_HOUR;
		minutes = minutes % TimeConstants.MINUTES_PER_HOUR;
		return String.format("%02d:%02d", hours ,minutes);
	}

}
