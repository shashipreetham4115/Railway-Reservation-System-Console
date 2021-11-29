package Entites;

public class Ticket extends Human {
    
	public String p_berth,p_status,from_station,to_station;
	public long train_id,user_id;

    public Ticket(String name, int age, String berth, String status,long train_id,String from_station,String to_station) {
        this.id = d.getTime();
        this.name = name;
        this.age = age;
        this.p_berth = berth;
        this.p_status = status;
        this.train_id = train_id;
        this.from_station = from_station;
        this.to_station = to_station;
    }

}
