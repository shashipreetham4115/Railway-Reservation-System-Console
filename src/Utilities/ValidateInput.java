package utilities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class ValidateInput {

	private ValidateInput() {
	}

	public static String getGender() {
		String gender = Inputs.getString("Please Enter Gender (Male or Female or Other)");
		String[] arr = { "M", "MALE", "F", "FEMALE", "O", "OTHER" };
		if (Arrays.asList(arr).contains(gender.toUpperCase())) {
			return gender;
		}
		System.out.println("Please Enter Valid Gender");
		return getGender();
	}

	public static int getAge() {
		int age = Inputs.getInt("Please Enter Age");
		if (age > 5 && age < 110) {
			return age;
		}
		System.out.println("Please Enter Valid Age");
		return getAge();
	}

	public static String getDate() {
		String strDate = Inputs.getString("Please Enter Date");
		try {
			int date = Integer.parseInt(strDate.split("/")[0]);
			int month = Integer.parseInt(strDate.split("/")[1]);
			int year = Integer.parseInt(strDate.split("/")[2]);
			LocalDate ld = LocalDate.of(year, month, date);
			return ld.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		} catch (Exception e) {
			System.out.print("Date or Date format is Invalid \n" + "Date Format should be DD/MM/YYYY, "
					+ "example: 01/04/2021,29/12/2021)");
			return getDate();
		}
	}

	public static String getPreferedBerth() {
		String pb = Inputs.getString("Please Enter Your Prefered Berth (UB,MB,LB,SU)");
		String[] arr = { "UB", "LB", "MD", "SU" };
		if (Arrays.asList(arr).contains(pb.toUpperCase())) {
			return pb;
		}
		System.out.println("Please Enter Valid Berth (UB,MB,LB,SU)");
		return getGender();
	}

	public static int getValidSeatsPerComp() {
		int seats = Inputs.getInt("Please Enter Total Seats Per Compartment");
		if (seats >= 8) {
			if (seats % 8 == 0)
				return seats;
			return seats = seats - seats % 8;
		}
		System.out.println("Please Enter Valid Seats Per Compartment");
		return getValidSeatsPerComp();
	}

	public static String getTime() {
		String time = Inputs.getLine("Please Enter Time (Format : 10:12 AM, 12:01 PM)");
		try {
			if (time.length() < 8) {
				String hours = time.split(":")[0];
				hours = hours.length() < 2 ? "0" + hours : hours;
				String mins = time.split(":")[1].split(" ")[0];
				mins = mins.length() < 2 ? "0" + mins : mins;
				time = hours + ":" + mins + " " + time.split(" ")[1];
			}
			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
			LocalTime ltime = LocalTime.parse(time, timeFormatter);
			return ltime.format(timeFormatter);
		} catch (Exception e) {
			System.out.println("Time " + time
					+ " is invalid 12 Hours Format\nFollow this Format : (hh:mm a) here a refers to am or pm");
			return getTime();
		}

	}

	public static int getCompartment() {
		int comp = Inputs.getInt("Please Enter Total No of Compartments");
		if (comp > 0)
			return comp;
		System.out.println("Please Enter Valid No of Compartments");
		return getCompartment();
	}

}
