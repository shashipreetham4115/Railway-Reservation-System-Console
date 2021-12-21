package interfaces;

import entites.User;

public interface LoginServices {

	public String validateUser(String username, String password);

	public boolean addNewUser(String un, String pw, String name, String gender, int age, String role);

	public boolean changePassword(String oldPassword, String newPassword);

	public boolean verifyPassword(String password);

	public String getUsername();

	public boolean isUsernameAvailable(String username);

	public User getLoggedInUser();

	public User getUser(int id);
}
