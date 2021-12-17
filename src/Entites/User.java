package entites;

import java.util.ArrayList;

import handlers.Database;

public class User {
	private String password;
	public String userType, name, gender;
	public int age, id;
	public ArrayList<Long> myBookings;

	public User(String name, String gender, int age) {
		this(null, name, gender, age, "anonymous");
	}

	public User(String password, String name, String gender, int age, String userType) {
		this.name = name;
		this.password = password;
		this.age = age;
		this.gender = gender;
		this.id = Database.getInstance().getUserId();
		this.userType = userType;
		if (userType.equals("user"))
			myBookings = new ArrayList<>();
	}

	// This is function which helps the LoginHandler to verify Password of the User without
	// returning the password
	public String getPassword() {
		return password;
	}

	// This function is used to change user password by verifying old password. If
	// the old password id correct then user able to change his password
	public void setPassword(String newPassword) {
		this.password = newPassword;
	}

}
