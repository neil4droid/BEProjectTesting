package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

public class TestParser {
	public static void main(String[] args) {
		String parserModel = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
		
		//If parser model is given as params,
		if(args.length > 0) {
			parserModel = args[0];
		}
		
		LexicalizedParser lp = LexicalizedParser.loadModel(parserModel);
		
		demoAPI(lp);
	}

	private static void demoAPI(LexicalizedParser lp) {
		// TODO Auto-generated method stub
		String sent2 = "A library issues books to students and teachers.";
		
		//Think of this as a factory, that creates tokenizers... :P
		TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
		
		//Get a tokenizer from the above created factory
		Tokenizer<CoreLabel> tok = tokenizerFactory.getTokenizer(new StringReader(sent2));
		
		//Use this tokenizer to tokenize the above String sent2
		List<CoreLabel> rawWords = tok.tokenize();
		
		//System.out.println(rawWords);
		System.out.println();
		
		//Parse the above tokenized sentence
		Tree parse = lp.apply(rawWords);
		
		//Print the collapsed dependencies
		File file = new File("H:\\Chinmay\\StanfordParserOutputs.txt");
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pw.println("Output of Stanford Parser in differnet formats:");
		pw.println();
		
		pw.println("penn:");
	    TreePrint tp = new TreePrint("penn");
	    tp.printTree(parse, pw);
	    pw.println();
	    pw.println("wordsAndTags:");
	    tp = new TreePrint("wordsAndTags");
	    tp.printTree(parse, pw);
	    pw.println();
	    pw.println("oneline:");
	    tp = new TreePrint("oneline");
	    tp.printTree(parse, pw);
	    pw.println();
	    pw.println("rootSymbolOnly:");
	    tp = new TreePrint("rootSymbolOnly");
	    tp.printTree(parse, pw);
	    pw.println();
	    pw.println("words:");
	    tp = new TreePrint("words");
	    tp.printTree(parse, pw);
	    pw.println();
	    pw.println("dependencies:");
	    tp = new TreePrint("dependencies");
	    tp.printTree(parse, pw);
	    pw.println();
	    pw.println("typedDependencies:");
	    tp = new TreePrint("typedDependencies");
	    tp.printTree(parse, pw);
	    pw.println();
	    pw.println("latexTree:");
	    tp = new TreePrint("latexTree");
	    tp.printTree(parse, pw);
	    pw.println();
	    pw.println("xmlTree:");
	    tp = new TreePrint("xmlTree");
	    tp.printTree(parse, pw);
	    pw.println();
	    pw.println("semanticGraph:");
	    tp = new TreePrint("semanticGraph");
	    tp.printTree(parse, pw);
	    pw.println();
	    pw.println("conllStyleDependencies:");
	    tp = new TreePrint("conllStyleDependencies");
	    tp.printTree(parse, pw);
	    pw.println();
	    pw.println("conll2007:");
	    tp = new TreePrint("conll2007");
	    tp.printTree(parse, pw);
		
	    pw.close();
	    
	    TreebankLanguagePack tlp = lp.treebankLanguagePack(); // PennTreebankLanguagePack for English
	    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
	    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
	    List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
	    
	    System.out.println(sent2);
	    System.out.println(tdl);
	    System.out.println("tdl.get(3) = "+tdl.get(3));
	    System.out.println("tdl.get(3).dep() = "+tdl.get(8).dep());
	    System.out.println("tdl.get(3).gov() = "+tdl.get(8).gov());
	    System.out.println("tdl.get(3).reln() = "+tdl.get(8).reln().toString());
	    //GrammaticalRelation newGR = new GrammaticalRelation(null, "dobj", "", null);
	    //System.out.println(tdl.get(3).reln().compareTo(newGR));
	    System.out.println(tdl.get(3).reln().getLanguage());
	    System.out.println(tdl.get(3).reln().getParent());
	    System.out.println(tdl.get(3).reln().getShortName());
	    System.out.println(tdl.get(3).reln().getSpecific());
	    System.out.println(tdl.get(3).reln().getLanguage());
	    System.out.println(tdl.get(3).reln().getLongName());
	    
	    //Print the parsed output
	    //System.out.println(parse);
	}
}



