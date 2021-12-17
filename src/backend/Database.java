package backend;

import java.util.HashMap;
import java.util.Map;

import entites.Ticket;
import entites.Train;
import entites.User;

public class Database {

	private Map<Integer, Train> avaTrains = new HashMap<>();
	private Map<Integer, User> users = new HashMap<>();
	private Map<Long, Ticket> passengerDetails = new HashMap<Long, Ticket>();
	private Map<String, Integer> usernames = new HashMap<>();
	private String loggedInUser;
	private int userId = 0;

	public int getUserId() {
		return ++userId;
	}

	private static Database instance = null;

	private Database() {
	}

	public static Database getInstance() {
		if (instance == null)
			instance = new Database();
		return instance;
	}

	public Map<Integer, Train> getAvaTrains() {
		return avaTrains;
	}

	public Map<Integer, User> getUsers() {
		return users;
	}

	public Map<Long, Ticket> getPassengerDetails() {
		return passengerDetails;
	}

	public Map<String, Integer> getUsernames() {
		return usernames;
	}

	public String getLoggedInUserId() {
		return loggedInUser;
	}

	public void setLoggedInUserId(String loggedInUser) {
		this.loggedInUser = loggedInUser;
	}
	
	public User getLoggedInUser() {
		return users.get(usernames.get(loggedInUser));
	}

}
