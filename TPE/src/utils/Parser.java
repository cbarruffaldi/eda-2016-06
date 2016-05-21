package utils;

import java.util.Scanner;

// TODO statics??
public class Parser {
    public static final int AIRPORT_NAME_LENGHT = 3;
    public static final int AIRLINE_NAME_MAX_LENGHT = 3;
    public static final double MAX_LATITUDE = 90.0;
    public static final double MAX_LONGITUDE = 180.0;

    private static Scanner sc;

    public  static void main(String[] args) {
        String ej1 = "insert airport BUE -34.602535 -58.368731";
        String ej2 = "insert flight AA 1432 Lu-Ju-Ma-Sa BUE PAR 08:46 14h45m 1850.23";
     //   parse(new Scanner(ej2));
       
        String test = "123 LU BUE";
        Scanner sc = new Scanner(test);
        
        
        String flight = "AA 132 Lu-Ju-Mi-Sa BUE PAR 18:19 14h45m 1850.00";
        System.out.println(validateFlight(flight));
    }

    private static boolean validateFlight(String line) {
    	String regex = "[a-zA-Z]{1,3} [0-9]+ (Lu|Ma|Mi|Ju|Vi|Sa|Do)(-(Lu|Ma|Mi|Ju|Vi|Sa|Do))* [a-zA-Z]{3} [a-zA-Z]{3} ([01]?[0-9]|2[0-3]):[0-5][0-9] ([0-9]+h)?[0-5][0-9]m [0-9]+(\\.[0-9]+)?";
		return line.matches(regex);
	}

    
    
    static void parse(Scanner scanner) {
        sc = scanner;
        while (sc.hasNext()) {
           parseCmd();
        }
    }

    // Provisorio, se tendria que encargar un "outputmanager"
    static void errorMsg() {
        System.out.println("Entrada incorrecta");
    }
    
    

    private static void parseCmd() {
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
        }
        if (!valid) {
            errorMsg(); // llamaría al outputmanager para que mande mensaje de error
            // podría poner un mensajito de exito si fue valid
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
                //TODO
                //valid = allInsert(sc);
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
        sc.useDelimiter("");
        String n = sc.next();
        if (true);
        sc.skip("src=");
        boolean valid = sc.hasNext();
        String orig, dest, option;
        if (valid) {
            orig = sc.next();
            valid = validAirportName(orig);
        }
        sc.skip("dst=");
        if (valid = valid && sc.hasNext()) {
            dest = sc.next();
            valid = validAirportName(dest);
        }
        sc.skip("priority=");
        if (valid = valid && sc.hasNext()) {
            option = sc.next();
            valid = validOption(option);
        }
        if (sc.hasNext()) { // Los weekDays son opcionales
            String days = sc.next();
            if (valid = days.matches("\\w\\w(-\\w\\w){1,4}?")) {
                String[] strs = days.split("\\w-\\w");
                //TODO con cada s[] hago el weekday, hay que ver la implementación
            }
        }
        if (valid);
        // TODO Llama al metodo de buscar la ruta
        return valid;
    }

    // O pongo scanner como variable en la clase??
    private static boolean airportInsert(Scanner sc) {
        boolean valid = sc.hasNext();
        String name; double lat, lng;
        if (valid) {
            name = sc.next();
            valid = validAirportName(name);
        }
        if (valid = valid && sc.hasNext()) {
            lat = new Double(sc.next());
            valid = validLatitude(lat);
        }
        if (valid = valid && sc.hasNext()) {
            lng = new Double(sc.next());
            valid = validLongitude(lng);
        }
        if (valid);
        // TODO metodo de insertar aeropuerto
        // TODO podría chequear que no quede nada sin 'consumir'
        return valid;
    }

    // Se puede matchear con una expresion regular gigante y despues saco la info sin tener que validar a cada rato
    private static boolean flightInsert(Scanner sc) {
        boolean valid = sc.hasNext();
        // IDEA
        String line = sc.nextLine();
        //  Matchea la expresion regular.
        validateFlight(line);
        
        sc = new Scanner(line);
        // Si matchea sigue, y se pueden sacar todos los ifs y si no retorna false
        //
        String airline, orig, dest; int flnumber; double price;
        if (valid) {
            airline = sc.next();
            valid = validAirlineName(airline);
        }
        if (valid = valid && sc.hasNext()) {
            flnumber = new Integer(sc.next());
            valid = validFlightNumber(flnumber);
        }
        if (valid = valid && sc.hasNext()) {
            orig = sc.next();
            valid = validAirportName(orig);
        }
        if (valid = valid && sc.hasNext()) {
            dest = sc.next();
            valid = validAirportName(dest);
        }
        if (valid = valid && sc.hasNext()) {
            String days = sc.next();
            if (valid = days.matches("\\w\\w(-\\w\\w){1,4}?")) {
                String[] strs = days.split("\\w-\\w");
                //TODO con cada s[] hago el weekday, hay que ver la implementación
            }
        }
        if (valid = valid && sc.hasNext()) {
            //TODO hora salida
        }
        if (valid = valid && sc.hasNext()) {
            //TODO duracion
        }
        if (valid = valid && sc.hasNext()) {
            price = new Double(sc.next());
            valid = validPrice(price);
        }
        if (valid);
        //TODO metodo insertar vuelo

        return valid;
    }


	private static boolean validOption(String option) {
        return option.equals("ft") || option.equals("pr") || option.equals("tt");
    }

    private static boolean validAirportName(String name) {
        return name.length() != AIRPORT_NAME_LENGHT;
    }

    private static boolean validLatitude(double lat) {
        return Math.abs(lat) <= MAX_LATITUDE;
    }

    private static boolean validLongitude(double lng) {
        return Math.abs(lng) <= MAX_LONGITUDE;
    }

    private static boolean validAirlineName(String name) {
        return name.length() <= AIRLINE_NAME_MAX_LENGHT;
    }

    private static boolean validFlightNumber(int flnum) {
        return flnum > 0;
    }

    private static boolean validPrice(double price) {
        return price > 0;
    }

    //TODO
    //    private static boolean allInsert(Scanner sc) {
    //        boolean valid = false;
    //        if (!sc.hasNext()) { return false; }
    //        switch (sc.next()) {
    //            case "airport":
    //                valid = insertAirportFromFile(sc);
    //                break;
    //            case "flight":
    //                valid = insertFlightFromFile(sc);
    //                break;
    //        }
    //        return valid;
    //    }
    //
    //    private static boolean insertAirportFromFile(Scanner sc) {
    //        // Leer de archivo como para airport e insertar
    //        return false;
    //    }
    //
    //    private static boolean insertFlightFromFile(Scanner sc) {
    //        //Leer de archivo como para flight e insertar
    //        return false;
    //    }


    private static boolean airportDelete(Scanner sc) {
        boolean valid = sc.hasNext();
        String name;
        // Repite esto en airportInsert
        if (valid) {
            name = sc.next();
            valid = validAirportName(name);
        }
        //TODO metodo borrar aeropuerto.
        return valid;
    }

    private static boolean flightDelete(Scanner sc) {
        boolean valid = sc.hasNext();
        String airline; int flnumber;
        // Repite esto de flight insert
        if (valid) {
            airline = sc.next();
            valid = validAirlineName(airline);
        }
        if (valid = valid && sc.hasNext()) {
            flnumber = new Integer(sc.next());
            valid = validFlightNumber(flnumber);
        }
        //TODO metodo de borrar vuelo
        return valid;
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