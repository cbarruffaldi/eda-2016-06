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
	private static String integer = "[0-9]+";
	private static String days = "(Lu|Ma|Mi|Ju|Vi|Sa|Do)(-(Lu|Ma|Mi|Ju|Vi|Sa|Do))*"; // "Lu" o bien "Lu-Mi-Ju", etc
	private static String airportName = "[a-zA-Zn—]{" + AIRPORT_NAME_LENGHT + "}";
	private static String twentyFourHourFormat = "([01]?[0-9]|2[0-3]):[0-5][0-9]"; //00:00 - 23:59
	private static String hours = "([0-9]+h)?[0-5][0-9]m"; // [xxh]xxm
	private static String realNum = "[0-9]+(\\.[0-9]+)?";
	
	private static String latitude = "-?[0-8]?[0-9](\\.[0-9]+)? | 90(\\.0+)?"; //0.000 - 90.0000
	private static String longitude = "-?([0-9]|1[0-7])(\\.[0-9]+)? | 180(\\.0+)?"; //0.000 - 180.0000

	
	
	
    public static boolean validateFlight(String line) {
    	String flightFormat = airlineName + spc + integer + spc + days + spc + airportName + spc + airportName +
    			spc + twentyFourHourFormat + spc + hours + spc + realNum;
    	//String regex = "[a-zA-Z]{1,3} [0-9]+ (Lu|Ma|Mi|Ju|Vi|Sa|Do)(-(Lu|Ma|Mi|Ju|Vi|Sa|Do))* [a-zA-ZÒ—]{3} [a-zA-ZÒ—]{3} ([01]?[0-9]|2[0-3]):[0-5][0-9] ([0-9]+h)?[0-5][0-9]m [0-9]+(\\.[0-9]+)?";

    	return line.matches(flightFormat);
	}
    
    public static void main(String[] args) {
        String flight = "AA 132 Lu-Ju-Mi-Sa BUE P—R 18:19 14h45m 1850.00";
        System.out.println(validateFlight(flight));
        
        String coordinate = "-34.602535 -58.368731";
        System.err.println(coordinate.matches(latitude + spc + longitude));
        		
        
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
