package utils;

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

    public void printBestRoute (List<Ticket> tickets) {
        if (stdOut) {
            printToStdout(tickets);
        } else {
            printToFile(tickets);
        }
    }

    private void printToStdout (List<Ticket> tickets) {
        print(tickets, System.out);
    }

    private void printToFile (List<Ticket> tickets) {
        try {
            print(tickets, new PrintStream(new FileOutputStream(fileName, true)));
        } catch (FileNotFoundException e) {
            System.err.println("Error writing/reading file");
        }
    }

    private void print (List<Ticket> tickets, PrintStream out) {
        if (textFormat) {
            printText(tickets, out);
        } else {
        	printKML(tickets, out);
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

    private void printKML (List<Ticket> tickets, PrintStream out) {
        out.print(KMLFormatter.airportsToKML(tickets));
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

    /**
     * Se encarga de calcular el precio, el tiempo total y el tiempo de vuelo
     * de la lista de tickets.
     */
    private static class RouteData {
        private double price;
        private Time fltime;
        private Time totalTime;
        private Moment prevArrivalMoment;

        public RouteData (List<Ticket> tickets) {
            price = 0;
            fltime = new Time(0);
            totalTime = new Time(0);
            prevArrivalMoment = tickets.get(0).getDeparture();


            for (Ticket ticket : tickets) {
                price += ticket.getPrice();
                fltime = fltime.addTime(ticket.getDuration());
                totalTime = totalTime.addTime(calculateTotalTime(ticket));
            }
        }

        private Time calculateTotalTime (Ticket ticket) {
            Time t = ticket.getDuration();
            t = t.addTime(prevArrivalMoment.howMuchUntil(ticket.getDeparture()));
            prevArrivalMoment = ticket.getArrival();
            return t;
        }
    }
}
