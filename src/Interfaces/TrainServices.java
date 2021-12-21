package interfaces;

import java.util.ArrayList;

import entites.Train;

public interface TrainServices {

	public ArrayList<Train> getTrains(String start, String destination, String date);

	public Train addTrain(int trainNo, String name, String start, String destination, String date, String time,
			String[] arrStations, String[] arrCodes, int seatsPerComp, int totalComp);

	public boolean validateTrainID(int trainNo);

	public Train getCurrentTrain();

	public Train getTrain(int id);

}
