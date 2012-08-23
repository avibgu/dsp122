package step4;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Mapper;

import data.Pattern;
import data.PatternInstance;
import data.Word;

public class Mapper4 extends Mapper<Word, PatternInstance, Pattern, PatternInstance> {

	protected Pattern mPattern;

	@Override
	protected void setup(Context pContext)
			throws IOException, InterruptedException {
		
		mPattern = new Pattern();
	}
	
	
	protected void map(Word hookWord, PatternInstance patternInstance, Context context)
			throws IOException, InterruptedException {
		
		mPattern.set(patternInstance);
		
		context.write(mPattern, patternInstance);
	}
}
