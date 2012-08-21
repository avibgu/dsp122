package step3;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import org.apache.hadoop.mapreduce.Reducer;

import data.Global;
import data.Pattern;
import data.Word;

public class Reducer3 extends Reducer<Word, Pattern, Word, Pattern> {


	protected Set<Pattern> sortedPatterns;

	protected void setup(Context context) throws IOException,
			InterruptedException {

		sortedPatterns = new TreeSet<Pattern>();
	}

	protected void reduce(Word hookWord, Iterable<Pattern> patterns, Context context)
			throws IOException, InterruptedException {

		sortedPatterns.clear();
		
		for (Pattern pattern : patterns){
			
			try {
				sortedPatterns.add((Pattern)pattern.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}	
		}

		int filterIndex = sortedPatterns.size() * Global.L / 100;
		
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
