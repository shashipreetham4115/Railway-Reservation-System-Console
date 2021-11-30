package Entites;

import java.util.*;

public class Train extends ID {
	public int available_tickets, rac_tickets, waiting_tickets, side_upper, upper, middle, lower,
			seatsPerComp, totalComp, u_comp = 1,l_comp = 1,m_comp = 1,su_comp=1;
	public String seat_berth = "L", start, destination, date, time, name;
	public Queue<Long> rac_waiting_list = new LinkedList<Long>();
	public Queue<Long> waiting_list = new LinkedList<Long>();
	public ArrayList<Ticket> canceled_tickets = new ArrayList<Ticket>();
	public Map<Long, Ticket> passenger_details = new HashMap<Long, Ticket>();
	public ArrayList<String> stops = new ArrayList<String>();
	public Map<String, List<Boolean>> booking_track = new HashMap<String, List<Boolean>>();

	public Train(String name, String start, String destination, String date, String time, int seatsPerComp,
			int totalComp) {
		
		this.rac_tickets = (seatsPerComp / 8) * 2 * totalComp;
		this.available_tickets = (seatsPerComp*totalComp) - (this.rac_tickets / 2);
		this.side_upper = seatsPerComp;
		this.upper = seatsPerComp - 2;
		this.middle = seatsPerComp - 3;
		this.lower = seatsPerComp - 4;
		this.start = start;
		this.destination = destination;
		this.date = date;
		this.time = time;
		this.name = name;
		this.id = d.getTime();
		this.waiting_tickets = rac_tickets / 2;
		this.totalComp = totalComp;
		this.seatsPerComp = seatsPerComp;
		System.out.println(available_tickets+" "+" "+rac_tickets+" "+waiting_tickets+" "+ totalComp+" "+seatsPerComp);
	}
}
