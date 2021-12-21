package application;

import java.util.*;

import interfaces.UserAppServices;
import utilities.InputsUtil;

public class UserUi extends Ui implements UserAppServices {

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

	public String validateUser() {
		try {
			while (true) {
				switch (InputsUtil.getInt("1) LoginHandler \n2) New User \n3) Back \nPlease Select Your Choice ")) {
				case 1: {
					String username = InputsUtil.getString("Please Enter Your Username");
					if (loginHandler.isUsernameAvailable(username))
						System.out.println("\nIncorrect Username");
					else {
						String password = InputsUtil.getString("Please Enter Your Password");
						String userType = loginHandler.validateUser(username, password);
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
