package step4;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Reducer;

import data.Pattern;
import data.PatternInstance;
import data.Word;

public class Reducer4 extends Reducer<Pattern, PatternInstance, Word, Pattern> {

	protected void reduce(Pattern pattern,
			Iterable<PatternInstance> PatternInstances, Context context)
			throws IOException, InterruptedException {

		for (PatternInstance patternInstance : PatternInstances) {

			try {
				pattern.add((Word) patternInstance.getHook().clone(),
						(Word) patternInstance.getTarget().clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}

		if (pattern.getHookWords().size() > 1)
			for (Word hook : pattern.getHookWords())
				context.write(hook, pattern);
	};
}
