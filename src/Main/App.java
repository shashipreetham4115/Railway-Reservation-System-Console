package Main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import Entites.Ticket;
import Entites.Train;
import Interfaces.BookingServices;

public class App extends BookingSoftware implements BookingServices {
	Scanner sc = new Scanner(System.in);

	public void BookTicket(String role) {
		String start = GetLine("Enter From Station").toUpperCase();
		String end = GetLine("Enter To Station").toUpperCase();
		String date = GetString("Enter Date");
		Ticket t = null;
		if (PrintTrains(start, end, date))
			if (ValidateTrainID()) {
				String pb = GetString("Please Enter Your Prefered Berth (UB,MB,LB,SU)").toUpperCase();
				if (role.equals("User"))
					t = BookTicket(Login.getLoggedInUser().name, Login.getLoggedInUser().age, start, end, pb);
				else
					t = BookTicket(GetLine("Please Enter Name"), GetInt("Please Enter Age"), start, end, pb);
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

	public boolean PrintTrains(String start, String end, String date) {
		ArrayList<Train> trains = GetTrains(start, end, date);
		System.out.println(
				"\n\n-----------------------------------------------------------------------------------------");
		System.out.format("%1$-20s%2$-30s%3$-20s%4$-20s", "Train_ID", "Train", "Time", "Available Seats");
		System.out
				.println("\n-----------------------------------------------------------------------------------------");

		for (Train t : trains) {
			System.out.format("\n%1$-20s%2$-30s%3$-20s%4$-10s", t.id, t.name, t.time,
					CheckBerthAvalability(start, end, "at"));
		}
		if (trains.size() == 0) {
			System.out.println("\nNo Trains Found...");
		}
		System.out.println(
				"\n-----------------------------------------------------------------------------------------\n");
		return trains.size() != 0;
	}

	boolean ValidateTrainID() {
		while (true) {
			long train_id = GetLong("Please Enter Train Number from Above List or enter -1 to exit");
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

	public void CancelTicket() {
		if (CancelTicket(GetLong("Please Enter Ticket ID")))
			System.out.println("\nSuccessfully Canceled Ticket");
		else
			System.out.println("\nFailed To Cancel Your Ticket Please Try Again");
	}

	public void PrintTicket(long id) {
		if (currentTrain.passenger_details.containsKey(id))
			PrintTicket(currentTrain.passenger_details.get(id));
		else
			System.out.println("Please Enter the Valid Ticket ID");
	}

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

	public int GetInt(String request) {
		System.out.print("\n" + request + " : ");
		return sc.nextInt();
	}

	public String GetString(String request) {
		System.out.print("\n" + request + " : ");
		return sc.next();
	}

	public String GetLine(String request) {
		sc.nextLine();
		System.out.print("\n" + request + " : ");
		return sc.nextLine();
	}

	public long GetLong(String request) {
		System.out.print("\n" + request + " : ");
		return sc.nextLong();
	}

}
