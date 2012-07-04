package step3;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import org.apache.hadoop.mapreduce.Reducer;

import data.Pattern;
import data.Word;

public class Reducer3 extends Reducer<Word, Pattern, Word, Pattern> {

	private static final int L = 10;

	protected Set<Pattern> sortedPatterns;

	protected void setup(Context context) throws IOException,
			InterruptedException {

		sortedPatterns = new TreeSet<Pattern>();
	}

	protected void reduce(Word hookWord, Iterable<Pattern> patterns, Context context)
			throws IOException, InterruptedException {

		sortedPatterns.clear();
		
		for (Pattern pattern : patterns)
			sortedPatterns.add(pattern);

		int filterIndex = sortedPatterns.size() * L / 100;
		
		int i = -1;
		
		for (Pattern pattern : sortedPatterns){
			
			i++;
			
			if (i < filterIndex)
				continue;
			
			else if (i > sortedPatterns.size() - filterIndex)
				break;
			
			context.write(hookWord, pattern);
		}
	}
}
