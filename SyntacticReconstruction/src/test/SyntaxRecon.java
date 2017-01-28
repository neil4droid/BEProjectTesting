package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.List;
import java.util.Scanner;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

/*
Take individual sentences and reconstruct them.
See that the sentences follow the following 8 rules after the reconstruction is done.
*/
public class SyntaxRecon {
	/*
	Syntax reconstruction rules: 
	1. Discard prepositional phrase (PP), adjective phrase
	(ADJP), determiner (DT) or adjective (JJ), if they
	precedes the subject of the sentence.
	
	2. If NP and VP is preceded by �No�, then convert it
	into �NP not VP�.
	
	3. Noun phrases (NP) which are separated by
	connectives like �and, or� are taken as individual
	sentences. If {{NP1}{VP1{ VBZ NP2,NP3 and
	NP4}}} then convert it into {{NP1}{VP1{ VBZ
	NP2 }}}, {{NP1}{VP1{ VBZ NP3}}},
	{{NP1}{VP1{ VBZ NP4}}}.
	
	4. Sentences which are connected by connectives like
	�and, or, but, yet� are split at their connectives
	and created at two individual sentences. If sentence1
	and/or sentence2, then convert it into two sentences
	{sentence1} {sentence2}.
	
	5. If a sentence has no verbs (VP) then discard that
	sentence.
	
	6. If a sentence is of the form {{NP1} {VP1 {NP2}
	{VP2 {NP3}}}}, then convert it into two sentences
	like {{NP1} {VP1 {NP2}}} and {{NP2} {VP2
	{NP3}}}.
	
	7. In the Sentences which are having a semicolon, treat
	the sentence after the semicolon as extra
	information for the preceding sentence and so
	discard sentence after semicolon.
	
	8. If a sentence is in passive voice, ask user to convert
	it into active voice. Normally passive voice
	sentences will contain word �be� which gives the
	sense as passive voice form. This needs some user
	interference to decide which sentence acts as
	passive voice.
	*/
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Open the file containing individual sentences for reconstruction
		File sentencesFile = new File("H:\\Chinmay\\workspace\\SyntacticReconstruction\\sentences.txt");
		Scanner sentencesFileScanner = null;
		try {
			sentencesFileScanner = new Scanner(sentencesFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Now, scan each of the sentences and perform syntactic reconstruction.
		//For now, we will test only the first sentence.
		String sentence1 = sentencesFileScanner.nextLine();
		
		//First we need to parse the sentence.
		//We will work with the universal Stanford dependencies and the tagged output of the parser.
		//The following method takes the sentence string and writes the tagged sentence and 
		//dependencies in the given sentence to a file "parserOutput.txt"
		List<TypedDependency> dependencyList = parseSentence(sentence1);
		
		//Now, we have the parser output for the sentence in the "parserOutput.txt" file
		File parserOutput = new File("H:\\Chinmay\\workspace\\SyntacticReconstruction\\parserOutput.txt");
		Scanner parserOutputFileScanner = null;
		try {
			parserOutputFileScanner = new Scanner(parserOutput);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*while(parserOutputFileScanner.hasNext()) 
			System.out.println(parserOutputFileScanner.next());*/
		removeAnd(dependencyList);
	}

	private static void removeAnd(List<TypedDependency> dependencyList) {
		// TODO Auto-generated method stub
		boolean andFound = false;
		for(int i=0; i<dependencyList.size(); i++) {
			String relation = dependencyList.get(i).reln().toString();
			if( relation.equals("conj:and") ) {
				andFound = true;
				break;
			}
		}
		if(!andFound) {
			System.out.println("And not found");
		} else {
			System.out.println("And found");
			
		}
	}

	private static List<TypedDependency> parseSentence(String sentence1) {
		// TODO Auto-generated method stub
		String parserModel = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
		LexicalizedParser lp = LexicalizedParser.loadModel(parserModel);
		
		//Think of this as a factory, that creates tokenizers... :P
		TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
				
		//Get a tokenizer from the above created factory
		Tokenizer<CoreLabel> tok = tokenizerFactory.getTokenizer(new StringReader(sentence1));
		
		//Use this tokenizer to tokenize the above String sent2
		List<CoreLabel> rawWords = tok.tokenize();
		
		//Parse the above tokenized sentence
		Tree parse = lp.apply(rawWords);
		
		//Create file "parserOutput.txt" and
		//Print the collapsed dependencies and tagged sentence to file
		File parserOutputFile = new File("H:\\Chinmay\\workspace\\SyntacticReconstruction\\parserOutput.txt");
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(parserOutputFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TreePrint tp = null;
		tp = new TreePrint("wordsAndTags");
		tp.printTree(parse, printWriter);
		tp = new TreePrint("oneline");
		tp.printTree(parse, printWriter);
		printWriter.println();
	    tp = new TreePrint("typedDependenciesCollapsed");
	    tp.printTree(parse, printWriter);
	    //printWriter.println();
	    
	    TreebankLanguagePack tlp = lp.treebankLanguagePack(); // PennTreebankLanguagePack for English
	    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
	    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
	    List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
	    /*String temp = tdl.get(3).toString();
	    System.out.println(tdl.get(3));
	    System.out.println(tdl.get(3).dep());
	    System.out.println(tdl.get(3).gov());
	    System.out.println(tdl.get(3).reln());
	    System.out.println();*/
	    
	    printWriter.close();
	    
	    //OpenFile.open("parserOutput.txt");
	    //System.out.print(parse.size()+" ");
	    //System.out.print(parse.numChildren()+"\n");
	    //System.out.println(parse.toString());
	    //parse.pennPrint();
	    return tdl;
	}

}
