package Entites;

import java.util.HashMap;
import java.util.Map;

public class User extends Human{

	private String password;
	public String userType;
	public Map<Long,Ticket> user_tickets;
	
	public User(String password,String name,String gender,int age,String userType) {
		this.name = name;
		this.password = password;
		this.age = age;
		this.gender = gender;
		this.id = d.getTime();
		this.userType = userType;
		if(userType.equals("user"))  user_tickets = new HashMap<Long,Ticket>();
	}
	
	public boolean verifyUser(String password) {
		return this.password.equals(password);
	}
	
	public boolean changePassword(String oldPassword,String newPassword) {
		if(oldPassword.equals(password)) {
			this.password = newPassword;
			return true;
		}
		return false;
	}

}
