package parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class ParserSliceComplexTokens implements IParser{

	private StanfordCoreNLP pipeline;

	public ParserSliceComplexTokens(StanfordCoreNLP pipeline) {
		this.pipeline = pipeline;
	}

	@Override
	public Input parse(Input input) {
		for (Sentence sentence : input.getSentences()) {
			Map<Integer, Group> groupMap = sentence.getGroupMap();

			for (Group group : groupMap.values()) {
				Token token = group.getToken();
				group.setTokenDecompositions(decomposeToken(token));
			}
		}

		return input;
	}

	private List<Token> decomposeToken(Token oldToken) {
		List<Token> newTokens = new LinkedList<Token>();
		String lemma = oldToken.getLemma();

		List<String> newLemmas = slice(lemma);

		if (newLemmas.size() > 1){

			String text = concatenate(newLemmas, " ");

			Annotation document = new Annotation(text);

			pipeline.annotate(document);

			List<CoreMap> sentences = document.get(SentencesAnnotation.class);
			
			for(CoreMap sentence: sentences) {
				for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
					Token newToken = new Token(token);
					newToken.setLemma(token.get(TextAnnotation.class));
					newToken.setPos(token.get(PartOfSpeechAnnotation.class));
					newTokens.add(newToken);
				}
			}
		}
		return newTokens;

	}

	private String concatenate(List<String> lemmas, String separator){
		StringBuffer sb = new StringBuffer("");
		if (lemmas.size() > 0){
			sb.append(lemmas.get(0));

			for (int i = 1; i < lemmas.size(); i++) {
				sb.append(separator+lemmas.get(i));
			}
		}
		return sb.toString();
	}

	private List<String> slice(String sentence) {
		String word ="";
		List<String> words = new ArrayList<String>();

		boolean prevSep = true;

		boolean prevLower = true;

		boolean lastAdded = true;
		for(char c: sentence.toCharArray()){

			if (Character.isLetter(c)) {
				if (Character.isUpperCase(c)){
					if ((prevSep || prevLower) && !word.equals("")){
						words.add(word);
						word=Character.toString(Character.toLowerCase(c));
					} else {
						word+=Character.toLowerCase(c);
					}
					prevSep = false;
					prevLower = false;	    	
				} else {
					prevLower = true;
					word+=c;
				}
				lastAdded = false;
			} else {
				if (!lastAdded){
					lastAdded = true;
					words.add(word);
					word = "";
				}
				prevSep = true;
			}
		}

		if (!lastAdded) words.add(word);

		return words;
	}

}
