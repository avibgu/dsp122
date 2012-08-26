package step4;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import data.Pattern;
import data.PatternInstance;

public class Reducer4 extends Reducer<Pattern, PatternInstance, Text, Pattern> {

	protected Text mTextWord;

	@Override
	protected void setup(Context pContext) throws IOException,
			InterruptedException {
		mTextWord = new Text();
	}

	protected void reduce(Pattern pattern,
			Iterable<PatternInstance> PatternInstances, Context context)
			throws IOException, InterruptedException {

		for (PatternInstance patternInstance : PatternInstances)
			pattern.add(patternInstance.getHook().toString(), patternInstance
					.getTarget().toString());

		if (pattern.getHookWords().size() > 1) {

			for (String hook : pattern.getHookWords()) {

				mTextWord.set(hook);
				context.write(mTextWord, pattern);
			}
		}
	};
}
