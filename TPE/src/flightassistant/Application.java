package flightassistant;

import utils.SaveManager;
import utils.Parser;

import java.io.IOException;
import java.util.Scanner;

/**
 * Clase principal del programa. Se encarga de cargar dos archivos preestablecidos,
 *  uno con los {@link Flight}s y otro con los {@link Airport}s.
 * Además llama a la clase {@link Parser} que se encarga de leer lo ingresado por el 
 * usuario por entrada estándar.
 * Y finalmente cerrar la aplicacion guardando los datos en los archivos preestablecidos.
 * @see Parser
 * @see FlightAssistant
 * @see SaveManager
 *
 */
public class Application {
    private static final String AirportsFileString = "airports.txt";
    private static final String FlightsFileString = "flights.txt";
    private static FlightAssistant flightAssistant;

    public static void main(String[] args) {
        flightAssistant = load();	//carga el flightAssistant
    	
    	if (calledWithArgument(args)) {
            Parser.parseArguments(args, flightAssistant);
        } else {
            //flightAssistant = new FlightAssistant();
            Scanner sc = new Scanner(System.in);
            System.out.println("Done loading");
            while (!Parser.parseShell(sc, flightAssistant));
            // el parser devuelve si se usó el comando para terminar el programa o no
            sc.close();
        }
    	exit(); 	//en ambos casos guarda y termina.
    }

    /**
     * Guarda los datos de vuelos y aeropuertos ingresados o modificados
     * durante la ejecucion de la aplicación en archivos. Finaliza la ejecucion de la
     * aplicación
     */
    private static void exit() {
        try {
        	SaveManager.saveAirports(flightAssistant);
            SaveManager.saveFlights(flightAssistant);
        } catch (IOException e) {
        	e.printStackTrace();
            System.out.println("Error");
         
        }
    }

    /**
     * Carga en el {@link FlightAssistant} los datos de los archivos "airports.txt" y "flights.txt"
     * para iniciar correctamente su ejecución.
     * @return <tt>FlighAssistant</tt> con los datos ya cargados.
     */
    private static FlightAssistant load() {
        FlightAssistant fa = new FlightAssistant();
        Parser.insertAirportsFromFile(AirportsFileString, fa);
        Parser.insertFlightsFromFile(FlightsFileString, fa);
        
        return fa;
    }
    
    /**
     * Informa si la aplicación fue ejecutada con parámetros
     * @param argumentos con las que fue llamada la aplicacion
     * @return true si se recibieron argumentos, false sino
     */

    private static boolean calledWithArgument(String[] args) {
        return args.length != 0;
    }
}
