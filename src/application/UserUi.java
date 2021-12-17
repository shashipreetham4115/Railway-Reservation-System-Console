package application;

import java.util.*;

import interfaces.UserAppServices;
import utilities.Inputs;

public class UserUi extends Ui implements UserAppServices {

	// This function calls the BookTicket function of TrainSoftware by sending its
	// role
	public void bookTicketForOthers() {
		try {
			bookTicket("Others");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// This Function calls the PrintTicket function of TrainSoftware by sending its
	// TicketID
	public void downloadTicket() {
		try {
			printTicket(Inputs.getLong("Please Enter Your PNR"));
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void cancelTicket() {
		try {
			long ticketId = Inputs.getLong("Please Enter PNR");
			if (login.getLoggedInUser().myBookings.contains(ticketId)) {
				if (cancelTicket(ticketId)) {
					login.getLoggedInUser().myBookings.remove(ticketId);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// This function is used to print user booked tickets
	public void myBookings() {
		try {
			ArrayList<Long> myTickets = login.getLoggedInUser().myBookings;

			if (myTickets.isEmpty()) {
				System.out.println("No BookingSoftware done till now");
				return;
			}

			printAllTickets(myTickets);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public String validateUser() {
		try {
			while (true) {
				switch (Inputs.getInt("1) Login \n2) New User \n3) Back \nPlease Select Your Choice ")) {
				case 1: {
					String username = Inputs.getString("Please Enter Your Username");
					if (login.isUsernameAvailable(username))
						System.out.println("\nIncorrect Username");
					else {
						String password = Inputs.getString("Please Enter Your Password");
						String userType = login.validateUser(username, password);
						if (userType != null && userType.equals("user")) {
							greetUser();
							return userType;
						} else if (userType != null && userType.equals("user"))
							System.out.println("Invalid Username or Password");
						else
							System.out.println("\nInvalid Password");
					}
					break;
				}
				case 2: {
					addNewUser("user");
					break;
				}
				case 3:
					return "";
				default:
					System.out.println("Please Enter Valid Option");
				}
			}
		} catch (Exception e) {
			System.out.println(e);
			return "";
		}
	}
}
