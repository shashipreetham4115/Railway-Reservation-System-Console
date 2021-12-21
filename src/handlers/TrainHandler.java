package handlers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

import entites.Station;
import entites.Train;
import interfaces.TrainServices;

public class TrainHandler implements TrainServices {

	private Train currentTrain;
	Database db = Database.getInstance();

	// Creates a Train at start of the TrainHandler
	public TrainHandler() {
		if (db.getAvaTrains().isEmpty()) {
			String[] arrStations = { "TENKASI", "SIVAKASI", "MADURAI", "DINDIGUL", "TIRUCHCHIRAPALLI",
					"CHENNAI EGMORE" };
			String[] arrCodes = { "TSI", "SVKS", "MDU", "DG", "TPJ", "MS" };
			currentTrain = addTrain(16102, "Chennai Egmore Express", "Tenkasi", "Chennai Egmore", "10/12/2021",
					"04:00 PM", arrStations, arrCodes, 8, 1);
		}
	}

	// This Function is Used to Filter TrainHandler available
	public ArrayList<Train> getTrains(String start, String destination, String date) {
		ArrayList<Train> trains = new ArrayList<Train>();

		for (Train p : db.getAvaTrains().values()) {
			int startIndex = p.arrStNames.indexOf(start.toUpperCase());
			int destinationIndex = p.arrStNames.indexOf(destination.toUpperCase());

			if (startIndex == -1)
				startIndex = p.arrStCodes.indexOf(start.toUpperCase());
			if (destinationIndex == -1)
				destinationIndex = p.arrStCodes.indexOf(destination.toUpperCase());

			if (startIndex != -1 && startIndex != -1 && date.equals(p.date))
				if (startIndex < destinationIndex)
					trains.add(p);
		}

		return trains;
	}

	// This Function is used to validate date a Train id given while booking ticket
	// and sets current Train if true.
	public boolean validateTrainID(int trainNo) {
		if (db.getAvaTrains().containsKey(trainNo)) {
			currentTrain = db.getAvaTrains().get(trainNo);
			return true;
		} else
			return false;
	}

	// This function is used to create a new Train
	public Train addTrain(int trainNo, String name, String start, String destination, String date, String time,
			String[] arrStations, String[] arrCodes, int seatsPerComp, int totalComp) {
		Train t = new Train(trainNo, name, start, destination, date, time, seatsPerComp, totalComp);
		db.getAvaTrains().put(t.id, t);
		Collections.addAll(t.arrStNames, arrStations);
		Collections.addAll(t.arrStCodes, arrCodes);
		Station sStart = new Station(arrStations[0], arrCodes[0], "----------", "--------", date, time);
		t.arrStations.add(sStart);
		String tempDate = date, tempTime = time;
		for (int i = 1; i < arrCodes.length - 1; i++) {
			String dt = getNextStopArraivalTimeAndDate(tempDate, tempTime);
			String arrDate = dt.split("&")[1], arrTime = dt.split("&")[0];
			String dtA5Mins = getTimeAndDateAfter5Mins(arrDate, arrTime);
			String depDate = dtA5Mins.split("&")[1], depTime = dtA5Mins.split("&")[0];
			Station s = new Station(arrStations[i], arrCodes[i], arrDate, arrTime, depDate, depTime);
			t.arrStations.add(s);
			tempDate = depDate;
			tempTime = depTime;
		}
		String dt = getNextStopArraivalTimeAndDate(tempDate, tempTime);
		String arrDate = dt.split("&")[1], arrTime = dt.split("&")[0];
		Station sLast = new Station(arrStations[arrCodes.length - 1], arrCodes[arrCodes.length - 1], arrDate, arrTime,
				"----------", "--------");
		t.arrStations.add(sLast);
		return t;
	}

	public Train getTrain(int id) {
		return db.getAvaTrains().get(id);
	}

	public Train getCurrentTrain() {
		return currentTrain;
	}

	private String getNextStopArraivalTimeAndDate(String preDate, String preTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
		LocalTime time = LocalTime.parse(preTime, formatter);
		float randomKms = (float) (Math.random() * 80) + 70;
		float avgSpeed = 60f;
		int hours = (int) (randomKms / avgSpeed);
		int mins = (int) (((randomKms / avgSpeed) - hours) * 60);
		time = time.plusMinutes(mins);
		time = time.plusHours(hours);
		String newTime = time.format(formatter);
		String newDate = preDate;
		if (preTime.split(" ")[1].equals("PM") && newTime.split(" ")[1].equals("AM")) {
			DateTimeFormatter ft = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate date = LocalDate.parse(preDate, ft);
			date.plusDays(1);
			newDate = date.plusDays(1).format(ft);
		}
		return newTime + "&" + newDate;
	}

	private String getTimeAndDateAfter5Mins(String preDate, String preTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
		LocalTime time = LocalTime.parse(preTime, formatter);
		time = time.plusMinutes(5);
		String newTime = time.format(formatter);
		String newDate = preDate;
		if (preTime.split(" ")[1].equals("PM") && newTime.split(" ")[1].equals("AM")) {
			DateTimeFormatter ft = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate date = LocalDate.parse(preDate, ft);
			date.plusDays(1);
			newDate = date.plusDays(1).format(ft);
		}
		return newTime + "&" + newDate;
	}

}