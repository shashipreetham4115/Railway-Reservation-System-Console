package handlers;

import java.util.*;

import entites.User;

public class LoginHandler {
	private Database db = Database.getInstance();
	private Map<String, Integer> usernames = db.getUsernames();
	private Map<Integer, User> users = db.getUsers();
	private User loggedInUser = db.getLoggedInUser();

	// This Constructor creates 3 Users at start of the APP
	public LoginHandler() {
		User l1 = new User("p", "Shashipreetham", "Male", 21, "admin");
		users.put(l1.id, l1);
		usernames.put("shashi", l1.id);
		User l2 = new User("p", "Mahesh", "Male", 21, "admin");
		users.put(l2.id, l2);
		usernames.put("mahesh", l2.id);
		User l3 = new User("p", "Balaji", "Male", 21, "user");
		users.put(l3.id, l3);
		usernames.put("royal", l3.id);
	}

	// This function validates the user by taking user name and password
	public String validateUser(String username, String password) {
		if (usernames.containsKey(username)) {
			if (getUser(usernames.get(username)).getPassword().equals(password)) {
				db.setLoggedInUserId(username);
				loggedInUser = db.getLoggedInUser();
				return loggedInUser.userType;
			}
		}
		return null;
	}

	// This Function is used to Add new User
	public boolean addNewUser(String un, String pw, String name, String gender, int age, String role) {
		try {
			User l = new User(pw, name, gender, age, role);
			users.put(l.id, l);
			usernames.put(un, l.id);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// This function is used to change password which will ask for old password and
	// if it is correct then user able to set new password
	public boolean changePassword(String oldPassword, String newPassword) {
		if (verifyPassword(oldPassword)) {
			loggedInUser.setPassword(newPassword);
			return true;
		}
		return false;
	}

	public boolean verifyPassword(String password) {
		return loggedInUser.getPassword().equals(password);
	}

	public String getUsername() {
		return loggedInUser.name;
	}

	public boolean isUsernameAvailable(String username) {
		return !usernames.containsKey(username);
	}

	public User getLoggedInUser() {
		return loggedInUser;
	}

	public User getUser(int id) {
		return users.get(id);
	}

}
