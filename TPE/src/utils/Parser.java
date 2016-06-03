package utils;

import flightassistant.FlightAssistant;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Parser{
    private static final String[] daysOfWeek = {"Lu", "Ma", "Mi", "Ju", "Vi", "Sa", "Do"};
    private static FlightAssistant flightAssistant;

    // TEST
    public  static void main(String[] args) {
        String ej1 = "insert airport BUE -34.602535 -58.368731";
        String ej2 = "insert flight AA 1432 Lu-Ju-Ma-Sa BUE PAR 08:46 14h45m 1850.23";
     //   parse(new Scanner(ej2));

        String test = "123 LU BUE";
        Scanner sc = new Scanner(test);
    }

    public static void parseShell(Scanner sc, FlightAssistant fa) {
        flightAssistant = fa;
        if (flightAssistant == null) {
            throw new IllegalStateException("No FlightAssistant to execute commands on");
        }
        while (sc.hasNext()) {
           parseFunction(sc);
        }
        flightAssistant = null;
    }

    private static void parseFunction(Scanner sc) {
        String str = sc.next();
        boolean valid = false;
        switch(str) {
            case "insert":
                valid = parseInsert(sc);
                break;
            case "delete":
                valid = parseDelete(sc);
                break;
            case "findRoute":
                valid = parseRoute(sc);
                break;
            case "outputFormat":
                valid = parseOutputFormat(sc);
                break;
            case "output":
                valid = parseOutputType(sc);
                break;
        }
        sc.close();

        if (!valid) {
            OutputManager.invalidCommand();
        }
    }

    private static boolean parseInsert(Scanner sc) {
        boolean valid = false;
        switch(sc.next()) {
            case "airport":
                valid = airportInsert(sc);
                break;
            case "flight":
                valid = flightInsert(sc);
                break;
            case "all":
                valid = fileInsert(sc);
                break;
        }
        return valid;
    }

    //repito el switch del insert pero llamando metodos de delete
    private static boolean parseDelete(Scanner sc) {
        boolean valid = false;
        switch(sc.next()) {
            case "airport":
                valid = airportDelete(sc);
                break;
            case "flight":
                valid = flightDelete(sc);
                break;
            case "all":
                valid = allDelete(sc);
                break;
        }
        return valid;
    }

    private static boolean parseRoute(Scanner sc) {
    	if(!sc.hasNext())
    		return false;

    	String line = sc.nextLine();

    	if(!RegexHelper.validateRoute(line))
    		return false; //Mal formato

    	sc = new Scanner(line);

        String orig,dest, option, days;

        sc.skip(" src=");
        orig = sc.next();

        sc.skip(" dst=");
        dest = sc.next();

        sc.skip(" priority=");
        option = sc.next();


        if (sc.hasNext()) { // Los weekDays son opcionales
        	sc.skip(" weekdays=");
        	days = sc.next();
        }else{
        	days = "";
        	}

        //flightAssistant.findRoute
        // TODO Llama al metodo de buscar la ruta
        return true;
    }

    private static boolean parseOutputFormat(Scanner sc) {
        if (!sc.hasNext()) { return false; }
        String format = sc.nextLine();

        switch (format) {
            case "text":
                OutputManager.setToTextFormat();
                break;
            case "KML":
                OutputManager.setToKMLFormat();
                break;
            default:
                return false;
        }
        return true;
    }

    private static boolean parseOutputType(Scanner sc) {
        if (!sc.hasNext()) { return false; }
        String type = sc.nextLine();

        switch (type) {
            case "stdout":
                // Que el assistant se guarde esta preferencia
                break;
            case "KML":
                // idem
                break;
            default:
                return false;
        }
        return true;
    }

    private static boolean airportInsert(Scanner sc){
    	if(!sc.hasNext())
    		return false;

    	String line = sc.nextLine();
    	//sc.close(); Me lo cerraría cuando leo de archivo.

        if(!RegexHelper.validateAirportInsertion(line, sc.delimiter().toString()))
        	return false; //Error en el formato

        sc = new Scanner(line);
        String name = sc.next();
        double lat = new Double(sc.next());
        double lng = new Double(sc.next());

        flightAssistant.insertAirport(name, lat, lng);
        return true;
    }

    private static boolean flightInsert(Scanner sc) {
    	if(!sc.hasNext())
    		return false;

    	String line = sc.nextLine();

        //  Matchea la expresion regular.
        if(!RegexHelper.validateFlightInsertion(line, sc.delimiter().toString()))
        	return false; //Salir, algo esta mal escrito

        sc = new Scanner(line); //Tambien podria hacerse un split

        String airline = sc.next();
        int flnumber = new Integer(sc.next());
        String days = sc.next();
        String orig = sc.next();
        String dest = sc.next();
        String timeOfDeparture = sc.next();
        String durationOfFlightStr = sc.next();
        double price = new Double(sc.next());

        Time depTime = Time.getTimeFromString(timeOfDeparture);
        LinkedList<Moment> departures = departures(days, depTime);
        int duration = getDuration(durationOfFlightStr);

        flightAssistant.insertFlight(airline, flnumber, price, departures, new Time(duration), orig, dest);
        return true;
    }

    private static int getDuration(String durationStr) {
        Scanner auxSc = new Scanner(durationStr);
        int hour = 0, min = 0;
        if (durationStr.contains("h")) {
            auxSc.useDelimiter("h");
            hour = auxSc.nextInt();
        }
        auxSc.useDelimiter("m");
        min = auxSc.nextInt();
        auxSc.close();
        return hour * TimeConstants.MINUTES_PER_HOUR + min;
    }

	private static LinkedList<Moment> departures(String days, Time timeOfDep) {
		String[] daysArr = days.split("-");
		LinkedList<Moment> departs = new LinkedList<>();

        for (String s : daysArr) {
            Day dayOfDep = Day.getDay(s);
            departs.add(new Moment(dayOfDep, timeOfDep));
        }
		return departs;
	}

    private static boolean fileInsert(Scanner sc) {
        boolean valid = false;
        if (!sc.hasNext()) { return false; }
        switch (sc.next()) {
            case "airport":
                valid = insertFromFile(sc, true);
                break;
            case "flight":
                valid = insertFromFile(sc, false);
                break;
        }
        return valid;
    }

    //Ahora pasa un booleano para indicar si se agrega aeropuerto o vuelo (no aeropuerto).
    private static boolean insertFromFile(Scanner sc, boolean insertAirport) {
        if (!sc.hasNext()) { return false; }
        String pathToFile = sc.next();

        boolean valid = true;
        try {
            Scanner fileSc = new Scanner(new File(pathToFile));
            fileSc.useDelimiter("#");

            // Lo único que cambia en leer de archivo es que separa "#" en lugar de " ".
            while (valid && fileSc.hasNextLine()) {
            	if (insertAirport)
            		valid = airportInsert(fileSc);
            	else
            		valid = flightInsert(fileSc);
            }
        } catch (FileNotFoundException e) {
            OutputManager.fileOpenErrorMsg();
        }
        return valid;
    }

    private static boolean airportDelete(Scanner sc) {
    	if(!sc.hasNext())
    		return false; //Error

    	String name = sc.nextLine();

    	if(!RegexHelper.validateAirportName(name))
    		return false;

        flightAssistant.removeAirport(name);
        return true;
    }



    private static boolean flightDelete(Scanner sc) {
        if(!sc.hasNext())
        	return false;

        String line = sc.next();

        if(!RegexHelper.validateFlightName(line))
        	return false;

        //validado
        sc = new Scanner(line);
        String airline = sc.next();
        int flnumber = new Integer(sc.next());

        flightAssistant.removeFlight(airline, flnumber);
        return true;
    }

    private static boolean allDelete(Scanner sc) {
        boolean valid = true;
        if (!sc.hasNext()) { return false; }
        switch (sc.next()) {
            case "airport":
                flightAssistant.removeAllAirports();
                break;
            case "flight":
                flightAssistant.removeAllFlights();
                break;
            default:
                valid = false;
                break;
        }
        return valid;
    }
}
