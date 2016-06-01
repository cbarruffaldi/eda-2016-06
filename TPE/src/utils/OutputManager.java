package utils;

import flightassistant.Airport;
import flightassistant.Flight;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * Created by Bianchi on 30/5/16.
 */
public class OutputManager {

    private static boolean textFormat = true;   // Por defecto modo texto. Sino lo podemos hacer con enums o de alguna otra forma

    public static void invalidCommand() {
        System.err.println("Invalid Command");
    }

    public static void notFoundMsg() {
        System.out.println("NotFound");
    }

    public static void fileOpenErrorMsg() {
        System.err.println("Could not open/read file");
    }

    public static void printToStdout(List<Airport> airports) {
        print(airports, System.out);
    }

    public static void printToFile(List<Airport> airports, String fileName) {
        try {
            print(airports, new PrintStream(new FileOutputStream(fileName)));
        } catch (FileNotFoundException e) {
            System.err.println("Error writing/reading file");
        }
    }

    private static void print(List<Airport> airports, PrintStream out) {
        if (textFormat == true) {
            printText(airports, out);
        } else {
            printKML(airports, out);
        }
    }

    private static void printText(List<Airport> airports, PrintStream out) {
        RouteData data = new RouteData(airports);
        out.println("Precio#" + data.price + '\n' + "TiempoVuelo#" + data.fltime + '\n'
                + "TiempoTotal#" + data.totalTime);

        out.print(airports.get(0) + "#");
        for (int i = 1; i < airports.size() - 1; i++) {
            Airport curr = airports.get(i);
            out.println(curr.getIncident() + "#" + curr);
            out.print(curr + "#");
        }
        Airport curr = airports.get(airports.size() - 1);
        out.println(curr.getIncident() + "#" + curr);
    }

    private static void printKML(List<Airport> airports, PrintStream out) {
        // TODO
    }

    public static void setToKMLFormat() {
        textFormat = false;
    }

    public static void setToTextFormat() {
        textFormat = true;
    }

    // Tambien podria almacenar el caminito con los vuelos, y cosas para el KML
    private static class RouteData {
        private double price;
        private Time fltime;
        private Time totalTime;

        public RouteData(List<Airport> airports) {
            for (int i = 1; i < airports.size(); i++) {
                Flight flight = airports.get(i).getIncident();
                price += flight.getPrice();
                fltime.addTime(flight.getDuration());
                // totalTime = calculateTotalTime();
            }
        }
    }
}
