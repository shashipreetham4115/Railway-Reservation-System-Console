package handlers;

import java.util.*;

import entites.Ticket;
import entites.Train;
import entites.User;
import interfaces.BookingServices;

public class BookingHandler implements BookingServices {

	private Train currentTrain;

	Database db = Database.getInstance();

	// This function is used to Book Ticket and returns the Ticket
	public Ticket bookTicket(User user, String start, String destination, String preferedberth, Train train_id) {
		currentTrain = train_id;
		db.getUsers().put(user.id, user);
		Ticket p = new Ticket(0l, "", "", start, destination, currentTrain.id, user.id);
		p.berth = allocateBerth(start, destination, p.pnr, preferedberth);
		if (p.berth == null)
			return null;
		p.status = p.berth.charAt(0) == 'S' ? "SL" : p.berth.charAt(0) == 'R' ? "RAC" : "WL";
		p.pnr = getUniqueBerthNumber(p.berth);
		if (p.status.equals("SL"))
			currentTrain.cnfList.add(p.pnr);
		db.getPassengerDetails().put(p.pnr, p);
		addtoBookingTrack(p);
		if (db.getLoggedInUser().userType.equals("user"))
			db.getLoggedInUser().myBookings.add(p.pnr);
		return p;
	}

	// This Function is Used to Cancel the Ticket
	public boolean cancelTicket(long id) {
		if (db.getPassengerDetails().containsKey(id)) {
			Ticket ticket = db.getPassengerDetails().get(id);
			currentTrain = db.getAvaTrains().get(ticket.train);
			if (ticket.status.equals("SL")) {
				currentTrain.canceledTickets.add(ticket);
				currentTrain.cnfList.remove(id);
			} else if (ticket.berth.equals("RAC")) {
				currentTrain.racWaitingList.remove(id);
				if (!currentTrain.waitingList.isEmpty()) {
					currentTrain.racWaitingList.add(currentTrain.waitingList.poll());
					currentTrain.waitingTickets++;
				} else
					currentTrain.racTickets++;
				makeberthCorrections("RAC");
			} else {
				currentTrain.waitingList.remove(id);
				makeberthCorrections("WL");
				currentTrain.waitingTickets++;
			}
			db.getPassengerDetails().remove(id);
			return true;
		}
		return false;
	}

	// This Function is used to make final Chart where for RAC passenger also seats
	// are allocated and after this ticket bookings for this train is not allowed.
	public ArrayList<Long> prepareChart(Train train) {

		currentTrain = train;

		freeUpCanceledTicketSpace();

		allocateAvailableBerths(currentTrain.racWaitingList, "RAC");

		allocateAvailableBerths(currentTrain.waitingList, "WL");

		allocateRacSeats();

		ArrayList<Long> tickets = new ArrayList<>();
		tickets.addAll(currentTrain.cnfList);
		tickets.addAll(currentTrain.racWaitingList);
		tickets.addAll(currentTrain.waitingList);

		return tickets;
	}

	public String getAvailableTickets(String start, String end, Train train) {
		return checkBerthAvalability(start, end, "at", train);
	}

	public Ticket getTicket(long id) {
		return db.getPassengerDetails().get(id);
	}

	// This function is called after ticket booked and this is used to keep tract of
	// a berth like from where to where it is free to allocate berths for other
	// passengers
	private void addtoBookingTrack(Ticket p) {
		int start_index = currentTrain.arrStNames.indexOf(p.fromStation);
		int end_index = currentTrain.arrStNames.indexOf(p.toStation);
		for (int i = start_index; i < end_index; i++) {
			currentTrain.bookingTrack.get(p.berth).set(i, false);
		}
	}

	// This function is used to allocate berth.
	private String allocateBerth(String start, String destination, long id, String pb) {
		String ava_berth = !currentTrain.bookingTrack.isEmpty()
				? checkBerthAvalability(start, destination, "", currentTrain)
				: null;
		if (ava_berth == null) {
			if (currentTrain.availableTickets > 0) {
				ava_berth = checkPreferredBerth(pb);
				currentTrain.availableTickets--;
			} else if (currentTrain.racTickets > 0) {
				ava_berth = "RAC" + "/" + (currentTrain.racWaitingList.size() + 1);
				currentTrain.racWaitingList.add(id);
				currentTrain.racTickets--;
			} else if (currentTrain.waitingTickets > 0) {
				ava_berth = "WL" + "/" + (currentTrain.waitingList.size() + 1);
				currentTrain.waitingList.add(id);
				currentTrain.waitingTickets--;
			} else
				return null;
			if (ava_berth != null) {
				List<Boolean> track = new ArrayList<Boolean>(
						Arrays.asList(new Boolean[currentTrain.arrStNames.size()]));
				Collections.fill(track, Boolean.TRUE);
				currentTrain.bookingTrack.put(ava_berth, track);
			}
		}
		return ava_berth;
	}

	// This function is used to check weather preferred berth is available or not if
	// yes that is returned else random berth is allocated
	private String checkPreferredBerth(String pb) {
		int u = currentTrain.upper, l = currentTrain.lower, m = currentTrain.middle, su = currentTrain.sideUpper;
		if (u > 0 && pb.equals("UB"))
			return getUB(u);
		else if (l > 0 && pb.equals("LB"))
			return getLB(l);
		else if (m > 0 && pb.equals("MB"))
			return getMB(m);
		else if (su > 0 && pb.equals("SU"))
			return getSU(su);
		else if (u > 0 && u > m && u > su)
			return getUB(u);
		else if (m > l && m > su)
			return getMB(m);
		else if (l > su)
			return getLB(l);
		else
			return getSU(su);
	}

	// This Function keeps track of Upper Berth weather it is available or not if
	// not goes to another compartment(until total no of compartments).
	private String getUB(int u) {
		String berth = "S" + currentTrain.uComp + "/" + u + "/U";
		currentTrain.upper = u % 2 == 0 ? u - 3 : u - 5;
		if (currentTrain.upper <= 0 && currentTrain.uComp < currentTrain.totalComp) {
			currentTrain.upper = currentTrain.seatsPerComp - 2;
			currentTrain.uComp++;
		}
		return berth;
	}

	// This Function keeps track of Middle Berth weather it is available or not if
	// not goes to another compartment(until total no of compartments).
	private String getMB(int m) {
		String berth = "S" + currentTrain.mComp + "/" + m + "/M";
		currentTrain.middle = m % 2 == 0 ? m - 5 : m - 3;
		if (currentTrain.middle <= 0 && currentTrain.mComp < currentTrain.totalComp) {
			currentTrain.middle = currentTrain.seatsPerComp - 3;
			currentTrain.mComp++;
		}
		return berth;
	}

	// This Function keeps track of Lower Berth weather it is available or not if
	// not goes to another compartment(until total no of compartments).
	private String getLB(int l) {
		String berth = "S" + currentTrain.lComp + "/" + l + "/L";
		currentTrain.lower = l % 2 == 0 ? l - 3 : l - 5;
		if (currentTrain.lower <= 0 && currentTrain.lComp < currentTrain.totalComp) {
			currentTrain.lower = currentTrain.seatsPerComp - 4;
			currentTrain.lComp++;
		}
		return berth;
	}

	// This Function keeps track of Side Upper Berth weather it is available or not
	// if
	// not goes to another compartment(until total no of compartments).
	private String getSU(int su) {
		String berth = "S" + currentTrain.suComp + "/" + su + "/SU";
		currentTrain.sideUpper = su - 8;
		if (currentTrain.sideUpper <= 0 && currentTrain.suComp < currentTrain.totalComp) {
			currentTrain.sideUpper = currentTrain.seatsPerComp;
			currentTrain.suComp++;
		}
		return berth;
	}

	// This function is used to check Berth availability in already booked tickets
	// and also returns available seats status
	private String checkBerthAvalability(String start, String destination, String calledFor, Train t) {

		String berth = null;
		int min = Integer.MAX_VALUE;
		int start_index = t.arrStNames.indexOf(start.toUpperCase());
		int end_index = t.arrStNames.indexOf(destination.toUpperCase());
		int ava_count = t.availableTickets;
		for (String s : t.bookingTrack.keySet()) {
			int count = 0;
			for (int i = start_index; i < t.arrStNames.size(); i++) {
				if (t.bookingTrack.get(s).get(i))
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
			}
		}
		if (calledFor.equals("at")) {
			int rac = t.racWaitingList.size();
			int wt = t.waitingList.size();
			return ava_count > 0 ? "SL " + ava_count
					: t.racTickets > 0 ? "RAC " + rac : t.waitingTickets > 0 ? "WL " + wt : "Tickets Not Available";
		}
		return berth;
	}

	// This function is used to make berth corrections in RAC and Waiting List like
	// if any tickets canceled from RAC then this function is called
	private void makeberthCorrections(String qn) {
		int j = 1;
		for (Long i : qn.equals("RAC") ? currentTrain.racWaitingList : currentTrain.waitingList) {
			db.getPassengerDetails().get(i).berth = qn + "/" + j;
			db.getPassengerDetails().get(i).status = qn;
			j++;
		}
	}

	// This function is used while preparing charts it will called for allocated
	// side lower for all RAC passengers
	private void allocateRacSeats() {
		int rac_comp = 1;
		int seatsPerComp = (currentTrain.seatsPerComp / 8) * 2;
		int seat = currentTrain.seatsPerComp - 1;
		boolean allocated = false;
		currentTrain.racWaitingList.addAll(currentTrain.waitingList);
		for (Long id : currentTrain.racWaitingList) {
			Ticket t = db.getPassengerDetails().get(id);
			t.berth = "RAC/S" + rac_comp + "/" + seat + "/SL";
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

	// This function is used while preparing charts it will check weather any
	// canceled tickets are free to allocate RAC berths or waiting list berths first
	// preference is given to RAC
	private void allocateAvailableBerths(Queue<Long> list, String string) {
		for (Long id : list) {
			Ticket t = db.getPassengerDetails().get(id);
			String ava_berth = checkBerthAvalability(t.fromStation, t.toStation, "booking", currentTrain);
			if (ava_berth != null) {
				t.berth = ava_berth;
				t.status = "SL";
				if (string.equals("RAC"))
					currentTrain.racWaitingList.remove(id);
				else
					currentTrain.waitingList.remove(id);
			}
		}
	}

	// This function is used while preparing charts it will free up all seats of
	// canceled tickets in booking track so that AllocateAvailableBerth can find
	// Free berths
	private void freeUpCanceledTicketSpace() {
		for (Ticket t : currentTrain.canceledTickets) {
			int start_index = currentTrain.arrStNames.indexOf(t.fromStation);
			int end_index = currentTrain.arrStNames.indexOf(t.toStation);
			for (int i = start_index; i < end_index; i++) {
				currentTrain.bookingTrack.get(t.berth).set(i, true);
			}
		}
	}

	private Long getUniqueBerthNumber(String berth) {
		String[] given = berth.split("/");
		String compNo = given[0].substring(1);
		String newString = "" + currentTrain.id;
		newString += (int) given[0].charAt(0);
		newString += compNo.length() > 1 ? compNo : "0" + compNo;
		newString += given[1].length() > 1 ? given[1] : "0" + given[1];
		return Long.parseLong(newString);
	}

	protected Map<Long, Ticket> getPassengerDetails() {
		return db.getPassengerDetails();
	}

}
