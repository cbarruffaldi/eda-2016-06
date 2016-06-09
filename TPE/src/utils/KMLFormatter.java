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
        str.append(HEADER);
        str.append(KML_OPENER);

        attachPlacemark(str,tickets.get(0).getOrigin());
        for (Ticket ticket : tickets)
            attachPlacemark(str, ticket.getDestination());

        str.append(KML_CLOSER);

        return new String(str);
    }

    private static void attachPlacemark (StringBuffer str, Airport airport) {
        str.append("<Placemark>\n");
        str.append("<name>" + airport.getId() + "</name>\n");
        str.append("<description>" + "</description>\n"); // empty description
        attachPoint(str, airport.getLatitude(), airport.getLongitude());
        str.append("</Placemark>\n");
    }

    private static void attachPoint (StringBuffer str, double latitude, double longitude) {
        str.append("<Point>\n");
        str.append("<coordinates>" + longitude + "," + latitude + "</coordinates>\n");
        str.append("</Point>\n");
    }
}
