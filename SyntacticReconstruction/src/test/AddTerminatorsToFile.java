package test;

/*
 * Add sentence terminators to AND removed sentences.
 * Check the sentence terminator at the end of the sentence and add it in required places.
 * 
 * Called by = SyntaxRecon.java => removeAnd()
 * 
 * Input file = andRemovedSentences.txt
 * Output file = andRemovedSentences2.txt
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AddTerminatorsToFile {
	
	public static void addTerminators(File file) {
		//File file = new File(fileName);
		Scanner t = null;
		try {
			t = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileWriter("andRemovedSentences2.txt", true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<String> sentences = new ArrayList<String>();
		while( t.hasNextLine() ) {
			sentences.add(t.nextLine());
		}
		
		for(int i=0; i<sentences.size(); i++) {
			sentences.add(i, sentences.remove(i).trim());
			System.out.println(sentences.get(i));
		}
		
		for(int i=0; i<sentences.size(); i=i+2) {
			String temp1 = sentences.remove(i);
			String temp2 = null;
			if( sentences.size() > 0 )
				temp2 = sentences.remove(i);
			char endChar = '0';
			if( temp2 != null && (temp1.charAt(temp1.length()-1) != '.' && temp1.charAt(temp1.length()-1) != '?' && temp1.charAt(temp1.length()-1) != '!') ) { 
				endChar = temp2.charAt(temp2.length()-1);
				temp1 = temp1 + endChar;
				System.out.println(temp1);
				pw.println(temp1);
			} else {
				endChar = temp1.charAt(temp1.length()-1);
				temp1 = temp1.substring(0, temp1.length()-2);
				temp1 = temp1 + endChar;
				System.out.println(temp1);
				pw.println(temp1);
			}
			if( temp2 != null ) {
				endChar = temp2.charAt(temp2.length()-1);
				temp2 = temp2.substring(0, temp2.length()-2);
				temp2 = temp2 + endChar;
				System.out.println(temp2);
				pw.println(temp2);
			}
		}
		pw.close();
	}
	
	
	public static void main(String[] args) {
		//String fileName = "andRemovedSentences.txt";
		addTerminators(new File("andRemovedSentences.txt"));
	}
	
}
