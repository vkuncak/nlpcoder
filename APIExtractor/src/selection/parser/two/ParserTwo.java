package selection.parser.two;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.Synset;

import selection.IParser;
import selection.ISentence;
import selection.WordProcessor;
import selection.parser.one.ConstituentOne;
import selection.parser.one.Word;
import selection.parser.one.WordNet;
import selection.parser.one.trees.SentenceOne;

public class ParserTwo extends IParser {

	private WordNet wordnet;	
	private int maxLevelDepth;
	private int intervalRadius;

	public ParserTwo(WordNet wordnet, int maxLevelDepth, int intervalRadius) {
		assert maxLevelDepth > 0;
		this.wordnet = wordnet;
		this.maxLevelDepth = maxLevelDepth;
		this.intervalRadius = intervalRadius;
	}

	public ISentence parse(SentenceOne curr) {
		Word[] words = curr.getWords();
		ConstituentOne[] constituents = curr.getConstituents();
		ConstituentTwo[] coustituents2 = getConstituents(constituents, words);
		return new SentenceTwo(coustituents2, words);
	}

	private ConstituentTwo[] getConstituents(ConstituentOne[] constituents, Word[] words) {
		ConstituentTwo[] constituents2 = new ConstituentTwo[constituents.length];

		for (int i = 0; i < constituents.length; i++) {
			constituents2[i] = getConstituent(constituents[i], words);
		}

		return constituents2;
	}

	private ConstituentTwo getConstituent(ConstituentOne cons, Word[] words) {
		int index = cons.getIndex();
		int firstImportantIndex = cons.smallestIndex();
		int lastImportantIndex = cons.largestIndex();
		int leftIndex = getSmallestIndex(firstImportantIndex);
		int rightIndex = getLargestIndex(lastImportantIndex, words.length);

		Word[] words2 = Arrays.copyOfRange(words, leftIndex, rightIndex+1);

		Wordset[] wordsets = new Wordset[words2.length];
		for (int i = 0; i < words2.length; i++) {
			Word word2 = words2[i];
			int wordIndex = word2.getIndex();
			wordsets[i] = getWordset(word2, index, wordIndex);
		}

		return new ConstituentTwo(wordsets, index, firstImportantIndex - leftIndex, lastImportantIndex - leftIndex);
	}

	private List<Word> prepareWords(LinkedList<Word> words, int constituentIndex, int wordIndex, Set<String> visited) {
		List<Word> words2 = new LinkedList<Word>();
		for (Word word : words) {
			prepareWord(constituentIndex, wordIndex, visited, words2, word);
		}
		return words2;
	}

	private void prepareWord(int constituentIndex, int wordIndex,
			Set<String> visited, List<Word> words2, Word word) {
		String name = word.getLemma();
		if (!name.contains("_") && !visited.contains(name)){
			visited.add(name);
			word.setConstIndex(constituentIndex);
			word.setIndex(wordIndex);
			words2.add(word);
		}
	}

	private Wordset getWordset(Word word, int constituentIndex, int wordIndex) {
		Set<String> visited = new HashSet<String>();
		Level[] levels = new Level[maxLevelDepth];

		List<ISynset> synsets = getSynonyms(word);
		levels[0] = new Level(levelZero(word, constituentIndex, wordIndex, visited, synsets), 0);

		for (int i = 1; i < maxLevelDepth; i++) {
			synsets = getNeighbours(synsets);
			levels[i] = new Level(getWords(synsets, constituentIndex, wordIndex, visited), i);
		}

		return new Wordset(levels);
	}

	private List<Word> levelZero(Word word, int constituentIndex, int wordIndex, Set<String> visited, List<ISynset> synsets) {
		List<Word> zeroLevelWords = new LinkedList<Word>();
		try {
			prepareWord(constituentIndex, wordIndex, visited, zeroLevelWords, word.clone());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		zeroLevelWords.addAll(getWords(synsets, constituentIndex, wordIndex, visited));
		return zeroLevelWords;
	}

	private List<ISynset> getNeighbours(List<ISynset> synsets) {
		// TODO Auto-generated method stub
		return wordnet.getNeighbors(synsets);
	}

	private List<Word> getWords(List<ISynset> synsets, int constituentIndex, int wordIndex, Set<String> visited) {
		LinkedList<Word> words = new LinkedList<Word>();
		for (ISynset synset : synsets) {
			words.addAll(getWords(synset));
		}

		return prepareWords(words, constituentIndex, wordIndex, visited);
	}

	private List<Word> getWords(ISynset synset) {
		return wordnet.getWords(synset);
	}

	private List<ISynset> getSynonyms(Word word) {
		return wordnet.getSynonyms(word);
	}

	private int getLargestIndex(int largestIndex, int sentenceLength) {
		return Math.min(largestIndex + intervalRadius, sentenceLength-1);
	}

	private int getSmallestIndex(int smallestIndex) {
		return Math.max(smallestIndex - intervalRadius, 0);
	}

}
