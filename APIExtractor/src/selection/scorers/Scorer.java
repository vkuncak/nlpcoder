package selection.scorers;

import selection.parser.one.Word;

public abstract class Scorer {

	public abstract double getScore(Word key);

	public abstract void clear();
	
	public abstract String toString(int contextIndex);
	
}
