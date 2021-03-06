package selection.parser.one;

import tests.WordPOS;
import edu.mit.jwi.item.POS;

public class TaggedLemma {
	private String word;
	private POS tag;
	
	public TaggedLemma(String word, POS tag) {
		super();
		this.word = word;
		this.tag = tag;
	}
	
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public POS getTag() {
		return tag;
	}

	public void setTag(POS tag) {
		this.tag = tag;
	}

	public static TaggedLemma create(String taggedString) {
		int splitter = taggedString.lastIndexOf("_");
		String rWord = taggedString.substring(0, splitter);
		String rTag = taggedString.substring(splitter+1);
		
		return new TaggedLemma(findWord(rWord), findTag(rWord, rTag));
	}
	
	private static POS findTag(String word, String tag){
		if (word.equals("string")){
			return POS.NOUN;
		} else if (word.equals("close") || word.equals("println") || word.equals("key")){
			return POS.VERB;
		} else if(tag.startsWith("N")){
			return POS.NOUN;
		} else if(tag.startsWith("V")){
			return POS.VERB;
		} else if(tag.startsWith("J")){
				return POS.ADJECTIVE;
	    } else if(tag.startsWith("R")){
				return POS.ADVERB;
	    } else return POS.NOUN;		
	}
	
	private static String findWord(String word){
		if (word.equals("println")) return "print";
		else return word;
	}

}
