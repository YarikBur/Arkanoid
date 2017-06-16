package ru.yarikbur.util;

import java.util.Scanner;

public class In {
	@SuppressWarnings({ "resource" })
	public int getInt(String str){
		Scanner input = new Scanner(System.in);
		System.out.print(str);
		Integer in = Integer.parseInt(input.nextLine());
		return in;
	}
	
	@SuppressWarnings({ "resource" })
	public String getString(String str){
		Scanner input = new Scanner(System.in);
		System.out.print(str);
		String in = input.nextLine();
		return in;
	}
}
