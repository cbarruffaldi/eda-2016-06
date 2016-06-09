package utils;

import flightassistant.Ticket;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * OutputManager presenta métodos para imprimir la salida del programa, con la opción de
 * hacerlo hacia salida estándar o hacia un archivo de texto, y en formato texto o KML.
 */
public class OutputManager {

	private PrintStream out = System.out;
    private boolean textFormat = true;   // Por defecto modo texto.

    public OutputManager () {
        textFormat = true;
    }

    /**
     * Mensaje cuando se ingresa un comando con formato inválido en la entrada.
     */
    public void invalidCommand () {
        System.err.println("Invalid Command");
    }

    /**
     * Mensaje cuando no se encuentra un camino pedido entre dos aeropuertos.
     */
    public void notFoundMsg () {
        out.println("NotFound");
    }

    public void fileOpenErrorMsg () {
        System.err.println("Could not open/read file");
    }

    public void exitErrorMsg () {
        System.err.println("Could not save program files");
    }

    /**
     * Se encarga de calcular tiempo total, tiempo de vuelo y precio del conjunto de
     * vuelos dados como parámetro, y lo imprime por salida estándar o a un archivo.
     * @see this.setToStdOutput this.setToKMLOutput
     * @param tickets lista de tickets de vuelos que forman el camino pedido.
     */
    public void printBestRoute (List<Ticket> tickets) {
    	print(tickets, out);
    }

    private void print (List<Ticket> tickets, PrintStream out) {
    	if (tickets == null || tickets.isEmpty())
    		notFoundMsg();
    	else if (textFormat) {
            printText(tickets, out);
        } else {
        	printKML(tickets, out);
        }
    	out.println();
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

    /**
     * Configura el formato de salida como KML. Queda guardado en la instancia de OutputManager.
     */
    public void setToKMLFormat () {
        textFormat = false;
    }

    /**
     * Configura el formato de salida como texto. Queda guardado en la instancia de OutputManager.
     */
    public void setToTextFormat () {
        textFormat = true;
    }

    /**
     * Configura el destino de salida a la salida estándar. Queda guardado en la instancia de OutputManager.
     */
    public void setToStdOutput () {
        out = System.out;
    }

    /**
     * Configura el destino de salida a un archivo txt. Queda guardado en la instancia de OutputManager.
     */
    public void setToFileOutput (String file) {
    	try {
			out = new PrintStream(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
            System.err.println("Error creating file");
		}
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
