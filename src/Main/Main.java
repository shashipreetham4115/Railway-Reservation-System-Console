package Main;

import Utilities.Inputs;

public class Main {
	public static void main(String[] args) {
		String userType = null;
		RailwayApp ra = new RailwayApp();
		UserApp ua = new UserApp();
		Login val = new Login();

		while (true) {
			while (userType == null) {
				userType = val.ValidateUser();
			}

			val.greetUser();

			while (userType == "admin") {

				System.out.println("\nPlease Choose Your Choice");
				System.out.println("1. Book Ticket");
				System.out.println("2. Cancel Ticket");
				System.out.println("3. Print Ticket");
				System.out.println("4. Passenger List");
				System.out.println("5. Add New User");
				System.out.println("6. Add New Train");
				System.out.println("7. PrepareChart");
				System.out.println("8. Change Password");
				System.out.println("9. Logout");
				System.out.println("10. Exit");
				
				switch (Inputs.GetInt("Enter Your Choice Here")) {
				case 1: {
					ra.BookTicket("Admin");
					break;
				}
				case 2: {
					ra.CancelTicket();
					break;
				}
				case 3: {
					ra.PrintTicket();
					break;
				}
				case 4:
					ra.PrintPassengers();
					break;
				case 5:
					val.addNewUser();
					break;
				case 6: {
					ra.AddTrain();
					break;
				}
				case 7: {
					ra.GetChart();
					break;
				}
				case 8: {
					val.ChangePassword();
					break;
				}
				case 9: {
					userType = null;
					break;
				}
				case 10: {
					System.exit(0);
				}
				default:
					System.out.println("Please Choose Correct Choice");
				}
			}
			while (userType == "user") {

				System.out.println("\nPlease Choose Your Choice");
				System.out.println("1. Book Ticket");
				System.out.println("2. Book Ticket For Others");
				System.out.println("3. Cancel Ticket");
				System.out.println("4. Download Ticket");
				System.out.println("5. My Bookings");
				System.out.println("6. Change Password");
				System.out.println("7. Logout");
				System.out.println("8. Exit");
				
				switch (Inputs.GetInt("Enter Your Choice Here")) {
				case 1: {
					ua.BookTicket("User");
					break;
				}
				case 2: {
					ua.BookTicketForOthers();
					break;
				}
				case 3: {
					ua.CancelTicket();
					break;
				}
				case 4: {
					ua.DownloadTicket();
					break;
				}
				case 5: {
					ua.MyBookings();
					break;
				}
				case 6: {
					val.ChangePassword();
					break;
				}
				case 7: {
					userType = null;
					break;
				}
				case 8: {
					System.exit(0);
				}
				default:
					System.out.println("Please Choose Correct Choice");
				}
			}
		}
	}
}
