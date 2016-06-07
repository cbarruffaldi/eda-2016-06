package utils;

import flightassistant.Airport;
import flightassistant.Flight;
import flightassistant.FlightAssistant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

public class FileManager2 {
	
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
	
	public static void saveAirports(FlightAssistant fa) throws FileNotFoundException{
		PrintStream out = new PrintStream(new File("airports.txt"));
		Iterator<Airport> it = fa.getAirports().valueIterator();
		while(it.hasNext()) {
			Airport airport = it.next();
			out.println(airport.getId() + "#" + airport.getLatitude() + "#" + airport.getLongitude());
		}
		out.close();
	}
	
	
	private static String departuresStr(Flight f) {
		List<Moment> departures = f.getDepartures();
		StringBuffer buffer = new StringBuffer();
		for(Moment m : departures) {
			buffer.append(m.getDay().toString() + "-");			
		}
		buffer.deleteCharAt(buffer.length()-1);
		return buffer.toString();
	}
	
	private static String airline(Flight f) {
		return f.getId().getAirline();
	}
	
	private static String originAirport(Flight f) {
		return f.getOrigin().toString();
	}
	
	private static String destAirport(Flight f) {
		return f.getDestination().toString();
	}
	
	private static String duration(Flight f) {
		return f.getDuration().toString();
	}
	
	private static String departureTime(Flight f) {
		Time time = f.getDepartureTime();
		int minutes = time.getMinutes();
		int hours = minutes/ TimeConstants.MINUTES_PER_HOUR;
		minutes = minutes % TimeConstants.MINUTES_PER_HOUR;
		return String.format("%02d:%02d", hours ,minutes);
		
	}

}
