package test;

/*
 * Also removes semi-colons
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;

public class RemoveConjunction {
	
	public static List<String> removeAnd(String sentence) {
		//works for only active voice with single and
		List<String> andRemovedSentences = new ArrayList<String>();
		
		//get the parse tree and dependencies
		Tree parse = Parser.getParseTree(sentence);
		List<TypedDependency> tdl = Parser.getTypedDependencies(parse);
		
		//check if and is present
		List<Tree> andNodes = TreeManipulation.searchNode("and", parse);
		if(andNodes.isEmpty()) {
			//no ands found
			System.out.println("No and found");
			andRemovedSentences.add(sentence);
		} else {
			//and(s) is found.
			System.out.println("Number of ands found = "+andNodes.size());
			
			//first, check the voice of the sentence
			int voice = MiscAPI.getVoice(tdl);
			if(voice == 0) {
				//passive voice
				System.out.println("Sentence is in passive voice.");
				//code to be written
				
			} else if(voice == 1) {
				//active voice
				
				//For now, consider only one and is present in the sentence.
				
				//Store parent of the CC node
				Tree CCNode = andNodes.get(0).ancestor(1, parse);
				Tree CCParent = CCNode.ancestor(1, parse);
				
				//Get the 1st sentence
				Iterator<Tree> CCIterator = parse.iterator();
				//get the first sentence
				String firstSentence = "";
				while( CCIterator.hasNext() ) {
					Tree node = CCIterator.next();
					if( node.equals(CCNode) ) {
						Tree CCParentNextSibling = TreeManipulation.getNextSibling(CCParent, parse);
						if(CCParentNextSibling == null) {
							while(CCParentNextSibling == null) {
								CCParentNextSibling = TreeManipulation.getNextSibling(CCParent.ancestor(1,parse), parse);
							}
						}
						while(!node.equals(CCParentNextSibling)) {
							node = CCIterator.next();
						}
					}
					if( node.isLeaf() ) {
						firstSentence += node.value() + " ";
					}
				}
				//Remove space between terminator and sentence
				firstSentence = MiscAPI.placeTerminator(firstSentence);
				andRemovedSentences.add(firstSentence);
				
				//Get the second sentence
				String secondSentence = "";
				Iterator<Tree> CCParentIterator = parse.iterator();
				if( !TreeManipulation.getNextSibling(CCNode, parse).value().equalsIgnoreCase("VP") ) {
					//There is no VP after CC
					//E.g. library issues books and loans to students
					//Take NP+VBZ from the VP before the CC
					while(CCParentIterator.hasNext()) {
						Tree node = CCParentIterator.next();
						//if( node.value().equals(".") )
							//break;
						if( node.equals(CCParent) ) {
							//break;
							while( !node.equals(CCNode) ) {
								node = CCParentIterator.next();
							}
							//System.out.println(node.value());
							node = CCParentIterator.next();
							node = CCParentIterator.next();
							//System.out.println(node.value());
						} else if(node.isLeaf()) {
							secondSentence += node.value() + " ";
							//System.out.print(node.value()+" ");
						}
					}
				}
				else if ( TreeManipulation.getNextSibling(CCNode, parse).value().equalsIgnoreCase("VP") ) {
					//There is a VP after CC
					//E.g. The library issues books to students and issues loans to teachers.
					//Take NP from before CC and the rest after CC
					Tree node = null;
					while( CCParentIterator.hasNext() ) {
						node = CCParentIterator.next();
						//if( node.value().equalsIgnoreCase(".") )
							//break;
						if( node.value().equalsIgnoreCase("VP") ) {
							while( !node.equals(TreeManipulation.getNextSibling(CCNode, parse)) ) {
								node = CCParentIterator.next();
							}
						}
						if( node.isLeaf() ) {
							secondSentence += node.value() + " ";
							//System.out.print(node.value()+" ");
						}
					}
				}
				else if ( TreeManipulation.getNextSibling(CCNode, parse).value().equalsIgnoreCase("NP") ) {
					//THere is NP after CC
					//E.g. Library issues books to students and school is a building. 
					//Directly split at the CC
					Tree node = TreeManipulation.getNextSibling(CCParent, parse);
					CCParentIterator = node.iterator();
					while( CCParentIterator.hasNext() ) {
						node = CCParentIterator.next();
						//if( node.value().equals(".") )
							//break;
						if( node.isLeaf() ) {
							secondSentence += node.value() + " ";
							//System.out.print(node.value()+" ");
						}
					}
				}
				secondSentence = MiscAPI.placeTerminator(secondSentence);
				andRemovedSentences.add(secondSentence);
			}
		}
		
		return andRemovedSentences;
	}

	public static String removeSemicolon(String sentence) {
		String temp = "";
		Tree parse = Parser.getParseTree(sentence);
		Iterator<Tree> it = parse.iterator();
		while(it.hasNext()) {
			Tree node = it.next();
			if(!node.value().toString().equalsIgnoreCase(";")) {
				if(node.isLeaf()) {
					temp = temp + node.value().toString()+" ";
				}
			} else {
				//; is found
				while( !(node.value().toString().equals(".") || node.value().toString().equals("?") || node.value().toString().equals("!")) ) {
					node = it.next();
				}
				//temp = temp + node.value().toString();
			}
		}
		temp = MiscAPI.placeTerminator(temp);
		//System.out.println(temp);
		return temp;
	}
	
	public static void main(String[] args) {
		String sentence = "A coach has a level of accreditation; a number of years of experience.";
		
		String ars = removeSemicolon(sentence);
		System.out.println(ars);
	}
}
