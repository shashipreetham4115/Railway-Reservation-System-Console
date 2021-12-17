package application;

import java.util.*;

import entites.Station;
import entites.Train;
import interfaces.RailwayAppServices;
import utilities.Inputs;
import utilities.ValidateInput;

public class RailwayUi extends Ui implements RailwayAppServices {
	Scanner sc = new Scanner(System.in);

	public void cancelTicket() {
		try {
			long ticketId = Inputs.getLong("Please Enter PNR");
			cancelTicket(ticketId);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// This function validates the user by taking username and password
	public String validateUser() {
		try {
			String username = Inputs.getString("Please Enter Your Username");
			if (login.isUsernameAvailable(username))
				System.out.println("\nIncorrect Username");
			else {
				String password = Inputs.getString("Please Enter Your Password");
				String userType = login.validateUser(username, password);
				if (userType != null && userType.equals("admin")) {
					greetUser();
					return userType;
				} else if (userType != null && userType.equals("user"))
					System.out.println("Invalid Username or Password");
				else
					System.out.println("\nIncorrect Password");
			}
			return "";
		} catch (Exception e) {
			System.out.println(e);
			return "";
		}
	}

	// This function is used to print all passengers in given train id
	public void printPassengers() {
		try {
			if (trainSoftware.validateTrainID(Inputs.getInt("Please Enter Your Train Id"))) {
				if (trainSoftware.getCurrentTrain().cnfList.size() == 0) {
					System.out.println("No BookingSoftware done till now");
					return;
				}

				ArrayList<Long> tickets = new ArrayList<>();
				tickets.addAll(trainSoftware.getCurrentTrain().cnfList);
				tickets.addAll(trainSoftware.getCurrentTrain().racWaitingList);
				tickets.addAll(trainSoftware.getCurrentTrain().waitingList);

				if (tickets.size() > 0) {
					System.out.println(tickets.size());
					printAllTickets(tickets);
				} else {
					System.out.println("No Ticket Booked till Now");
				}
			} else
				System.out.println("\nPlease Enter Valid Train Id");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// This function Overrides AddTrain of BookingSoftware Software to take all
	// inputs
	// required and then calls that function
	public void addTrain() {
		try {
			int trainNo = Inputs.getInt("Please Enter Train No");
			String name = Inputs.getLine("Please Enter Train Name").toUpperCase();
			String start = Inputs.getLine("Please Enter Start Station").toUpperCase();
			String startCode = Inputs.getLine("Please Enter Start Station Code").toUpperCase();
			String destination = Inputs.getLine("Please Enter Destination").toUpperCase();
			String destinationCode = Inputs.getLine("Please Enter Destination Station Code").toUpperCase();
			String date = ValidateInput.getDate();
			String time = ValidateInput.getTime();
			int totalComp = ValidateInput.getCompartment();
			int SeatsPerComp = ValidateInput.getValidSeatsPerComp();
			int stops = Inputs.getInt("Please Enter how many stops are there");
			String[] arrStations = new String[stops + 1];
			String[] arrCodes = new String[stops + 1];
			sc.nextLine();
			arrStations[0] = start;
			arrCodes[0] = startCode;
			for (int i = 1; i <= stops; i++) {
				System.out.print("\nPlease Enter " + i + "Stop      : ");
				arrStations[i] = sc.nextLine().toUpperCase();
				System.out.print("\nPlease Enter " + i + "Stop Code :");
				arrCodes[i] = sc.nextLine().toUpperCase();
			}
			arrStations[stops] = destination;
			arrCodes[stops] = destinationCode;
			printTrain(trainSoftware.addTrain(trainNo, name, start, destination, date, time, arrStations, arrCodes,
					SeatsPerComp, totalComp));
			System.out.println("Sucessfully Added...");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void printTrain(Train t) {
		try {
			System.out.print(
					"\n----------------------------------------------------------------------------------------------------------------------------------\n");
			System.out.print("\nTrain No            : " + t.id);
			System.out.print("\nTrain Name          : " + t.name);
			System.out.print("\nStart Station       : " + t.start);
			System.out.print("\nDestination Station : " + t.destination);
			System.out.print(
					"\n-----------------------------------------------------------------------------------------------------------------------------------\n");
			System.out.print(
					"\n------------------------------------------------------------Stops------------------------------------------------------------------\n");
			System.out.print(
					"\n-----------------------------------------------------------------------------------------------------------------------------------\n");
			System.out.format("%1$-20s%2$-20s%3$-20s%4$-20s%5$-20s%6$-20s", "Station Name", "Station Code",
					"Arrival Date", "Arrival Time", "Departure Date", "Departure Time");
			System.out.print(
					"\n-----------------------------------------------------------------------------------------------------------------------------------\n");
			for (Station s : t.arrStations) {
				System.out.format("\n%1$-20s%2$-20s%3$-20s%4$-20s%5$-20s%6$-20s\n", s.name, s.code, s.arrivalDate,
						s.arrivalTime, s.departureDate, s.departureTime);
			}

			System.out.print(
					"\n-----------------------------------------------------------------------------------------------------------------------------------\n");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// Overrides the PrintTicket of TrainSoftware class to get input and then calls
	// that
	// function
	public void printTicket() {
		try {
			printTicket(Inputs.getLong("Please Enter PNR"));
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// get Chart is function where final chart prepared is printed after taking all
	// inputs i.e train id.
	public void getChart() {
		try {
			if (trainSoftware.validateTrainID(Inputs.getInt("Please Enter Your Train Id"))) {
				printAllTickets(bookingSoftware.prepareChart(trainSoftware.getCurrentTrain()));
			} else
				System.out.println("\nPlease Enter Valid Train Id");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void addNewUser() {
		try {
			while (true) {
				switch (Inputs.getInt("1) Admin \n2) User \n3) Back \nPlease Choose User Role")) {
				case 1: {
					addNewUser("admin");
					break;
				}
				case 2: {
					addNewUser("user");
					break;
				}
				case 3:
					return;
				default:
					System.out.println("Please Enter Valid Option");
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
