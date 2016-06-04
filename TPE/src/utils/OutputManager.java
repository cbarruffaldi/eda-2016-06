package utils;

import flightassistant.Airport;
import flightassistant.Ticket;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * Created by Bianchi on 30/5/16.
 */
public class OutputManager {

    private static boolean textFormat = true;   // Por defecto modo texto.

    private static boolean stdOut = true; // Por defecto va a salida estandar.

    private static String fileName;

    public static void invalidCommand() {
        System.err.println("Invalid Command");
    }

    public static void notFoundMsg() {
        System.out.println("NotFound");
    }

    public static void fileOpenErrorMsg() {
        System.err.println("Could not open/read file");
    }

    public static void exitErrorMsg() { System.err.print("Could not save program files"); }

    public static void printBestRoute(List<Airport> airports) {
        if (stdOut) {
            printToStdout(airports);
        } else {
            printToFile(airports);
        }
    }

    public static void printToStdout(List<Airport> airports) {
        print(airports, System.out);
    }

    public static void printToFile(List<Airport> airports) {
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

    public static void setToStdOutput() {
        stdOut = true;
    }

    public static void setToFileOutput(String file) {
        stdOut = false;
        fileName = file;
    }

    // Tambien podria almacenar el caminito con los vuelos, y cosas para el KML
    private static class RouteData {
        private double price;
        private Time fltime;
        private Time totalTime;

        // TODO medio choto lo de pedirle el segundo aeropuerto, que pasa si hay uno solo?
        public RouteData(List<Airport> airports) {
            int weeks = 0; boolean moreThanADay = false;
            Airport firstAirport = airports.get(1);
            Day firstDay = firstAirport.getIncident().getDeparture().getDay();
            Day prevDay = firstDay;
            fltime = new Time(0);

            for (int i = 1; i < airports.size(); i++) {

                Ticket ticket = airports.get(i).getIncident();
                price += ticket.getPrice();
                fltime.addTime(ticket.getDuration());

                if (!prevDay.equals(ticket.getDeparture().getDay())) {
                    moreThanADay = true;
                } else if (moreThanADay && ticket.getDeparture().getDay().equals(firstDay)) {
                    weeks++; // Paso una semana.
                }
            }
            // Recorre de nuevo la lista, pero es un poco menos engorroso en el código.
            totalTime = calculateTotalTime(weeks, airports.get(1).getIncident(), airports.get(airports.size()-1).getIncident());
        }

        // TODO testear esto y lo de contar las semanas
        private Time calculateTotalTime(int weeks, Ticket first, Ticket last) {
            Time timeSum = first.getDeparture().howMuchUntil(last.getArrival());
            timeSum.addMinutes(TimeConstants.MINUTES_PER_WEEK * weeks);
            return timeSum;
        }

        public double getPrice() {
            return price;
        }

        public Time getFltime() {
            return fltime;
        }

        public Time getTotalTime() {
            return totalTime;
        }
    }

}
