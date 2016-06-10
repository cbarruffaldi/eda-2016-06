package flightassistant;

import utils.SaveManager;
import utils.Parser;

import java.io.IOException;
import java.util.Scanner;

public class Application {
    private static final String AirportsFileString = "airports.txt";
    private static final String FlightsFileString = "flights.txt";
    private static FlightAssistant flightAssistant;
    
    public static void main(String[] args) {
       // Por ahora lo saco porque no anda
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

    private static void exit() {
        try {
        	SaveManager.saveAirports(flightAssistant);
            SaveManager.saveFlights(flightAssistant);
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }

    private static FlightAssistant load() {
        FlightAssistant fa = new FlightAssistant();
        Parser.insertAirportsFromFile(AirportsFileString, fa);
        Parser.insertFlightsFromFile(FlightsFileString, fa);
        
        return fa;
    }

    private static boolean calledWithArgument(String[] args) {
        return args.length != 0;
    }
}
