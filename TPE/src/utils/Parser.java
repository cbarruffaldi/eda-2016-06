package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// TODO statics??
public class Parser {
    public static final int AIRPORT_NAME_LENGHT = 3;
    public static final int AIRLINE_NAME_MAX_LENGHT = 3;
    public static final double MAX_LATITUDE = 90.0;
    public static final double MAX_LONGITUDE = 180.0;
    public static final String[] daysOfWeek = {"Lu", "Ma", "Mi", "Ju", "Vi", "Sa", "Do"};

    public  static void main(String[] args) {
        String ej1 = "insert airport BUE -34.602535 -58.368731";
        String ej2 = "insert flight AA 1432 Lu-Ju-Ma-Sa BUE PAR 08:46 14h45m 1850.23";
     //   parse(new Scanner(ej2));

        String test = "123 LU BUE";
        Scanner sc = new Scanner(test);
    }

    static void parse(Scanner sc) {
        while (sc.hasNext()) {
           parseFunction(sc);
        }
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
                valid = parseOutputType(sc); // TODO: Type??
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

        // TODO metodo de insertar aeropuerto

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
        String durationOfFlight = sc.next();
        double price = new Double(sc.next());

        //Procesar las cosas
        boolean[] daysOfDeparture = daysOfDep(days);
        // TODO metodo de insertar vuelo
        return true;
    }


	private static boolean[] daysOfDep(String days) {
		String[] daysArr = days.split("-");
		boolean[] departs = new boolean[7];

		for(int i = 0 ; i < daysArr.length ; i++) {
            for (int j = 0; j < daysOfWeek.length; j++) {
                if (daysArr[i].equals(daysOfWeek[j])) {
                    departs[j] = true;
                }
            }
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

    	String line = sc.nextLine();

    	if(!RegexHelper.validateAirportName(line))
    		return false;

    	//Else en line quedo el nombre bien
    	//TODO metodo borrar aeropuerto.
        return true;
    }



    private static boolean flightDelete(Scanner sc) {
        if(!sc.hasNext())
        	return false;

        String line = sc.next();

        if(!RegexHelper.validateFlightName(line))
        	return false;

        //Todo validado
        sc = new Scanner(line);
        String airline = sc.next();
        int flnumber = new Integer(sc.next());

        //TODO metodo borrar vuelo

        return true;
    }

    private static boolean allDelete(Scanner sc) {
        boolean valid = true;
        if (!sc.hasNext()) { return false; }
        switch (sc.next()) {
            case "airport":
                //TODO metodo de borrar todos los aeropuertos
                break;
            case "flight":
                //TODO metodo de borrar todos los vuelos
                break;
            default:
                valid = false;
                break;
        }
        return valid;
    }
}
