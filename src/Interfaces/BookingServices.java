package interfaces;

import java.util.ArrayList;

import entites.Ticket;
import entites.Train;
import entites.User;

public interface BookingServices {

	Ticket bookTicket(User user, String start, String destination, String preferedBerth, Train trainId);

	ArrayList<Long> prepareChart(Train train);

	boolean cancelTicket(long id);

	public Ticket getTicket(long id);

	public String getAvailableTickets(String start, String end, Train train);

}
