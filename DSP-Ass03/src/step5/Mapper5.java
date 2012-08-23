package step5;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Mapper;

import data.Pattern;
import data.Word;

public class Mapper5 extends Mapper<Word, Pattern, Word, Pattern> {

	protected void map(Word hookWord, Pattern pattern, Context context)
			throws IOException, InterruptedException {
		context.write(hookWord, pattern);
	}
}
