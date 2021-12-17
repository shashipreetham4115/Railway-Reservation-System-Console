package entites;

public class Station {
	public String name, code, arrivalTime, departureTime, arrivalDate, departureDate;

	public Station(String name, String code, String arrivalDate, String arrivalTime, String departureDate,
			String departureTime) {
		this.name = name;
		this.code = code;
		this.arrivalDate = arrivalDate;
		this.arrivalTime = arrivalTime;
		this.departureTime = departureTime;
		this.departureDate = departureDate;
	}

}
