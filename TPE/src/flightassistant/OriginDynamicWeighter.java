package flightassistant;

import utils.Day;
import utils.Moment;
import utils.Time;

//Tiene que salir si o si ese dia
//Repetimos codigo. Mala suerte :(
public class OriginDynamicWeighter {

	Day departureDay;
	
	public OriginDynamicWeighter(Day departure){
		this.departureDay = departure;
	}
	
    public Double weight(Airport from, Airport to, Moment startMoment) {

    	if(!from.flightExistsTo(to))
    		return Double.POSITIVE_INFINITY;
    	
        HigherIterator ticketIter = from.iteratorOfHigherFlightsTo(to, startMoment);

        if (!ticketIter.hasNext())
            return Double.POSITIVE_INFINITY;

        Ticket min = ticketIter.next();
        Time shortestTime = totalTime(startMoment, min);
          
       	while (ticketIter.hasNext() && min.getDeparture().getDay().equals(departureDay)) {
            Ticket ticket = ticketIter.next();
            Time waitTime = startMoment.howMuchUntil(ticket.getDeparture());

            // Dado que los vuelos se iteran se forma ordenada por momento de salida,
            // los waitTime son crecientes. Si un waitTime es mayor a el
            // menor total time ya puedo cortar y retornar; todos los siguientes
            // vuelos tendrán mayor waitTime y por ende mayor total time.
            if (waitTime.compareTo(shortestTime) >= 0)
            	return (double)shortestTime.getMinutes();

            Time aux = totalTime(startMoment, ticket);
            if (aux.compareTo(shortestTime) < 0) {
                min = ticket;
                shortestTime = aux;
            }
        }
   
       	if(!min.getDeparture().getDay().equals(departureDay))
        	return Double.POSITIVE_INFINITY;

        return (double)shortestTime.getMinutes();

    }

    private Time totalTime(Moment start, Ticket ticket) {
        Time wait = start.howMuchUntil(ticket.getDeparture());
        return wait.addTime(ticket.getDuration()); //Se hace esto en lugar de ticket.getArrival por si dura mas de una semana?
    }
}
