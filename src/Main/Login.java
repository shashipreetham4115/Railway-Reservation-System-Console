package Main;

import java.util.*;

import Entites.User;
import Interfaces.LoginServices;
import Utilities.Inputs;

public class Login implements LoginServices {
	Scanner sc = new Scanner(System.in);
	private static Map<String, User> user = new HashMap<String, User>();
	private static String loggedInUser;

	public Login() {
		User l1 = new User("p", "Shashipreetham", "Male", 21, "admin");
		user.put("shashi", l1);
		User l2 = new User("p", "Mahesh", "Male", 21, "admin");
		user.put("mahesh", l2);
		User l3 = new User("p", "Balaji", "Male", 21, "user");
		user.put("royal", l3);
	}

	public String ValidateUser() {
		switch (Inputs.GetInt("Please Select Your Choice \n 1) Login \n 2) New User")) {
		case 1:
			break;
		case 2: {
			addNewUser();
			break;
		}
		default:
			return null;
		}
		String username = Inputs.GetString("Please Enter Your Username");
		if (user.containsKey(username)) {
			if (user.get(username).verifyUser(Inputs.GetString("Please Enter Your Password"))) {
				loggedInUser = username;
				return user.get(username).userType;
			} else
				System.out.println("\nInvalid password");
		} else
			System.out.println("\nInvalid Username");
		return null;
	}

	public void addNewUser() {
		while (true) {
			String un = Inputs.GetString("Please Enter New Username");
			if (user.containsKey(un)) {
				System.out.println("Username not Available");
				continue;
			}
			sc.nextLine();
			String name = Inputs.GetLine("Please Enter Full Name");
			String gender = Inputs.GetString("Please Enter Your Gender");
			int age = Inputs.GetInt("Please Enter Your Age");
			String pw = Inputs.GetString("Please Enter Password");
			User l = new User(pw, name, gender, age, loggedInUser == null ? "user" : "admin");
			user.put(un, l);
			System.out.println("Successfully Added");
			break;
		}
	}

	public void ChangePassword() {
		if (user.get(loggedInUser).changePassword(Inputs.GetString("Please Enter Your Old Password"),
				Inputs.GetString("Please Enter Your New Password")))
			System.out.println("Your Password has been changed Successfully");
		else
			System.out.println("You Have Entered Wrong Password");
	}

	public void greetUser() {
		System.out.println("\nWelcome " + user.get(loggedInUser).name);
	}

	public static User getLoggedInUser() {
		return user.get(loggedInUser);
	}
}
