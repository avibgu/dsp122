package step2;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.mapreduce.Reducer;

import data.Pattern;
import data.Word;
import data.WordContext;

public class Reducer2Test extends Reducer<WordContext, Word, Word, Pattern> {

	protected Set<Word> wordsSet;
	protected Pattern mPattern;
	protected WordContext mWordContext;

	public Reducer2Test() {
		wordsSet = new HashSet<Word>();
		mPattern = new Pattern();
		mWordContext = new WordContext();
	}

	protected void reduce(WordContext wordContext, Iterable<Word> words,
			Context context) throws IOException, InterruptedException {

		mWordContext = wordContext;

		for (Word word : words)
			wordsSet.add(word);

		mPattern.set(getWordAt(0), getWordAt(1), getWordAt(2), getWordAt(3), getWordAt(4));

		if (mPattern.isLegal())
			;
	}

	private Word getWordAt(int pIndex) {

		for (Word word : wordsSet)
			if (word.equals(mWordContext.getWordAt(pIndex)))
				return word;

		return null;
	}
}
