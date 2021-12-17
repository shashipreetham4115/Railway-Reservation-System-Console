package interfaces;

public interface AppServices {

	void bookTicket(String role);

	boolean cancelTicket(Long ticketId);

	String validateUser();

	void changePassword();

	void addNewUser(String role);

}
