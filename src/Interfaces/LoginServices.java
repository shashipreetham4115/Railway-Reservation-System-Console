package interfaces;

public interface LoginServices {
	
	 String validateUser();

	 boolean changePassword();

	 void addNewUser(String role);
	
}
