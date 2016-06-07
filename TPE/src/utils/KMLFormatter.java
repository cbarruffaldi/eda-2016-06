package utils;

import flightassistant.Airport;

import java.util.List;

public class KMLFormatter {
    private static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    private static final String KML_OPENER = "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n";
    private static final String KML_CLOSER = "</kml>\n";

    public static String airportsToKML (List<Airport> airports) {
        StringBuffer str = new StringBuffer();
        str.append(HEADER);
        str.append(KML_OPENER);

        for (Airport airport : airports)
            attachPlacemark(str, airport);

        str.append(KML_CLOSER);

        return new String(str);
    }

    private static void attachPlacemark (StringBuffer str, Airport airport) {
        str.append("<Placemark>\n");
        str.append("<name>" + airport.getId() + "</name>\n");
        str.append("<description></description>\n"); // empty description
        attachPoint(str, airport.getLatitude(), airport.getLongitude());
        str.append("</Placemark>\n");
    }

    private static void attachPoint (StringBuffer str, double latitude, double longitude) {
        str.append("<Point>\n");
        str.append("<coordinates>" + longitude + "," + latitude + "</coordinates>\n");
        str.append("</Point>\n");
    }
}
