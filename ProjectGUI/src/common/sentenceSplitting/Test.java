package common.sentenceSplitting;

/*
 * Split a paragraph into sentences using the FormSentences class.
 * Input file = anotherTest.txt
 * Output file = sentences.txt
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Test {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//Using FormSentences class
		List<String> sentenceList = new ArrayList<String>();
		try {
			sentenceList = FormSentences.getSentences("anotherTest.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0; i<sentenceList.size(); i++) {
			System.out.println(sentenceList.get(i));
		}
		
		//Create a file containing separate sentences
		File file = new File("sentences.txt");
		try {
			PrintWriter printWriter = new PrintWriter(file);
			for(int i=0; i<sentenceList.size(); i++)
				printWriter.println(sentenceList.get(i));
			printWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
