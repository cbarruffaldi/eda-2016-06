package utils;

/**
 * Serie de metodos estaticos usando expresiones regulares para validar la entrada
 */
public class RegexHelper implements InputConstraints {

    //Atomos para las expresiones usadas
	private static String spc = " ";
	private static String airlineName = "[a-zA-Z]{1," + AIRLINE_NAME_MAX_LENGHT + "}";
	private static String number = "[0-9]+";
	private static String days = "(Lu|Ma|Mi|Ju|Vi|Sa|Do)(-(Lu|Ma|Mi|Ju|Vi|Sa|Do))*"; // "Lu" o bien "Lu-Mi-Ju", etc
	private static String airportName = "[a-zA-Z]{" + AIRPORT_NAME_LENGHT + "}";
	private static String twentyFourHourFormat = "([01]?[0-9]|2[0-3]):[0-5][0-9]"; //00:00 - 23:59
	private static String hours = "([0-9]+h)?[0-5][0-9]m"; // [xxh]xxm
	private static String realPositiveNum = "[0-9]+(\\.[0-9]+)?";
	
	private static String latitude = "-?([0-8]?[0-9](\\.[0-9]+)?|90(\\.0+)?)"; //-90.000 - 90.0000
	private static String longitude = "-?(([0-9]|1[0-7])?[0-9](\\.[0-9]+)?|180(\\.0+)?)"; //-180.000 - 180.0000

    private static String flightValidationFromText = createFlightRegexp(spc);
	private static String flightValidationFromFile = createFlightRegexp("#");
    //Static para no armarla cada vez
    private static String flightNameFormat = airlineName + spc + number;
    //Static para no armarla cada vez
    private static String routeFormat = "src=" + airportName + spc + "dst=" + airportName
			+ spc + "priority=(ft|pr|tt)" + "( weekdays=" + days + ")?";

	private static String createFlightRegexp(String separator) {
		return airlineName + separator + number + separator + days + separator + airportName + separator + airportName +
				separator + twentyFourHourFormat + separator + hours + separator + realPositiveNum;
	}
    
	// Se usa de igual manera para leer de linea de comandos o de archivo, con la unica diferencia
	//que en los archivos se separan las palabras por medio de "#".
    public static boolean validateFlightInsertion(String line, String separator) {
		String flightFormat = (separator.equals(spc)) ? flightValidationFromText : flightValidationFromFile;
		return line.matches(flightFormat);
	}

    public static boolean validateFlightName(String line) {
    	return line.matches(flightNameFormat);
	}

    public static boolean validateAirportInsertion(String line, String separator) {
    	String airportFormat = airportName + separator + latitude + separator + longitude;
    	return line.matches(airportFormat);
	}
    
    public static boolean validateRoute(String line) {
        	return line.matches(routeFormat);
	}
    
	public static boolean validateAirportName(String line) {
		return line.matches(airportName);
	}
}
