package step2;

import java.io.IOException;
import java.util.Vector;

import org.apache.hadoop.mapreduce.Reducer;

import data.Pattern;
import data.Word;
import data.WordContext;

public class Reducer2 extends Reducer<WordContext, Word, Word, Pattern> {

	protected Vector<Word> wordsSet;
	protected Pattern mPattern;
	protected WordContext mWordContext;

	public Reducer2() {
		wordsSet = new Vector<Word>();
		mPattern = new Pattern();
		mWordContext = new WordContext();
	}

	protected void reduce(WordContext wordContext, Iterable<Word> words,
			Context context) throws IOException, InterruptedException {

		mWordContext = wordContext;

		for (Word word : words){
			
			// TODO: DEBUG
			System.out.print(word.getWord() + "\t");
			System.out.flush();
			
			if (!wordsSet.contains(word))
				wordsSet.add(word);
		}
			

		// TODO: DEBUG
		System.out.println("\nwords:");
		System.out.flush();
		for (Word word : wordsSet){
			System.out.println(word);
			System.out.flush();
		}
		
		// TODO: DEBUG
		System.out.println("mWordContext:\n" + mWordContext);
		System.out.flush();
		
		mPattern.set(getWordAt(0), getWordAt(1), getWordAt(2), getWordAt(3),
				getWordAt(4), mWordContext.getHookTargetCount());

		// TODO: DEBUG
		System.out.println("mPattern:\n" + mPattern);
		System.out.flush();
					
		if (mPattern.isLegal()){
			
			mPattern.calcPMI(context.getCounter("group", "totalCounter").getValue());
			context.write(mPattern.getHook(), mPattern);
		}
	}

	private Word getWordAt(int pIndex) {

		for (Word word : wordsSet)
			if (word.equals(mWordContext.getWordAt(pIndex)))
				return word;

		return null;
	}
}
