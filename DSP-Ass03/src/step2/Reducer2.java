package step2;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.mapreduce.Reducer;

import data.PatternInstance;
import data.Word;
import data.WordContext;

public class Reducer2 extends Reducer<WordContext, Word, Word, PatternInstance> {

	protected Set<Word> wordsSet;
	protected PatternInstance mPatternInstance;
	protected WordContext mWordContext;
	protected long mTotalCounter;

	@Override
	protected void setup(Context pContext) throws IOException,
			InterruptedException {

		wordsSet = new HashSet<Word>();
		mPatternInstance = new PatternInstance();
		mWordContext = new WordContext();
		mTotalCounter = pContext.getCounter("group", "totalCounter").getValue();

		if (0 == mTotalCounter)
			mTotalCounter = pContext.getConfiguration().getLong("totalCounter",
					5000);
	}

	protected void reduce(WordContext wordContext, Iterable<Word> words,
			Context context) throws IOException, InterruptedException {

		wordsSet.clear();

		mWordContext = wordContext;

		for (Word word : words) {

			try {
				wordsSet.add((Word) word.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}

		mPatternInstance.set(
				getWordAt(0),
				getWordAt(1),
				getWordAt(2),
				getWordAt(3),
				getWordAt(4),
				context.getConfiguration().getInt(
						wordContext.getWordAt(1).getWord()
								+ wordContext.getWordAt(3).getWord(), 1));

		if (mPatternInstance.isLegal()) {

			mPatternInstance.calcPMI(mTotalCounter);
			context.write(mPatternInstance.getHook(), mPatternInstance);
		}
	}

	private Word getWordAt(int pIndex) {

		for (Word word : wordsSet)
			if (word.equals(mWordContext.getWordAt(pIndex)))
				return word;

		return null;
	}
}
