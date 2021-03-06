package common.sentenceSplitting;

/*
 * Code to form proper sentences given a paragraph of text.
 * Called by Test.java
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.WordToSentenceProcessor;
import edu.stanford.nlp.util.StringUtils;

public class FormSentences {
	public static List<String> getSentences(String paragraph) throws FileNotFoundException {
		StringBuilder temp = new StringBuilder("");
		
		List<CoreLabel> tokens = new ArrayList<CoreLabel>();
		PTBTokenizer<CoreLabel> tokenizer = new PTBTokenizer<CoreLabel>(new StringReader(paragraph), new CoreLabelTokenFactory(), ""); 
		while(tokenizer.hasNext()) {
			tokens.add(tokenizer.next());
		}
		
		List<List<CoreLabel>> sentences = new WordToSentenceProcessor<CoreLabel>().process(tokens);
		
		int end;
		int start = 0;
		List<String> sentenceList = new ArrayList<String>();
		for (List<CoreLabel> sentence: sentences) {
		    end = sentence.get(sentence.size()-1).endPosition();
		    sentenceList.add(paragraph.substring(start, end).trim());
		    start = end;
		}
		
		return sentenceList;
	}
	
}
