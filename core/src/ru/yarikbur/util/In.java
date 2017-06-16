package ru.yarikbur.util;

import java.util.Scanner;

public class In {
	@SuppressWarnings({ "resource" })
	public int getInt(){
		Scanner input = new Scanner(System.in);
		System.out.print("Input Int: ");
		Integer in = Integer.parseInt(input.nextLine());
		return in;
	}
	
	@SuppressWarnings({ "resource" })
	public String getString(){
		Scanner input = new Scanner(System.in);
		System.out.print("Input String: ");
		String in = input.nextLine();
		return in;
	}
}
