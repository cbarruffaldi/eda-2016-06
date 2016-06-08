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

    private static String fileName;
    private boolean textFormat = true;   // Por defecto modo texto.
    private boolean stdOut = true; // Por defecto va a salida estandar.

    public OutputManager () {
        textFormat = true;
        stdOut = true;
    }

    public void invalidCommand () {
        System.err.println("Invalid Command");
    }

    public void notFoundMsg () {
        System.out.println("NotFound");
    }

    public void fileOpenErrorMsg () {
        System.err.println("Could not open/read file");
    }

    public void exitErrorMsg () {
        System.err.print("Could not save program files");
    }

    public void printBestRoute (List<Ticket> airports) {
        if (stdOut) {
            printToStdout(airports);
        } else {
            printToFile(airports);
        }
    }

    private void printToStdout (List<Ticket> airports) {
        print(airports, System.out);
    }

    private void printToFile (List<Ticket> airports) {
        try {
            print(airports, new PrintStream(new FileOutputStream(fileName, true)));
        } catch (FileNotFoundException e) {
            System.err.println("Error writing/reading file");
        }
    }

    private void print (List<Ticket> airports, PrintStream out) {
        if (textFormat) {
            printText(airports, out);
        } else {
            // TODO: printKML(airports, out);
        }
    }

    private void printText (List<Ticket> tickets, PrintStream out) {
        RouteData data = new RouteData(tickets);
        out.println(
            "Precio#" + data.price + '\n' + "TiempoVuelo#" + data.fltime + '\n' + "TiempoTotal#"
                + data.totalTime + '\n');
        for (Ticket ticket : tickets)
        	printTicket(ticket, out);
    }

    private void printTicket (Ticket ticket, PrintStream out) {
        String origin = ticket.getOrigin().getId();
        String destination = ticket.getDestination().getId();
        String airline = ticket.getFlightId().getAirline();
        int flightNum = ticket.getFlightId().getNumber();

        out.println(origin + "#" + airline + "#" + flightNum + "#" + destination);
    }

    private void printKML (List<Airport> airports, PrintStream out) {
        out.print(KMLFormatter.airportsToKML(airports));
    }

    public void setToKMLFormat () {
        textFormat = false;
    }

    public void setToTextFormat () {
        textFormat = true;
    }

    public void setToStdOutput () {
        stdOut = true;
    }

    public void setToFileOutput (String file) {
        stdOut = false;
        fileName = file;
    }

    // Tambien podria almacenar el caminito con los vuelos, y cosas para el KML
    private static class RouteData {
        private double price;
        private Time fltime;
        private Time totalTime;
        private Moment arrivalMoment;

        public RouteData (List<Ticket> tickets) {
            price = 0;
            fltime = new Time(0);
            totalTime = new Time(0);
            arrivalMoment = tickets.get(0).getDeparture();


            for (Ticket ticket : tickets) {
                price += ticket.getPrice();
                fltime = fltime.addTime(ticket.getDuration());
                totalTime = totalTime.addTime(calculateTotalTime(ticket));
            }
        }

        private Time calculateTotalTime (Ticket ticket) {
            Time t = ticket.getDuration();
            t = t.addTime(arrivalMoment.howMuchUntil(ticket.getDeparture()));
            return t;
        }
    }

}
