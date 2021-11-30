package Interfaces;

import java.util.ArrayList;
import java.util.Collection;

import Entites.Ticket;
import Entites.Train;

public interface BookingServices {
	
	abstract Ticket BookTicket(String name, int age, String start, String destination, String preferedberth);

	abstract Train AddTrain(String name, String start, String destination, String date, String time, String[] arr,
			int seatsPerComp, int totalComp);

	abstract Collection<Ticket> PrepareChart(Train t);
	
	abstract boolean CancelTicket(long id);
	
	abstract boolean ValidateTrainID(long train_id);
	
	abstract ArrayList<Train> GetTrains(String start, String destination, String date);

}
