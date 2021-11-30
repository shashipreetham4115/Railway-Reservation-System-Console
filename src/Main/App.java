package Main;

import java.util.ArrayList;
import java.util.Collection;

import Entites.Ticket;
import Entites.Train;
import Utilities.Inputs;

public class App extends BookingSoftware {

	// Overrides BookTicket method of BookingSoftware because before booking ticket
	// needs to get all inputs. So, here it will Taking all required inputs for
	// booking ticket
	public void BookTicket(String role) {
		String start = Inputs.GetLine("Enter From Station").toUpperCase();
		String end = Inputs.GetLine("Enter To Station").toUpperCase();
		String date = Inputs.GetString("Enter Date");
		Ticket t = null;
		if (PrintTrains(start, end, date))
			if (ValidateTrainID()) {
				String pb = Inputs.GetString("Please Enter Your Prefered Berth (UB,MB,LB,SU)").toUpperCase();
				if (role.equals("User"))
					t = BookTicket(Login.getLoggedInUser().name, Login.getLoggedInUser().age, start, end, pb);
				else
					t = BookTicket(Inputs.GetLine("Please Enter Name"), Inputs.GetInt("Please Enter Age"), start, end,
							pb);
			} else
				return;
		else
			return;
		if (t == null) {
			System.out.print("Tickets Not Available");
		} else {
			PrintTicket(t);
			System.out.print("\n Ticket Booked Sucessfully");
		}
	}

	// This Function will Print all Trains Available for the given start,
	// destination and date.
	public boolean PrintTrains(String start, String end, String date) {
		ArrayList<Train> trains = GetTrains(start, end, date);
		System.out.println(
				"\n\n-----------------------------------------------------------------------------------------");
		System.out.format("%1$-20s%2$-30s%3$-20s%4$-20s", "Train_ID", "Train", "Time", "Available Seats");
		System.out
				.println("\n-----------------------------------------------------------------------------------------");

		for (Train t : trains) {
			System.out.format("\n%1$-20s%2$-30s%3$-20s%4$-10s", t.id, t.name, t.time,
					CheckBerthAvalability(start, end, "at", t));
		}
		if (trains.size() == 0) {
			System.out.println("\nNo Trains Found...");
		}
		System.out.println(
				"\n-----------------------------------------------------------------------------------------\n");
		return trains.size() != 0;
	}

	// Overrides the ValidateTrain method of BookingSoftware because of taking
	// inputs and if it is wrong it will ask again
	boolean ValidateTrainID() {
		while (true) {
			long train_id = Inputs.GetLong("Please Enter Train Number from Above List or enter -1 to exit");
			if (train_id == -1)
				break;
			if (ValidateTrainID(train_id)) {
				return true;
			} else {
				System.out.print("\nPlease Enter Correct ID");
			}
		}
		return false;
	}

	// Function is Used to Get Ticket and Print All Ticket Details.
	private void PrintTicket(Ticket t) {
		System.out.println("----------------------------------------------------\n");
		System.out.println("Ticket ID : " + t.id);
		System.out.println("Name      : " + t.name);
		System.out.println("Age       : " + t.age);
		System.out.println("berth     : " + t.p_berth);
		System.out.println("From      : " + t.from_station);
		System.out.println("To        : " + t.to_station);
		System.out.println("Date      : " + ava_trains.get(t.train_id).date);
		System.out.println("Time      : " + ava_trains.get(t.train_id).time);
		System.out.println("Train     : " + ava_trains.get(t.train_id).name + " (ID : " + t.train_id + ")");
		System.out.println("-----------------------------------------------------\n");
	}

	// This Function Overrides the CancelTicket method in Booking SOftware and after
	// taking a valid ticket id it will call Cancel Ticket function in booking
	// software
	public void CancelTicket() {
		if (ValidateTrainID(Inputs.GetLong("Please Enter Your Train ID"))) {
			long ticket_id = Inputs.GetLong("Please Enter Ticket ID");
			if (currentTrain.passenger_details.containsKey(ticket_id)) {
				if (CancelTicket(Inputs.GetLong("Please Enter Ticket ID")))
					System.out.println("\nSuccessfully Canceled Ticket");
				else
					System.out.println("\nFailed To Cancel Your Ticket Please Try Again");
			} else
				System.out.println("\n Please Enter Valid Ticket ID");
		} else
			System.out.print("\nPlease Enter Valid Train ID");
	}

	// This Function Overloads the PrintTicket Function because before calling print
	// ticket it will verify the given ticket id and then it call if ticket id is
	// valid
	public void PrintTicket(long id) {
		if (ValidateTrainID(Inputs.GetLong("Please Enter Your Train ID"))) {
			if (currentTrain.passenger_details.containsKey(id))
				PrintTicket(currentTrain.passenger_details.get(id));
			else
				System.out.println("Please Enter the Valid Ticket ID");
		} else
			System.out.print("\nPlease Enter Valid Train ID");
	}

	// This Function is used to print collection of Tickets which are taken from the
	// parameters
	public void PrintAllTickets(Collection<Ticket> my_tickets) {
		System.out.println(
				"\n------------------------------------------------------------------------------------------------------------------------------");
		System.out.format("%1$-15s%2$-15s%3$-15s%4$-15s%5$-15s%6$-15s%7$-15s%8$-15s", "Ticket_ID", "Name", "Age",
				"From", "To", "Date", "Time", "Status/berth");
		System.out.println(
				"\n------------------------------------------------------------------------------------------------------------------------------\n");
		for (Ticket p : my_tickets) {
			Train t = ava_trains.get(p.train_id);
			System.out.format("%1$-15s%2$-15s%3$-15s%4$-15s%5$-15s%6$-15s%7$-15s%8$-15s", p.id, p.name, p.age,
					p.from_station, p.to_station, t.date, t.time, p.p_berth);
			System.out.println();
		}
		System.out.println(
				"\n------------------------------------------------------------------------------------------------------------------------------\n");
	}

}
