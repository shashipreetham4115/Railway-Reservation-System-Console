package Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import Entites.Ticket;
import Entites.Train;
import Interfaces.BookingServices;

public class BookingSoftware implements BookingServices {
	static Train currentTrain;
	protected static Map<Long, Train> ava_trains = new HashMap<Long, Train>();

	public BookingSoftware() {
		if (ava_trains.isEmpty()) {
			String[] arr = { "Tenkasi", "Kadayanallur", "Sankarankovil", "Rajapalayam", "Srivilliputtur", "Sivakasi",
					"Virudunagar", "Madurai", "Dindigul", "Tiruchchirapalli", "Vriddhachalam", "Villupuram",
					"Chengalpattu", "Tambaram", "Chennai Egmore" };
			currentTrain = AddTrain("Chennai Egmore Express", "Tenkasi", "Chennai Egmore", "30-11-2021", "4:00PM", arr,
					8, 1);
		}
	}

	public Ticket BookTicket(String name, int age, String start, String destination, String preferedberth) {

		Ticket p = new Ticket(name, age, "", "", currentTrain.id, start, destination);
		p.p_berth = AllocateBerth(start, destination, p.id, preferedberth);
		if (p.p_berth == null)
			return null;
		p.p_status = p.p_berth.charAt(0) == 'S' ? "SL" : p.p_berth.charAt(0) == 'R' ? "RAC" : "WL";
		currentTrain.passenger_details.put(p.id, p);
		AddtoBookingTrack(p);
		if (Login.getLoggedInUser().userType.equals("user"))
			Login.getLoggedInUser().user_tickets.put(p.id, p);
		return p;
	}

	public ArrayList<Train> GetTrains(String start, String destination, String date) {
		ArrayList<Train> trains = new ArrayList<Train>();

		for (Train p : ava_trains.values()) {
			if (p.stops.indexOf(start.toUpperCase()) != -1 && p.stops.indexOf(destination.toUpperCase()) != -1
					&& date.equals(p.date)) {
				trains.add(p);
			}
		}

		return trains;
	}

	public boolean ValidateTrainID(long train_id) {
		if (ava_trains.containsKey(train_id)) {
			currentTrain = ava_trains.get(train_id);
			return true;
		} else
			return false;
	}

	public boolean CancelTicket(long id) {
		if (currentTrain.passenger_details.containsKey(id)) {
			if (currentTrain.passenger_details.get(id).p_status.equals("SL")) {
				currentTrain.canceled_tickets.add(currentTrain.passenger_details.get(id));
			} else if (currentTrain.passenger_details.get(id).p_berth.equals("RAC")) {
				currentTrain.rac_waiting_list.remove(id);
				if (!currentTrain.waiting_list.isEmpty()) {
					currentTrain.rac_waiting_list.add(currentTrain.waiting_list.poll());
					currentTrain.waiting_tickets++;
				} else
					currentTrain.rac_tickets++;
				MakeberthCorrections("RAC");
			} else {
				currentTrain.waiting_list.remove(id);
				MakeberthCorrections("WL");
				currentTrain.waiting_tickets++;
			}
			currentTrain.passenger_details.remove(id);
			Login.getLoggedInUser().user_tickets.remove(id);
			return true;
		}
		return false;
	}

	public Collection<Ticket> PrepareChart() {

		FreeUpCanceledTicketSpace();

		AllocateAvailableBerths(currentTrain.rac_waiting_list, "RAC");

		AllocateAvailableBerths(currentTrain.waiting_list, "WL");

		AllocateRacSeats();

		return currentTrain.passenger_details.values();
	}

	public Train AddTrain(String name, String start, String destination, String date, String time, String[] arr,
			int seatsPerComp, int totalComp) {
		Train t = new Train(name, start, destination, date, time, seatsPerComp, totalComp);
		ava_trains.put(t.id, t);
		Collections.addAll(t.stops, arr);
		t.stops.replaceAll(String::toUpperCase);
		return t;
	}

	private void AddtoBookingTrack(Ticket p) {
		int start_index = currentTrain.stops.indexOf(p.from_station);
		int end_index = currentTrain.stops.indexOf(p.to_station);
		for (int i = start_index; i < end_index; i++) {
			currentTrain.booking_track.get(p.p_berth).set(i, false);
		}
	}

	private String AllocateBerth(String start, String destination, long id, String pb) {
		String ava_berth = CheckBerthAvalability(start, destination, "");
		if (ava_berth == null) {
			if (currentTrain.available_tickets > 0) {
				ava_berth = CheckPerferedBerth(pb);
				currentTrain.available_tickets--;
			} else if (currentTrain.rac_tickets > 0) {
				ava_berth = "RAC" + "/" + (currentTrain.rac_waiting_list.size() + 1);
				currentTrain.rac_waiting_list.add(id);
				System.out.println(id);
				currentTrain.rac_tickets--;
			} else if (currentTrain.waiting_tickets > 0) {
				ava_berth = "WL" + "/" + (currentTrain.waiting_list.size() + 1);
				currentTrain.waiting_list.add(id);
				currentTrain.waiting_tickets--;
			} else
				return null;
			if (ava_berth != null) {
				List<Boolean> track = new ArrayList<Boolean>(Arrays.asList(new Boolean[currentTrain.stops.size()]));
				Collections.fill(track, Boolean.TRUE);
				currentTrain.booking_track.put(ava_berth, track);
			}
		}
		return ava_berth;
	}

	private String CheckPerferedBerth(String pb) {
		int u = currentTrain.upper, l = currentTrain.lower, m = currentTrain.middle, su = currentTrain.side_upper;
		if (u > 0 && pb.equals("UB"))
			return GetUB(u);
		else if (l > 0 && pb.equals("LB"))
			return GetLB(l);
		else if (m > 0 && pb.equals("MB"))
			return GetMB(m);
		else if (su > 0 && pb.equals("SU"))
			return GetSU(su);
		else if (u > l && u > m && u > su)
			return GetUB(u);
		else if (m > l && m > su)
			return GetMB(m);
		else if (l > su)
			return GetLB(l);
		else
			return GetSU(su);
	}

	private String GetUB(int u) {
		String berth = "S" + currentTrain.u_comp + "/" + u + "/U";
		currentTrain.upper = u % 2 == 0 ? u - 3 : u - 5;
		if (currentTrain.upper <= 0 && currentTrain.u_comp < currentTrain.totalComp) {
			currentTrain.upper = currentTrain.seatsPerComp - 2;
			currentTrain.u_comp++;
		}
		return berth;
	}

	private String GetMB(int m) {
		String berth = "S" + currentTrain.m_comp + "/" + m + "/M";
		currentTrain.middle = m % 2 == 0 ? m - 5 : m - 3;
		if (currentTrain.middle <= 0 && currentTrain.m_comp < currentTrain.totalComp) {
			currentTrain.middle = currentTrain.seatsPerComp - 3;
			currentTrain.m_comp++;
		}
		return berth;
	}

	private String GetLB(int l) {
		String berth = "S" + currentTrain.l_comp + "/" + l + "/L";
		currentTrain.lower = l % 2 == 0 ? l - 3 : l - 5;
		if (currentTrain.lower <= 0 && currentTrain.l_comp < currentTrain.totalComp) {
			currentTrain.lower = currentTrain.seatsPerComp - 4;
			currentTrain.l_comp++;
		}
		return berth;
	}

	private String GetSU(int su) {
		String berth = "S" + currentTrain.su_comp + "/" + su + "/SU";
		currentTrain.side_upper = su - 8;
		if (currentTrain.side_upper <= 0 && currentTrain.su_comp < currentTrain.totalComp) {
			currentTrain.side_upper = currentTrain.seatsPerComp;
			currentTrain.su_comp++;
		}
		return berth;
	}

	protected String CheckBerthAvalability(String start, String destination, String calledFor) {

		String berth = null;
		int min = Integer.MAX_VALUE;
		int start_index = currentTrain.stops.indexOf(start.toUpperCase());
		int end_index = currentTrain.stops.indexOf(destination.toUpperCase());
		int ava_count = currentTrain.available_tickets;
		for (String s : currentTrain.booking_track.keySet()) {
			int count = 0;
			for (int i = start_index; i < currentTrain.stops.size(); i++) {
				if (currentTrain.booking_track.get(s).get(i))
					count++;
				else
					break;
			}
			if ((end_index - start_index) <= count) {
				if (count < min) {
					min = count;
					berth = s;
				}
				ava_count++;
				System.out.println(ava_count);
			}
		}
		if (calledFor.equals("at")) {
			int rac = currentTrain.rac_waiting_list.size();
			int wt = currentTrain.waiting_list.size();
			return ava_count > 0 ? "SL " + ava_count
					: currentTrain.rac_tickets > 0 ? "RAC " + rac
							: currentTrain.waiting_tickets > 0 ? "WL " + wt : "Tickets Not Available";
		}
		return berth;
	}

	private void MakeberthCorrections(String qn) {
		int j = 1;
		for (Long i : qn.equals("RAC") ? currentTrain.rac_waiting_list : currentTrain.waiting_list) {
			currentTrain.passenger_details.get(i).p_berth = qn + "/" + j;
			currentTrain.passenger_details.get(i).p_status = qn;
			j++;
		}
	}

	private void AllocateRacSeats() {
		int rac_comp = 1;
		int seatsPerComp = (currentTrain.seatsPerComp / 8) * 2;
		int seat = currentTrain.seatsPerComp - 1;
		boolean allocated = false;
		currentTrain.rac_waiting_list.addAll(currentTrain.waiting_list);
		for (Long id : currentTrain.rac_waiting_list) {
			Ticket t = currentTrain.passenger_details.get(id);
			t.p_berth = "RAC/S" + rac_comp + "/" + seat + "/SL";
			if (allocated) {
				seat -= 8;
				if (seat <= 0 && rac_comp < currentTrain.totalComp) {
					seat = seatsPerComp - 1;
					currentTrain.totalComp++;
				}
			}
			if (seat <= 0 && rac_comp == currentTrain.totalComp)
				break;
			allocated = !allocated;
		}
	}

	private void AllocateAvailableBerths(Queue<Long> list, String string) {
		for (Long id : list) {
			Ticket t = currentTrain.passenger_details.get(id);
			String ava_berth = CheckBerthAvalability(t.from_station, t.to_station, "booking");
			if (ava_berth != null) {
				t.p_berth = ava_berth;
				t.p_status = "SL";
				if (string.equals("RAC"))
					currentTrain.rac_waiting_list.remove(id);
				else
					currentTrain.waiting_list.remove(id);
			}
		}
	}

	private void FreeUpCanceledTicketSpace() {
		for (Ticket t : currentTrain.canceled_tickets) {
			int start_index = currentTrain.stops.indexOf(t.from_station);
			int end_index = currentTrain.stops.indexOf(t.to_station);
			for (int i = start_index; i < end_index; i++) {
				currentTrain.booking_track.get(t.p_berth).set(i, true);
			}
		}
	}

}
