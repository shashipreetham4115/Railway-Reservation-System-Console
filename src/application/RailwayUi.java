package application;

import java.util.*;

import entites.Station;
import entites.Ticket;
import entites.Train;
import entites.User;
import handlers.BookingHandler;
import handlers.LoginHandler;
import handlers.TrainHandler;
import interfaces.BookingServices;
import interfaces.LoginServices;
import interfaces.TrainServices;
import utilities.InputsUtil;
import utilities.ValidateInputUtil;

public class RailwayUi {

	LoginServices loginHandler = new LoginHandler();
	TrainServices trainHandler = new TrainHandler();
	BookingServices bookingHandler = new BookingHandler();
	private ArrayList<Integer> trainIds = new ArrayList<>();

	public void bookTicket(String role) {
		try {
			String start = InputsUtil.getLine("Enter From Station").toUpperCase();
			String end = InputsUtil.getLine("Enter To Station").toUpperCase();
			String date = ValidateInputUtil.getDate();
			Ticket t = null;
			if (printTrains(start, end, date))
				if (validateTrainID()) {
					Train train = trainHandler.getCurrentTrain();
					int startIndex = train.arrStCodes.indexOf(start);
					int endIndex = train.arrStCodes.indexOf(end);
					if (startIndex != -1)
						start = train.arrStNames.get(startIndex);
					if (endIndex != -1)
						end = train.arrStNames.get(endIndex);
					String pb = ValidateInputUtil.getPreferedBerth();
					User u;
					if (role.equals("User")) {
						u = loginHandler.getLoggedInUser();
						t = bookingHandler.bookTicket(u, start, end, pb, trainHandler.getCurrentTrain());
					} else {
						u = new User(InputsUtil.getLine("Please Enter Name"), ValidateInputUtil.getGender(),
								ValidateInputUtil.getAge());
						t = bookingHandler.bookTicket(u, start, end, pb, trainHandler.getCurrentTrain());
					}
				} else
					return;
			else
				return;
			if (t == null) {
				System.out.print("Tickets Not Available");
			} else {
				printTicket(t);
				System.out.print("\n Ticket Booked Sucessfully");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// This Function Overrides the CancelTicket method in BookingHandler SOftware
	// and after taking a valid ticket id it will call Cancel Ticket function in
	// booking software
	public boolean cancelTicket(Long ticketId) {
		try {
			if (bookingHandler.getTicket(ticketId) != null) {
				if (bookingHandler.cancelTicket(ticketId)) {
					System.out.println("\nSuccessfully Canceled Ticket");
					return true;
				} else
					System.out.println("\nFailed To Cancel Your Ticket Please Try Again");
			} else
				System.out.println("\n Please Enter Valid PNR");
			return false;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	// Overrides the ValidateTrain method of BookingHandler because of taking
	// inputs and if it is wrong it will ask again
	private boolean validateTrainID() {
		try {
			while (true) {
				int sno = InputsUtil.getInt("Please Enter S.No from Above List or enter -1 to exit");
				int trainId = trainIds.get(sno - 1);
				if (trainId == -1)
					break;
				if (trainHandler.validateTrainID(trainId)) {
					return true;
				} else {
					System.out.print("\nPlease Enter Correct Id");
				}
			}
			return false;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	// This Function will Print all TrainHandler Available for the given start,
	// destination and date.
	protected boolean printTrains(String start, String end, String date) {
		try {
			ArrayList<Train> trains = trainHandler.getTrains(start, end, date);
			System.out.println(
					"\n\n-----------------------------------------------------------------------------------------");
			System.out.format("%1$-7s%2$-10s%3$-30s%4$-10s%5$-10s", "S.No", "Train No", "Train", "Time",
					"Available Seats");
			System.out.println(
					"\n-----------------------------------------------------------------------------------------");
			int i = 0;
			for (Train t : trains) {
				int startIndex = t.arrStCodes.indexOf(start);
				int endIndex = t.arrStCodes.indexOf(end);
				if (startIndex != -1)
					start = t.arrStNames.get(startIndex);
				if (endIndex != -1)
					end = t.arrStNames.get(endIndex);
				System.out.format("\n%1$-7s%2$-10s%3$-30s%4$-10s%5$-10s", ++i, t.id, t.name, t.time,
						bookingHandler.getAvailableTickets(start, end, t));
				trainIds.add(t.id);
			}
			if (trains.size() == 0) {
				System.out.println("\nNo Trains Found...");
			}
			System.out.println(
					"\n-----------------------------------------------------------------------------------------\n");
			return trains.size() != 0;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	protected void printAllTickets(ArrayList<Long> myTickets) {
		try {
			System.out.println(
					"\n------------------------------------------------------------------------------------------------------------------------------");
			System.out.format("%1$-17s%2$-25s%3$-5s%4$-20s%5$-20s%6$-15s%7$-10s%8$-5s", "PNR", "Name", "Age", "From",
					"To", "Date", "Time", "Status/berth");
			System.out.println(
					"\n------------------------------------------------------------------------------------------------------------------------------\n");
			for (Long p : myTickets) {
				Ticket ticket = bookingHandler.getTicket(p);
				Train train = trainHandler.getTrain(ticket.train);
				Station start = train.arrStations.get(train.arrStNames.indexOf(ticket.fromStation));
				User user = loginHandler.getUser(ticket.user);
				System.out.format("%1$-17s%2$-25s%3$-5s%4$-20s%5$-20s%6$-15s%7$-10s%8$-5s", ticket.pnr, user.name,
						user.age, ticket.fromStation, ticket.toStation, start.departureDate, start.departureTime,
						ticket.berth);
				System.out.println();
			}
			System.out.println(
					"\n------------------------------------------------------------------------------------------------------------------------------\n");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// Function is Used to get Ticket and Print All Ticket Details.
	private void printTicket(Ticket ticket) {
		try {
			Train train = trainHandler.getTrain(ticket.train);
			User user = loginHandler.getUser(ticket.user);
			Station start = train.arrStations.get(train.arrStNames.indexOf(ticket.fromStation));
			Station destination = train.arrStations.get(train.arrStNames.indexOf(ticket.toStation));
			System.out.print(
					"\n-----------------------------------------------------------------------------------------------------------------------------------\n");
			System.out.print(
					"-------------------------------------------------------Ticket Details--------------------------------------------------------------");
			System.out.print(
					"\n-----------------------------------------------------------------------------------------------------------------------------------\n");
			System.out.format("\n%1$-30s%2$-35s%3$-30s%4$-30s\n", "Passenger Name    :", user.name,
					"Passenger Age       :", user.age);
			System.out.format("\n%1$-30s%2$-35s%3$-30s%4$-30s\n", "Ticket PNR        :", ticket.pnr,
					"Allocated Berth     :", ticket.berth);
			System.out.format("\n%1$-30s%2$-35s%3$-30s%4$-30s\n", "Train No          :", train.id,
					"Train Name          :", train.name);
			System.out.format("\n%1$-30s%2$-35s%3$-30s%4$-30s\n", "Boarding At       :", start.name,
					"Scheduled Departure :", start.departureDate + " " + start.departureTime);
			System.out.format("\n%1$-30s%2$-35s%3$-30s%4$-30s\n", "Reservation Up to :", destination.name,
					"Scheduled Arrival   :", destination.arrivalDate + " " + destination.arrivalTime);
			System.out.print(
					"\n-----------------------------------------------------------------------------------------------------------------------------------\n");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// This Function Overloads the PrintTicket Function because before calling print
	// ticket it will verify the given ticket id and then it call if ticket id is
	// valid
	protected void printTicket(long id) {
		try {
			if (bookingHandler.getTicket(id) != null)
				printTicket(bookingHandler.getTicket(id));
			else
				System.out.println("Please Enter the Valid PNR");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
