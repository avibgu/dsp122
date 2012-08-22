package step1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.hadoop.mapreduce.Reducer;

import data.Global;
import data.Word;
import data.WordContext;
import data.WordType;

public class Reducer1 extends Reducer<Word, WordContext, Word, WordContext> {

	protected List<WordContext> wordContextsList;

	protected ConcurrentMap<Word, AtomicInteger> mTargetsMap;

	protected void setup(Context context) throws IOException,
			InterruptedException {

		wordContextsList = new ArrayList<WordContext>();
		mTargetsMap = new ConcurrentHashMap<Word, AtomicInteger>();
	}

	protected void reduce(Word word, Iterable<WordContext> wordContexts,
			Context context) throws IOException, InterruptedException {
		
		wordContextsList.clear();
		mTargetsMap.clear();

		long counter = context.getCounter("group", "hooksCounter").getValue();

		int sum = 0;

		for (WordContext wordContext : wordContexts) {

			sum += wordContext.getNumOfOccurrences();
			
			try {
				wordContextsList.add((WordContext)wordContext.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}

		word.setCount(sum);

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
							.getWordAt(3)).get());

				if (wordContext.getWordAt(3).equals(word))
					wordContext.setHookTargetCount(mTargetsMap.get(wordContext
							.getWordAt(1)).get());
			}
		}

		// we write just HFW, CW or Hook words
		for (WordContext wordContext : wordContextsList)
			context.write(word, wordContext);
	}

	private void incTargetCounter(Word target) {

		mTargetsMap.putIfAbsent(target, new AtomicInteger(0));
		mTargetsMap.get(target).incrementAndGet();
	}
}
