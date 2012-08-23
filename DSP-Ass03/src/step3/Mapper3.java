package step3;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Mapper;

import data.PatternInstance;
import data.Word;

public class Mapper3 extends Mapper<Word, PatternInstance, Word, PatternInstance> {

	protected void map(Word hookWord, PatternInstance pattern, Context context)
			throws IOException, InterruptedException {
		context.write(hookWord, pattern);
	}
}
