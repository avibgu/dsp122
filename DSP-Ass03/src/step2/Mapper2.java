package step2;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Mapper;

import data.Word;
import data.WordContext;

public class Mapper2 extends Mapper<Word, WordContext, WordContext, Word> {

	@Override
	protected void map(Word word, WordContext wordContext, Context context)
			throws IOException, InterruptedException {
		context.write(wordContext, word);
	}
}
