package Utilities;

import java.util.Scanner;

public class Inputs {
	static Scanner sc = new Scanner(System.in);

	public static int GetInt(String request) {
		System.out.print("\n" + request + " : ");
		return sc.nextInt();
	}

	public static String GetString(String request) {
		System.out.print("\n" + request + " : ");
		return sc.next();
	}

	public static String GetLine(String request) {
		sc.nextLine();
		System.out.print("\n" + request + " : ");
		return sc.nextLine();
	}

	public static long GetLong(String request) {
		System.out.print("\n" + request + " : ");
		return sc.nextLong();
	}

}
