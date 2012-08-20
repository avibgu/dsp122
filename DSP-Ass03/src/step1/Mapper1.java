package step1;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import data.Word;
import data.WordContext;

public class Mapper1 extends Mapper<LongWritable, Text, Word, WordContext> {

	protected Word mWord;
	protected WordContext mContext;
	protected int mCount;

	protected void setup(Context context) throws IOException,
			InterruptedException {

		mWord = new Word();
		mContext = new WordContext();
		mCount = -1;
	}

	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

		String[] splitted = value.toString().split("\t");

		if (splitted.length != 5)
			return;

		mCount = Integer.parseInt(splitted[2]);

		splitted = splitted[0].split(" ");

		if (splitted.length != 5)
			return;

		mContext.set(cleanWord(splitted[0]), cleanWord(splitted[1]),
				cleanWord(splitted[2]), cleanWord(splitted[3]),
				cleanWord(splitted[4]));
		mContext.setNumOfOccurrences(mCount);

		for (int i = 0; i < 5; i++) {

			mWord.setWord(splitted[i]);
			context.write(mWord, mContext);
		}
	}

	private String cleanWord(String word) {

//		if (word.startsWith("\""))
//			word = word.substring(1);
//
//		if (word.endsWith("\""))
//			word = word.substring(0, word.length() - 1);

		return word;
	}
}
