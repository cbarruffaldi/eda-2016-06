package flightassistant;

import java.util.ArrayList;
import java.util.List;

import utils.Day;
import utils.Moment;

public class TicketMachine {

	/**
	 * Devuelve una lista de Ticket dado un Flight. Esta lista contiene un Ticket por cada
	 * Momento de salida del vuelo.
	 * @param flight - vuelo correspondiente a los Ticket a generar.
	 * @return lista de tickets.
	 */
	public static List<Ticket> makeTickets(Flight flight) {
		if (flight == null)
			throw new NullPointerException("null flight");

		List<Ticket> tickets = new ArrayList<Ticket>(Day.TOTAL_DAYS); // Imposible generar más de 7 tickets
		for (Moment departure : flight.getDepartures())
			tickets.add(new Ticket(flight, departure));

		return tickets;
	}

}
