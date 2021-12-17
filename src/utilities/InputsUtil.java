package utilities;

import java.util.Scanner;

public class InputsUtil {

	static Scanner sc = new Scanner(System.in);

	public static int getInt(String request) {
		while (true) {
			try {
				return Integer.parseInt(getString(request));
			} catch (Exception e) {
				System.out.println("Please Enter Valid Input");
			}
		}
	}

	public static String getString(String request) {
		System.out.print("\n" + request + " : ");
		return sc.next();
	}

	public static String getLine(String request) {
		sc.nextLine();
		System.out.print("\n" + request + " : ");
		return sc.nextLine();
	}

	public static long getLong(String request) {
		while (true) {
			try {
				return Long.parseLong(getString(request));
			} catch (Exception e) {
				System.out.println("Please Enter Valid Input");
			}
		}
	}

}
