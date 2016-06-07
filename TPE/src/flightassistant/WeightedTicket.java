package flightassistant;

public class WeightedTicket {
    private Ticket ticket;
    private double weight;

    public WeightedTicket (Ticket ticket, double weight) {
        this.ticket = ticket;
        this.weight = weight;
    }

    public double weight () {
        return weight;
    }

    public Ticket ticket () {
        return ticket;
    }
}
