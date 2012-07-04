package step3;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Mapper;

import data.Pattern;
import data.Word;

public class Mapper3 extends Mapper<Word, Pattern, Word, Pattern> {

	protected void map(Word word, Pattern pattern, Context context)
			throws IOException, InterruptedException {
		context.write(word, pattern);
	}
}
