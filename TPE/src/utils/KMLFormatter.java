package utils;

import flightassistant.Airport;
import flightassistant.Ticket;

import java.util.List;

/**
 * Se encarga de generar un string con el formato correspondiente a un documento KML
 *
 */
public class KMLFormatter {
    private static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    private static final String KML_OPENER = "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n";
    private static final String KML_CLOSER = "</kml>\n";

    public static String airportsToKML (List<Ticket> tickets) {
        StringBuffer str = new StringBuffer();
        Ticket lastTicket = tickets.get(tickets.size()-1);
        str.append(HEADER);
        str.append(KML_OPENER);

        for (Ticket ticket : tickets)
            attachPlacemark(str, ticket.getOrigin(), buildTicketDescription(ticket));
        attachPlacemark(str,lastTicket.getDestination(), "Aeropuerto destino");

        str.append(KML_CLOSER);

        return new String(str);
    }

    private static String buildTicketDescription(Ticket ticket) {
    	StringBuffer str = new StringBuffer();
    	str.append("El vuelo " + ticket.getFlightId());
    	str.append(" parte con destino a " + ticket.getDestination());
		return str.toString();
	}

	private static void attachPlacemark (StringBuffer str, Airport airport, String description) {
        str.append("<Placemark>\n");
        str.append("<name>" + airport.getId() + "</name>\n");
        str.append("<description>" + description + "</description>\n"); // empty description
        attachPoint(str, airport.getLatitude(), airport.getLongitude());
        str.append("</Placemark>\n");
    }

    private static void attachPoint (StringBuffer str, double latitude, double longitude) {
        str.append("<Point>\n");
        str.append("<coordinates>" + longitude + "," + latitude + "</coordinates>\n");
        str.append("</Point>\n");
    }
}
