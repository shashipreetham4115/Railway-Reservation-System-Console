package entites;

public class Ticket {

	public String berth, status, fromStation, toStation;
	public long pnr;
	public int user,train;

	public Ticket(long pnr,String berth, String status, String fromStation, String toStation, int train, int user) {
		this.pnr = pnr;
		this.berth = berth;
		this.status = status;
		this.train = train;
		this.fromStation = fromStation;
		this.toStation = toStation;
		this.user = user;
	}

}
