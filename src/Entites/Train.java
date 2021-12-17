package entites;

import java.util.*;

public class Train {
	public int availableTickets, racTickets, waitingTickets, sideUpper, upper, middle, lower, seatsPerComp, totalComp,
			uComp = 1, lComp = 1, mComp = 1, suComp = 1, id;
	public String seatBerth = "L", start, destination, date, time, name;
	public Queue<Long> racWaitingList = new LinkedList<Long>();
	public Queue<Long> waitingList = new LinkedList<Long>();
	public ArrayList<Long> cnfList = new ArrayList<Long>();
	public ArrayList<Ticket> canceledTickets = new ArrayList<Ticket>();
	public ArrayList<String> arrStNames = new ArrayList<String>();
	public ArrayList<String> arrStCodes = new ArrayList<String>();
	public ArrayList<Station> arrStations = new ArrayList<Station>();
	public Map<String, List<Boolean>> bookingTrack = new HashMap<String, List<Boolean>>();

	public Train(int trainNo, String name, String start, String destination, String date, String time, int seatsPerComp,
			int totalComp) {

		this.racTickets = (seatsPerComp / 8) * 2 * totalComp;
		this.availableTickets = (seatsPerComp * totalComp) - (this.racTickets / 2);
		this.sideUpper = seatsPerComp;
		this.upper = seatsPerComp - 2;
		this.middle = seatsPerComp - 3;
		this.lower = seatsPerComp - 4;
		this.start = start;
		this.destination = destination;
		this.date = date;
		this.time = time;
		this.name = name;
		this.id = trainNo;
		this.waitingTickets = racTickets / 2;
		this.totalComp = totalComp;
		this.seatsPerComp = seatsPerComp;
	}
}
