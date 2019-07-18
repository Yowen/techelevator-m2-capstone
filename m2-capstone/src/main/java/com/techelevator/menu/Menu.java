package com.techelevator.menu;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class Menu {
	
	private PrintWriter out;
	private Scanner in;
	
	public Menu(InputStream input, OutputStream output) {
	    this.out = new PrintWriter(output);
	    this.in = new Scanner(input);
	}
	
	public String getInputAndOutput(String msg) {
		System.out.print(msg);
		String input = in.nextLine();
		return input;
	}
	
	public void output(String msg) {
		System.out.print(msg);
	}
	
}
