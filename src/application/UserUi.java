package application;

import java.util.*;

import interfaces.UserServices;
import utilities.InputsUtil;

public class UserUi extends RailwayUi implements UserServices {

	// This function calls the BookTicket function of TrainHandler by sending its
	// role
	public void bookTicketForOthers() {
		try {
			bookTicket("Others");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// This Function calls the PrintTicket function of TrainHandler by sending its
	// TicketID
	public void downloadTicket() {
		try {
			printTicket(InputsUtil.getLong("Please Enter Your PNR"));
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void cancelTicket() {
		try {
			long ticketId = InputsUtil.getLong("Please Enter PNR");
			if (loginHandler.getLoggedInUser().myBookings.contains(ticketId)) {
				if (cancelTicket(ticketId)) {
					loginHandler.getLoggedInUser().myBookings.remove(ticketId);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// This function is used to print user booked tickets
	public void myBookings() {
		try {
			ArrayList<Long> myTickets = loginHandler.getLoggedInUser().myBookings;

			if (myTickets.isEmpty()) {
				System.out.println("No Bookings done till now");
				return;
			}

			printAllTickets(myTickets);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
