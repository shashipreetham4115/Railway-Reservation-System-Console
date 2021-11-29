package Main;

import java.util.*;


import Entites.Ticket;
import Interfaces.UserAppServices;
import Utilities.Inputs;

public class UserApp extends App implements UserAppServices {

	public void BookTicket() {
		BookTicket("User");
	}

	public void BookTicketForOthers() {
		BookTicket("Others");
	}

	public void DownloadTicket() {
		PrintTicket(Inputs.GetLong("Please Enter Your Ticket ID"));
	}

	public void MyBookings() {
		Collection<Ticket> my_tickets = Login.getLoggedInUser().user_tickets.values();

		if (my_tickets.isEmpty()) {
			System.out.println("No Bookings done till now");
			return;
		}

		PrintAllTickets(my_tickets);
	}
}
