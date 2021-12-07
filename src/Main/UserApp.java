package Main;

import java.util.*;

import Entites.Ticket;
import Interfaces.UserAppServices;
import Utilities.Inputs;

public class UserApp extends App implements UserAppServices {

	// This function calls the BookTicket function of App by sending its role
	public void BookTicketForOthers() {
		BookTicket("Others");
	}

	// This Function calls the PrintTicket function of App by sending its TicketID
	public void DownloadTicket() {
		PrintTicket(Inputs.GetLong("Please Enter Your Ticket ID"));
	}

	// This function is used to print user booked tickets
	public void MyBookings() {
		Collection<Ticket> my_tickets = Login.getLoggedInUser().user_tickets.values();

		if (my_tickets.isEmpty()) {
			System.out.println("No Bookings done till now");
			return;
		}

		PrintAllTickets(my_tickets);
	}
}
