package step1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.mapreduce.Reducer;

import data.Word;
import data.WordContext;
import data.WordType;

public class Reducer1 extends Reducer<Word, WordContext, Word, WordContext> {

	protected static final int FH = 15;
	protected static final int FB = 5;
	protected static final int FC = 10;
	
	private static final long N = 100;
	
	protected List<WordContext> wordContextsList;

	protected void setup(Context context) throws IOException,
			InterruptedException {
		
		wordContextsList = new ArrayList<WordContext>();
	}

	protected void reduce(Word word, Iterable<WordContext> wordContexts,
			Context context) throws IOException, InterruptedException {

		wordContextsList.clear();
		
		long counter = context.getCounter("group", "hooksCounter").getValue();
		                
		int sum = 0;

		for (WordContext wordContext : wordContexts){
			
			sum += wordContext.getNumOfOccurrences();
			wordContextsList.add(wordContext);
		}

		word.setCount(sum);
		
		// we count every word
		context.getCounter("group", "totalCounter").increment(sum);

		if (sum > FH)
			word.setType(WordType.HFW);

		else if (counter < N && sum < FC && sum > FB) {
			context.getCounter("group", "counter").increment(1);
			word.setType(WordType.HOOK);
		}
		
		else if (sum < FC)
			word.setType(WordType.CW);
		
		else
			return;
		
		// we write just HFW, CW or Hook words
		for (WordContext wordContext : wordContextsList)
			context.write(word, wordContext);
	}
}
