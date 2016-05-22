package utils;

import java.util.Scanner;

/**
 * Serie de metodos estaticos usando expresiones regulares para validar la entrada
 * @author Marcelo
 *
 */
public class RegexHelper {
	
    public static final int AIRPORT_NAME_LENGHT = 3;
    public static final int AIRLINE_NAME_MAX_LENGHT = 3;
    public static final double MAX_LATITUDE = 90.0;
    public static final double MAX_LONGITUDE = 180.0;

	private static String spc = " ";
	private static String airlineName = "[a-zA-Z]{1," + AIRLINE_NAME_MAX_LENGHT + "}";
	private static String number = "[0-9]+";
	private static String days = "(Lu|Ma|Mi|Ju|Vi|Sa|Do)(-(Lu|Ma|Mi|Ju|Vi|Sa|Do))*"; // "Lu" o bien "Lu-Mi-Ju", etc
	private static String airportName = "[a-zA-Zn�]{" + AIRPORT_NAME_LENGHT + "}";
	private static String twentyFourHourFormat = "([01]?[0-9]|2[0-3]):[0-5][0-9]"; //00:00 - 23:59
	private static String hours = "([0-9]+h)?[0-5][0-9]m"; // [xxh]xxm
	private static String realPositiveNum = "[0-9]+(\\.[0-9]+)?";
	
	private static String latitude = "-?([0-8]?[0-9](\\.[0-9]+)?|90(\\.0+)?)"; //0.000 - 90.0000
	private static String longitude = "-?(([0-9]|1[0-7])?[0-9](\\.[0-9]+)?|180(\\.0+)?)"; //0.000 - 180.0000

    // Se usa de igual manera para leer de línea de comandos o de archivo, con la única diferencia que en
    // los archivos se separan las palabras por medio de "#".
    public static boolean validateFlightInsertion(String line, String separator) {
    	String flightFormat = airlineName + separator + number + separator + days + separator + airportName + separator + airportName +
    			separator + twentyFourHourFormat + separator + hours + separator + realPositiveNum;
    	
    	//String regex = "[a-zA-Z]{1,3} [0-9]+ (Lu|Ma|Mi|Ju|Vi|Sa|Do)(-(Lu|Ma|Mi|Ju|Vi|Sa|Do))* [a-zA-Z��]{3} [a-zA-Z��]{3} ([01]?[0-9]|2[0-3]):[0-5][0-9] ([0-9]+h)?[0-5][0-9]m [0-9]+(\\.[0-9]+)?";

    	return line.matches(flightFormat);
	}
    
    public static boolean validateFlightName(String line) {
    	String flightNameFormat = airlineName + spc + number;
    	
    	return line.matches(flightNameFormat);
	}

    public static boolean validateAirportInsertion(String line, String separator) {
    	String airportFormat = airportName + separator + latitude + separator + longitude;
    	return line.matches(airportFormat);
	}
    

    //TODO: Testing
    public static boolean validateRoute(String line) {
    	String routeFormat = "src=" + airportName + spc + "dst=" + airportName 
    			+ spc + "(priority=(ft|pr|tt))*" + "( weekdays=" + days + ")?";
    	
    	return line.matches(routeFormat);
	}
    
	public static boolean validateAirportName(String line) {
		return line.matches(airportName);
	}

   
    public static void main(String[] args) {
        String flight = "AA 132 Lu-Ju-Mi-Sa BUE P�R 18:19 14h45m 1850.00";
        System.out.println(validateFlightInsertion(flight, spc));
        
        String coordinate = "-34.602535 -58.368731";
        
        System.err.println(coordinate.matches(latitude + spc + longitude));
        		
        
        String route = "src=BUE dst=LON priority=pr weekdays=Lu-Mi-Vi";
        
        //System.err.println(Parser.parseRoute(new Scanner(route)));
        
        Scanner sc = new Scanner(flight);
        
        
        String airline = sc.next();
        int flnumber = new Integer(sc.next());
        String days = sc.next();
        String orig = sc.next();
        String dest = sc.next();
        String timeOfDeparture = sc.next();
        String durationOfFlight = sc.next();
        double price = new Double(sc.next());
        
        
       System.out.println(airline);
       System.out.println(flnumber);
       System.out.println(days);
       System.out.println(orig);
       System.out.println(dest);
       System.out.println(timeOfDeparture);
       System.out.println(durationOfFlight);
       System.out.println(price);

    }

    
    

}
