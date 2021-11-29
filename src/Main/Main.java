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

			while (userType != null) {

				System.out.println("\nPlease Choose Your Choice");
				System.out.println("1. Book Ticket");
				System.out.println("2. Cancel Ticket");
				System.out.println("3. Print Ticket");
				if (userType.equals("admin"))
					System.out.println("4. Passenger List");
				if (userType.equals("admin"))
					System.out.println("5. Add New User");
				if (userType.equals("user"))
					System.out.println("6. My Bookings");
				if (userType.equals("admin"))
					System.out.println("7. Add New Train");
				if (userType.equals("user"))
					System.out.println("8. Book Ticket For Others");
				if (userType.equals("admin"))
					System.out.println("9. PrepareChart");
				System.out.println("10. Change Password");
				System.out.println("11. Logout");
				System.out.println("12. Exit");
				switch (Inputs.GetInt("Enter Your Choice Here")) {
				case 1: {
					if (userType.equals("admin"))
						ra.BookTicket();
					else
						ua.BookTicket();
					break;
				}
				case 2: {
					if (userType.equals("admin"))
						ra.CancelTicket();
					else
						ua.CancelTicket();
					break;
				}
				case 3: {
					if (userType.equals("admin"))
						ra.PrintTicket();
					else
						ua.DownloadTicket();
					break;
				}
				case 4:
					if (userType == "admin")
						ra.PrintPassengers();
					else
						System.out.println("Please Choose Correct Choice");
					break;
				case 5:
					if (userType == "admin")
						val.addNewUser();
					else
						System.out.println("Please Choose Correct Choice");
					break;
				case 6: {
					if (userType.equals("user"))
						ua.MyBookings();
					else
						System.out.println("Please Choose Correct Choice");
					break;
				}
				case 7: {
					if (userType.equals("admin"))
						ra.AddTrain();
					else
						System.out.println("Please Choose Correct Choice");
					break;
				}
				case 8: {
					if (userType.equals("user"))
						ua.BookTicketForOthers();
					else
						System.out.println("Please Choose Correct Choice");
					break;
				}
				case 9: {
					if (userType.equals("admin"))
						ra.GetChart();
					else
						System.out.println("Please Choose Correct Choice");
					break;
				}
				case 10: {
					val.ChangePassword();
					break;
				}
				case 11: {
					userType = null;
					break;
				}
				case 12: {
					System.exit(0);
				}
				default:
					System.out.println("Please Choose Correct Choice");
				}
			}
		}
	}
}
