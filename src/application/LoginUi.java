package application;

import handlers.LoginHandler;
import interfaces.LoginServices;
import utilities.InputsUtil;
import utilities.ValidateInputUtil;

public class LoginUi {

	LoginServices loginHandler = new LoginHandler();

	public String validateUser() {
		try {
			while (true) {
				switch (InputsUtil.getInt("1) Login \n2) New User \n3) Back \nPlease Select Your Choice ")) {
				case 1: {
					String username = InputsUtil.getString("Please Enter Your Username");
					if (loginHandler.isUsernameAvailable(username))
						System.out.println("\nIncorrect Username");
					else {
						String password = InputsUtil.getString("Please Enter Your Password");
						String userType = loginHandler.validateUser(username, password);
						if (userType != null && userType.equals("user")) {
							greetUser();
							return userType;
						} else if (userType != null && userType.equals("user"))
							System.out.println("Invalid Username or Password");
						else
							System.out.println("\nInvalid Password");
					}
					break;
				}
				case 2: {
					addNewUser("user");
					break;
				}
				case 3:
					return "";
				default:
					System.out.println("Please Enter Valid Option");
				}
			}
		} catch (Exception e) {
			System.out.println(e);
			return "";
		}
	}

	// This function validates the user by taking username and password
	public String validateAdmin() {
		try {
			String username = InputsUtil.getString("Please Enter Your Username");
			if (loginHandler.isUsernameAvailable(username))
				System.out.println("\nIncorrect Username");
			else {
				String password = InputsUtil.getString("Please Enter Your Password");
				String userType = loginHandler.validateUser(username, password);
				if (userType != null && userType.equals("admin")) {
					greetUser();
					return userType;
				} else if (userType != null && userType.equals("user"))
					System.out.println("Invalid Username or Password");
				else
					System.out.println("\nIncorrect Password");
			}
			return "";
		} catch (Exception e) {
			System.out.println(e);
			return "";
		}
	}

	// This Function is used to Add new User
	public void addNewUser(String role) {
		try {
			String un = "";
			while (true) {
				un = InputsUtil.getString("Please Enter New Username");
				if (loginHandler.isUsernameAvailable(un)) {
					break;
				}
				System.out.println("Username not Available");
			}
			String name = InputsUtil.getLine("Please Enter Full Name");
			String gender = ValidateInputUtil.getGender();
			int age = ValidateInputUtil.getAge();
			String pw = InputsUtil.getString("Please Enter Password");
			if (loginHandler.addNewUser(un, pw, name, gender, age, role))
				System.out.println("Successfully Added");
			else
				System.out.println("Please Try Again");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// This function is used to change password which will ask for old password and
	// if it is correct then user able to set new password
	public void changePassword() {
		try {
			String oldPassword = InputsUtil.getString("Please Enter Your Old Password");
			if (loginHandler.verifyPassword(oldPassword)) {
				loginHandler.changePassword(oldPassword, InputsUtil.getString("Please Enter Your New Password"));
				System.out.println("Your Password has been changed Successfully");
			} else
				System.out.println("You Have Entered Wrong Password");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// Once User Logged In this Function Greets the User
	public void greetUser() {
		try {
			System.out.println("\nWelcome " + loginHandler.getUsername());
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
