package application;

import java.util.*;

import backend.TrainSoftware;
import backend.BookingSoftware;
import backend.Login;
import entites.Station;
import entites.Ticket;
import entites.Train;
import entites.User;
import utilities.Inputs;
import utilities.ValidateInput;

public class Ui {

	Scanner sc = new Scanner(System.in);
	Login login = new Login();
	TrainSoftware trainSoftware = new TrainSoftware();
	BookingSoftware bookingSoftware = new BookingSoftware();
	private ArrayList<Integer> trainIds = new ArrayList<>();

	// This Function is used to Add new User
	public void addNewUser(String role) {
		try {
			String un = "";
			while (true) {
				un = Inputs.getString("Please Enter New Username");
				if (login.isUsernameAvailable(un)) {
					break;
				}
				System.out.println("Username not Available");
			}
			sc.nextLine();
			String name = Inputs.getLine("Please Enter Full Name");
			String gender = ValidateInput.getGender();
			int age = ValidateInput.getAge();
			String pw = Inputs.getString("Please Enter Password");
			if (login.addNewUser(un, pw, name, gender, age, role))
				System.out.println("Successfully Added");
			else
				System.out.println("Please Try Again");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// This function is used to change password which will ask for old password and
	// if it is correct then user able to set new password
	public void changePassword() {
		try {
			String oldPassword = Inputs.getString("Please Enter Your Old Password");
			if (login.verifyPassword(oldPassword)) {
				login.changePassword(oldPassword, Inputs.getString("Please Enter Your New Password"));
				System.out.println("Your Password has been changed Successfully");
			} else
				System.out.println("You Have Entered Wrong Password");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// Once User Logged In this Function Greets the User
	public void greetUser() {
		try {
			System.out.println("\nWelcome " + login.getUsername());
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void bookTicket(String role) {
		try {
			String start = Inputs.getLine("Enter From Station").toUpperCase();
			String end = Inputs.getLine("Enter To Station").toUpperCase();
			String date = ValidateInput.getDate();
			Ticket t = null;
			if (printTrains(start, end, date))
				if (validateTrainID()) {
					Train train = trainSoftware.getCurrentTrain();
					int startIndex = train.arrStCodes.indexOf(start);
					int endIndex = train.arrStCodes.indexOf(end);
					if (startIndex != -1)
						start = train.arrStNames.get(startIndex);
					if (endIndex != -1)
						end = train.arrStNames.get(endIndex);
					String pb = ValidateInput.getPreferedBerth();
					User u;
					if (role.equals("User")) {
						u = login.getLoggedInUser();
						t = bookingSoftware.bookTicket(u, start, end, pb, trainSoftware.getCurrentTrain());
					} else {
						u = new User(Inputs.getLine("Please Enter Name"), ValidateInput.getGender(),
								ValidateInput.getAge());
						t = bookingSoftware.bookTicket(u, start, end, pb, trainSoftware.getCurrentTrain());
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

	// This Function Overrides the CancelTicket method in BookingSoftware SOftware
	// and after taking a valid ticket id it will call Cancel Ticket function in
	// booking software
	public boolean cancelTicket(Long ticketId) {
		try {
			if (bookingSoftware.getTicket(ticketId) != null) {
				if (bookingSoftware.cancelTicket(ticketId)) {
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

	public String getAvailableTickets(String start, String end, Train train) {
		try {
			return bookingSoftware.getAvailableTickets(start, end, train);
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	// Overrides the ValidateTrain method of BookingSoftware because of taking
	// inputs and if it is wrong it will ask again
	private boolean validateTrainID() {
		try {
			while (true) {
				int sno = Inputs.getInt("Please Enter S.No from Above List or enter -1 to exit");
				int trainId = trainIds.get(sno - 1);
				if (trainId == -1)
					break;
				if (trainSoftware.validateTrainID(trainId)) {
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

	// This Function will Print all TrainSoftware Available for the given start,
	// destination and date.
	protected boolean printTrains(String start, String end, String date) {
		try {
			ArrayList<Train> trains = trainSoftware.getTrains(start, end, date);
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
						bookingSoftware.getAvailableTickets(start, end, t));
				trainIds.add(t.id);
			}
			if (trains.size() == 0) {
				System.out.println("\nNo TrainSoftware Found...");
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
				Ticket ticket = bookingSoftware.getTicket(p);
				Train train = trainSoftware.getTrain(ticket.train);
				Station start = train.arrStations.get(train.arrStNames.indexOf(ticket.fromStation));
				User user = login.getUser(ticket.user);
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
			Train train = trainSoftware.getTrain(ticket.train);
			User user = login.getUser(ticket.user);
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
			if (bookingSoftware.getTicket(id) != null)
				printTicket(bookingSoftware.getTicket(id));
			else
				System.out.println("Please Enter the Valid PNR");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
