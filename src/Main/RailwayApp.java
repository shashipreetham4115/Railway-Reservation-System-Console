package Main;

import java.util.*;

import Interfaces.RailwayAppServices;
import Utilities.Inputs;

public class RailwayApp extends App implements RailwayAppServices {
	Scanner sc = new Scanner(System.in);

	// This function is used to print all passengers in given train id
	public void PrintPassengers() {
		if (ValidateTrainID(Inputs.GetLong("Please Enter Your Train ID"))) {
			if (currentTrain.passenger_details.size() == 0) {
				System.out.println("No Bookings done till now");
				return;
			}
			PrintAllTickets(currentTrain.passenger_details.values());
		} else
			System.out.println("\nPlease Enter Valid Train ID");
	}

	// This function Overrides AddTrain of Booking Software to take all inputs
	// required and then calls that function
	public void AddTrain() {

		String name = Inputs.GetLine("Please Enter Train Name");
		String start = Inputs.GetLine("Please Enter Start Station");
		String destination = Inputs.GetLine("Please Enter Destination");
		String date = Inputs.GetString("Please Enter Date");
		String time = Inputs.GetString("Please Enter Time");
		int totalComp = Inputs.GetInt("Please Enter Total No of Compartments");
		int SeatsPerComp = Inputs.GetInt("Please Enter Total Seats Per Compartment");
		int stops = Inputs.GetInt("Please Enter how many stops are there");
		String[] arr = new String[stops + 1];
		sc.nextLine();
		arr[0] = start;
		for (int i = 1; i <= stops; i++) {
			System.out.print("\nPlease Enter " + i + "Stop : ");
			arr[i] = sc.nextLine();
		}
		arr[stops] = destination;
		AddTrain(name, start, destination, date, time, arr, SeatsPerComp, totalComp);
		System.out.println("Sucessfully Added...");
	}

	// Overrides the PrintTicket of App class to get input and then calls that
	// function
	public void PrintTicket() {
		PrintTicket(Inputs.GetLong("Please Enter Your Ticket ID"));
	}

	// Get Chart is function where final chart prepared is printed after taking all
	// inputs i.e train id.
	public void GetChart() {
		if (ValidateTrainID(Inputs.GetLong("Please Enter Your Train ID"))) {
			PrintAllTickets(PrepareChart(currentTrain));
		} else
			System.out.println("\nPlease Enter Valid Train ID");
	}

}
