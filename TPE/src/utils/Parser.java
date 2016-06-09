package utils;

import flightassistant.FlightAssistant;
import flightassistant.Ticket;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Clase que procesa y parsea la entrada, tanto la estándar como por un archivo de texto,
 * y ejecuta los comandos ingresados en un {@link FlightAssistant} dado. Realiza todas las validaciones
 * en cuanto al contenido de los comandos, y también imprime mensajes según corresponda.
 * @see OutputManager
 * @see RegexHelper
 * @see FileManager
 */
public class Parser{

    private static final Pattern numeralPatt = Pattern.compile("#");
    private static final Pattern spacePatt = Pattern.compile("(\\s)*");

    /**
     * {@link Pattern} que representa cualquier caracter.
     */
    private static final Pattern allPatt = Pattern.compile(".*");

    /**
     * {@link FlightAssistant} sobre el cual se ejecutan los comandos ingresados.
     */
    private static FlightAssistant flightAssistant;

    /**
     * Expresa si se usó el comando "exitAndClose" para terminar la ejecución del programa.
     */
    private static boolean hasEnded = false;

    /**
     * {@link OutputManager} que se usa para manejar la impresión de la salida del parser.
     */
    private static OutputManager Output = new OutputManager();

    /**
     * Parsea los argumentos cuando estos se ingresan por entrada estándar (comandos shell).
     * @param sc {@link Scanner} del texto ingresado.
     * @param fa {@link FlightAssistant} sobre el que se ejecutan los comandos.
     * @return
     */
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
            case "exitAndSave":
                valid = true;
                hasEnded = true;
                break;
        }

        if (!valid) {
            Output.invalidCommand();
            consumeScanner(sc);
        }
    }

    /**
     * Consume el resto del texto pendiente en el Scanner.
     * @param sc {@link Scanner} a vaciar.
     */
    private static void consumeScanner(Scanner sc) {
        sc.skip(allPatt);
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

        String line = restOfLine(sc);

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

        List<Day> days = new LinkedList<>();
        if (sc.hasNext()) { // Los weekDays son opcionales
            sc.skip(" weekdays=");
            days = getDaysFromStr(sc.next());
            // Si no hay weekDays "days" es una lista vacía.
        }
        List<Ticket> path = findPathWithOption(option, orig, dest, days);
        if (path == null || path.isEmpty()) {
            Output.notFoundMsg();
        } else {
            Output.printBestRoute(path);
        }
        return true;
    }

    private static List<Ticket> findPathWithOption(String option, String orig, String dest, List<Day> days) {
        List<Ticket> path = new LinkedList<>(); // para que no tire warning
        long tick, tock;
        switch (option) {
            case "ft":
            	tick = System.currentTimeMillis();
                path = flightAssistant.findQuickestPath(orig, dest, days);
                tock = System.currentTimeMillis();
                System.err.println("Time:" + (tock-tick) / 1000.0 + "s");
                break;
            case "pr":
            	tick = System.currentTimeMillis();
                path = flightAssistant.findCheapestPath(orig, dest, days);
                tock = System.currentTimeMillis();
                System.err.println("Time: " +(tock-tick) / 1000.0 + "s");
                break;
            case "tt":
            	tick = System.currentTimeMillis();
                path = flightAssistant.findShortestTotalTimeRoute(orig, dest, days);
            	tock = System.currentTimeMillis();
                System.err.println("Time: " + (tock-tick) / 1000.0 + "s");
                break;
            default:
                throw new IllegalArgumentException("Invalid Option");
        }
        return path;
    }

    private static boolean parseOutputFormat(Scanner sc) {
        if (!sc.hasNext()) { return false; }
        String format = sc.next();

        switch (format) {
            case "text":
                Output.setToTextFormat();
                break;
            case "KML":
                Output.setToKMLFormat();
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
                Output.setToStdOutput();
                break;
            case "file":
                if (!sc.hasNext()) {
                    return false;
                } else {
                    Output.setToFileOutput(sc.next());
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
                valid = insertFromFile(sc.next(), true);
                break;
            case "flight":
                valid = insertFromFile(sc.next(), false);
                break;
        }
        return valid;
    }

    private static boolean airportInsert(Scanner sc) {
        if (!sc.hasNext())
            return false;

        String line = restOfLine(sc);

        if (!RegexHelper.validateAirportInsertion(line, sc.delimiter().toString())) {
            return false; //Error en el formato
        }

        Pattern delimiter = sc.delimiter();
        sc = new Scanner(line);
        sc.useDelimiter(delimiter);
        String name = sc.next();
        double lat = new Double(sc.next());
        double lng = new Double(sc.next());

        flightAssistant.insertAirport(name, lat, lng);
        consumeScanner(sc);
        return true;
    }

    private static boolean flightInsert(Scanner sc) {
    	if(!sc.hasNext())
    		return false;

    	String line = restOfLine(sc);

        //  Matchea la expresion regular.
        if(!RegexHelper.validateFlightInsertion(line, sc.delimiter().toString()))
        	return false; //Salir, algo esta mal escrito

        Pattern delimiter = sc.delimiter();
        sc = new Scanner(line);
        sc.useDelimiter(delimiter);

        String airline = sc.next();
        int flnumber = new Integer(sc.next());
        String days = sc.next();
        String orig = sc.next();
        String dest = sc.next();
        String timeOfDeparture = sc.next();
        String durationOfFlightStr = sc.next();
        double price = new Double(sc.next());

        Time depTime = new Time(timeOfDeparture);
        List<Moment> departures = departures(days, depTime);
        int duration = getDuration(durationOfFlightStr);

        flightAssistant.insertFlight(airline, flnumber, price, departures, new Time(duration), orig, dest);
        consumeScanner(sc);
        return true;
    }

    private static boolean airportDelete(Scanner sc) {
    	if(!sc.hasNext())
    		return false; //Error

        String name = restOfLine(sc);

    	if(!RegexHelper.validateAirportName(name))
    		return false;

        flightAssistant.removeAirport(name);
        return true;
    }

    private static boolean flightDelete(Scanner sc) {
        if(!sc.hasNext())
        	return false;

        String line = restOfLine(sc);

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
        int hour = 0, min;
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

    private static List<Day> getDaysFromStr(String str) {
        String[] daysArr = str.split("-");
        LinkedList<Day> ans = new LinkedList<>();
        for (String s : daysArr) {
            ans.add(Day.getDay(s));
        }
        return ans;
    }

	private static List<Moment> departures(String days, Time timeOfDep) {
		LinkedList<Moment> departs = new LinkedList<>();
        for (Day dayOfDep: getDaysFromStr(days)) {
            departs.add(new Moment(dayOfDep, timeOfDep));
        }
		return departs;
	}

	public static void insertAirportsFromFile(String fileName, FlightAssistant fa) {
		flightAssistant = fa;
		insertFromFile(fileName, true);
		flightAssistant = null;
	}

	public static void insertFlightsFromFile(String fileName, FlightAssistant fa) {
		flightAssistant = fa;
		insertFromFile(fileName, false);
		flightAssistant = null;
	}

    //Ahora pasa un booleano para indicar si se agrega aeropuerto o vuelo (no aeropuerto).
    private static boolean insertFromFile(String pathToFile, boolean insertAirport) {
        boolean valid = true;
        try {
            Scanner fileSc = new Scanner(new File(pathToFile));
            fileSc.useDelimiter(numeralPatt);

            // Lo único que cambia en leer de archivo es que separa "#" en lugar de " ".
            while (valid && fileSc.hasNextLine()) {
            	if (insertAirport)
            		valid = airportInsert(fileSc);
            	else
            		valid = flightInsert(fileSc);
            }
        } catch (FileNotFoundException e) {
            Output.fileOpenErrorMsg();
        }
        return valid;
    }


    /**
     * Parsea los argumentos cuando estos se ingresan como parámetros del programa.
     * @param cmd conjunto de argumentos.
     * @param fa {@link FlightAssistant} sobre el que se ejecutan los comandos.
     */
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
            Output.invalidCommand();
        }
    }

    private static boolean parseAirportArgument(String[] cmd) {
        if (cmd.length != 3) return false;
        Scanner filePathSc = new Scanner(cmd[1]);
        boolean ans = false;
        switch (cmd[2]) {
            case "--append-airports":
                insertFromFile(filePathSc.next(), true);
                ans = true;
                break;
            case "--replace-airports":
                flightAssistant.removeAllAirports(); // Borro antes de insertar
                insertFromFile(filePathSc.next(), true);
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
                insertFromFile(filePathSc.next(), false);
                ans = true;
                break;
            case "--replace-flights":
                flightAssistant.removeAllFlights(); // Borro antes de insertar
                insertFromFile(filePathSc.next(), false);
                ans = true;
                break;
        }
        return ans;
    }

    /**
     * Avanza el {@link Scanner} hasta el final de la línea y devuelve toda esa línea sin
     * espacios el principio.
     * @return resto de la línea
     */
    // Avanza el scanner hasta el final de la linea y devuelve toda esa línea sin espacios al principio.
    private static String restOfLine(Scanner sc) {
        sc.skip(spacePatt);
        return sc.nextLine();
    }
}
