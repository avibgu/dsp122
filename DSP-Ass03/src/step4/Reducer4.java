package step4;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import data.Pattern;
import data.PatternInstance;

public class Reducer4 extends Reducer<Pattern, PatternInstance, Text, Pattern> {

	protected Text mTextWord;
	protected Pattern mPattern;

	@Override
	protected void setup(Context pContext) throws IOException,
			InterruptedException {
		mTextWord = new Text();
		mPattern = new Pattern();
	}

	protected void reduce(Pattern pattern,
			Iterable<PatternInstance> PatternInstances, Context context)
			throws IOException, InterruptedException {

		try {
			mPattern = (Pattern) pattern.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		for (PatternInstance patternInstance : PatternInstances)
			mPattern.add(patternInstance.getHook().getWord(), patternInstance
					.getTarget().getWord());
		
		if (mPattern.getHookWords().size() > 1) {

			for (String hook : mPattern.getHookWords()) {

				mTextWord.set(hook);
				context.write(mTextWord, mPattern);
			}
		}
	};
}
