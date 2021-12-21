package application;

import interfaces.RailwayAppServices;
import interfaces.UserAppServices;
import utilities.InputsUtil;

public class Main {
	public static void main(String[] args) {
		String userType = "";
		RailwayAppServices ra = new RailwayUi();
		UserAppServices ua = new UserUi();

		while (true) {
			while (userType == "") {
				switch (InputsUtil.getInt("1) Admin \n2) User \nPlease Choose Your Role ")) {
				case 1: {
					userType = ra.validateUser();
					break;
				}
				case 2: {
					userType = ua.validateUser();
					break;
				}
				default:
					System.out.println("Please Choose Correct Choice");
				}
			}

			while (userType.equals("admin")) {

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

				switch (InputsUtil.getInt("Enter Your Choice Here")) {
				case 1: {
					ra.bookTicket("Admin");
					break;
				}
				case 2: {
					ra.cancelTicket();
					break;
				}
				case 3: {
					ra.printTicket();
					break;
				}
				case 4:
					ra.printPassengers();
					break;
				case 5:
					ra.addNewUser();
					break;
				case 6: {
					ra.addTrain();
					break;
				}
				case 7: {
					ra.getChart();
					break;
				}
				case 8: {
					ra.changePassword();
					break;
				}
				case 9: {
					userType = "";
					break;
				}
				case 10: {
					System.exit(0);
				}
				default:
					System.out.println("Please Choose Correct Choice");
				}
			}
			while (userType.equals("user")) {

				System.out.println("\nPlease Choose Your Choice");
				System.out.println("1. Book Ticket");
				System.out.println("2. Book Ticket For Others");
				System.out.println("3. Cancel Ticket");
				System.out.println("4. Download Ticket");
				System.out.println("5. My Bookings");
				System.out.println("6. Change Password");
				System.out.println("7. Logout");
				System.out.println("8. Exit");

				switch (InputsUtil.getInt("Enter Your Choice Here")) {
				case 1: {
					ua.bookTicket("User");
					break;
				}
				case 2: {
					ua.bookTicketForOthers();
					break;
				}
				case 3: {
					ua.cancelTicket();
					break;
				}
				case 4: {
					ua.downloadTicket();
					break;
				}
				case 5: {
					ua.myBookings();
					break;
				}
				case 6: {
					ra.changePassword();
					break;
				}
				case 7: {
					userType = "";
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
