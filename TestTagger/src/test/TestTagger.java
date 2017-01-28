package test;

import edu.stanford.nlp.tagger.maxent.*;

public class TestTagger {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MaxentTagger tagger = new MaxentTagger("models/english-bidirectional-distsim.tagger");
		String taggedString = tagger.tagString("Library issues books to students.");
		System.out.println(taggedString);
	}

}
