package step1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.mapreduce.Reducer;

import data.Global;
import data.Word;
import data.WordContext;
import data.WordType;

public class Reducer1 extends Reducer<Word, WordContext, Word, WordContext> {

	protected List<WordContext> wordContextsList;

	protected Map<Word, Integer> mTargetsMap;

	protected void setup(Context context) throws IOException,
			InterruptedException {

		// TODO: DEBUG
		System.out.println("REDUCER-1-setup");
		System.out.flush();
		
		wordContextsList = new ArrayList<WordContext>();
		mTargetsMap = new HashMap<Word, Integer>();
	}

	protected void reduce(Word word, Iterable<WordContext> wordContexts,
			Context context) throws IOException, InterruptedException {

		// TODO: Debug
		System.out.println("REDUCER-1-1: " + word);
		System.out.flush();
		
		wordContextsList.clear();

		long counter = context.getCounter("group", "hooksCounter").getValue();

		int sum = 0;

		for (WordContext wordContext : wordContexts) {

			sum += wordContext.getNumOfOccurrences();
			wordContextsList.add(wordContext);
		}

		word.setCount(sum);
		
		// TODO: Debug
		System.out.println("REDUCER-1-2: " + word);
		System.out.flush();
		
		// we count every word
		context.getCounter("group", "totalCounter").increment(sum);

		if (sum > Global.FH)
			word.setType(WordType.HFW);

		else if (counter < Global.N && sum < Global.FC && sum > Global.FB) {
			context.getCounter("group", "counter").increment(1);
			word.setType(WordType.HOOK);
		}

		else if (sum < Global.FC)
			word.setType(WordType.CW);

		else
			return;

		// for each Hook word, write in the context - how many times
		// the target word appeared with me..
		if (word.getType() == WordType.HOOK) {

			for (WordContext wordContext : wordContextsList) {

				if (wordContext.getWordAt(1).equals(word))
					incTargetCounter(wordContext.getWordAt(3));

				if (wordContext.getWordAt(3).equals(word))
					incTargetCounter(wordContext.getWordAt(1));
			}

			for (WordContext wordContext : wordContextsList) {

				if (wordContext.getWordAt(1).equals(word))
					wordContext.setHookTargetCount(mTargetsMap.get(wordContext
							.getWordAt(3)));

				if (wordContext.getWordAt(3).equals(word))
					wordContext.setHookTargetCount(mTargetsMap.get(wordContext
							.getWordAt(1)));
			}
		}

		// we write just HFW, CW or Hook words
		for (WordContext wordContext : wordContextsList)
			context.write(word, wordContext);
		
		// TODO: Debug
		System.out.println("REDUCER-1-3: " + word);
		System.out.flush();
	}

	private void incTargetCounter(Word target) {

		Integer count = mTargetsMap.get(target);

		if (null == count)
			count = 0;

		mTargetsMap.put(target, count + 1);
	}
}
