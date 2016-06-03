package utils;

import flightassistant.FlightAssistant;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public class Parser{
    private static final String[] daysOfWeek = {"Lu", "Ma", "Mi", "Ju", "Vi", "Sa", "Do"};
    private static FlightAssistant flightAssistant;
    private static boolean hasEnded = false; // Dice si usaron el comando exitAndClose. Pensar mejor solución.
    private static boolean stdOut = true; // Por defecto a salida estandar

    // ARGUMENTOS SHELL

    public static boolean parseShell(Scanner sc, FlightAssistant fa) {
        flightAssistant = fa;
        if (flightAssistant == null) {
            throw new IllegalStateException("No FlightAssistant to execute commands on");
        }
        if (sc.hasNext())
            parseFunction(sc);
        flightAssistant = null;
        return hasEnded;
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
                valid = parseOutputTarget(sc);
                break;
            case "exitAndClose":
                valid = true;
                hasEnded = true;
                break;
        }

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

        sc.skip(" ");
        String line = sc.nextLine();

    	if(!RegexHelper.validateRoute(line))
    		return false; //Mal formato

    	sc = new Scanner(line);

        String orig, dest, option;

        sc.skip("src=");
        orig = sc.next();

        sc.skip(" dst=");
        dest = sc.next();

        sc.skip(" priority=");
        option = sc.next();

        LinkedList<Day> days;
        if (sc.hasNext()) { // Los weekDays son opcionales
            sc.skip(" weekdays=");
            days = getDaysFromStr(sc.next());
            // Si no hay weekDays "days" queda en null.
        }

        // TODO Llama al metodo de buscar la ruta
        // Se supone que le devuelve una lista con los aeropuertos y eso se lo manda al outputmanager a que lo imprima
        // OutputManager.printBestRoute(airports);
        return true;
    }

    private static boolean parseOutputFormat(Scanner sc) {
        if (!sc.hasNext()) { return false; }
        String format = sc.next();

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

    private static boolean parseOutputTarget(Scanner sc) {
        if (!sc.hasNext()) { return false; }
        String type = sc.next();
        switch (type) {
            case "stdout":
                OutputManager.setToStdOutput();
                break;
            case "file":
                if (!sc.hasNext()) {
                    return false;
                } else {
                    OutputManager.setToFileOutput(sc.next());
                }
                break;
            default:
                return false;
        }
        return true;
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

    private static boolean airportInsert(Scanner sc) {
        if (!sc.hasNext())
            return false;

        sc.skip(" ");
        String line = sc.nextLine();

        if (!RegexHelper.validateAirportInsertion(line, sc.delimiter().toString())) {
            return false; //Error en el formato
        }

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

        sc.skip(" ");
    	String line = sc.nextLine();

        //  Matchea la expresion regular.
        if(!RegexHelper.validateFlightInsertion(line, sc.delimiter().toString()))
        	return false; //Salir, algo esta mal escrito

        sc = new Scanner(line);

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

    private static boolean airportDelete(Scanner sc) {
    	if(!sc.hasNext())
    		return false; //Error

        sc.skip(" ");
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

    private static int getDuration(String durationStr) {
        Scanner auxSc = new Scanner(durationStr);
        int hour = 0, min = 0;
        if (durationStr.contains("h")) {
            auxSc.useDelimiter("h");
            hour = auxSc.nextInt();
            auxSc.skip("h");
        }
        auxSc.useDelimiter("m");
        min = auxSc.nextInt();
        auxSc.close();
        return hour * TimeConstants.MINUTES_PER_HOUR + min;
    }

    private static LinkedList<Day> getDaysFromStr(String str) {
        String[] daysArr = str.split("-");
        LinkedList<Day> ans = new LinkedList<>();
        for (String s : daysArr) {
            ans.add(Day.getDay(s));
        }
        return ans;
    }

	private static LinkedList<Moment> departures(String days, Time timeOfDep) {
		LinkedList<Moment> departs = new LinkedList<>();
        for (Day dayOfDep: getDaysFromStr(days)) {
            departs.add(new Moment(dayOfDep, timeOfDep));
        }
		return departs;
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

    // ARGUMENTOS POR LINEA DE COMANDOS.

    public static void parseArguments(String[] cmd, FlightAssistant fa) {
        boolean valid = false;
        flightAssistant = fa;
        switch (cmd[0]) {
            case "--airport-file":
                valid = parseAirportArgument(cmd);
                break;
            case "--flight-file":
                valid = parseFlightArgument(cmd);
                break;
            case "--delete-airports":
                if (valid = (cmd.length == 1))
                    flightAssistant.removeAllAirports();
                break;
            case "--delete-flights":
                if (valid = (cmd.length == 1))
                    flightAssistant.removeAllFlights();
                break;
        }
        if (!valid) {
            OutputManager.invalidCommand();
        }
    }

    // TODO Ver como no repetir codigo entre esta y la misma pero de los vuelos
    private static boolean parseAirportArgument(String[] cmd) {
        if (cmd.length != 3) return false;
        Scanner filePathSc = new Scanner(cmd[1]);
        boolean ans = false;
        switch (cmd[2]) {
            case "--append-airports":
                insertFromFile(filePathSc, true);
                ans = true;
                break;
            case "--replace-airports":
                flightAssistant.removeAllAirports(); // Borro antes de insertar
                insertFromFile(filePathSc, true);
                ans = true;
                break;
        }
        return ans;
    }

    private static boolean parseFlightArgument(String[] cmd) {
        if (cmd.length != 3) return false;
        Scanner filePathSc = new Scanner(cmd[1]);
        boolean ans = false;
        switch (cmd[2]) {
            case "--append-flights":
                insertFromFile(filePathSc, false);
                ans = true;
                break;
            case "--replace-flights":
                flightAssistant.removeAllFlights(); // Borro antes de insertar
                insertFromFile(filePathSc, false);
                ans = true;
                break;
        }
        return ans;
    }
}
