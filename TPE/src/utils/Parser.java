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

    static boolean parseRoute(Scanner sc) {
    	if(!sc.hasNext())
    		return false;
    	
    	String line = sc.nextLine();
    	sc.close();

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

    
    
    private static boolean airportInsert(Scanner sc){
    	if(!sc.hasNext())
    		return false;
    	
    	String line = sc.nextLine();
    	sc.close();
    	
        if(!RegexHelper.validateAirportInsertion(line))
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
    	sc.close();

        //  Matchea la expresion regular.
        if(!RegexHelper.validateFlightInsertion(line))
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
        
        return true;
    }

    
    

	private static boolean[] daysOfDep(String days) {
		String[] daysArr = days.split("-");
		boolean[] departs = new boolean[7];
		
		for(int i = 0 ; i < daysArr.length ; i++){
			switch(daysArr[i]){
				case "Lu":
					departs[0] = true;
				break;
				case "Ma":
					departs[1] = true;
				break;
				
				case "Mi":
					departs[2] = true;
				break;
				
				case "Ju":
					departs[3] = true;
				break;
				
				case "Vi":
					departs[4] = true;
				break;
				
				case "Sa":
					departs[5] = true;
				break;
				
				case "Do":
					departs[6] = true;
				break;
				
			}
		}
		return departs;
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
    	if(!sc.hasNext())
    		return false; //Error
    	
    	String line = sc.nextLine();
    	sc.close();
    	
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
        sc.close();
        
        if(!RegexHelper.validateFlightName(line))
        	return false;
        
        //Todo validado
        sc = new Scanner(line);
        String airline = sc.next(); 
        int flnumber = new Integer(sc.next());

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
