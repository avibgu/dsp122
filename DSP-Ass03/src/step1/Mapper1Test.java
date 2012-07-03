package step1;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import data.Word;
import data.WordContext;

public class Mapper1Test extends Mapper<LongWritable, Text, Word, WordContext> {

	protected Word mWord;
	protected WordContext mContext;
	protected IntWritable mCount;
	
	protected void setup(Context context) throws IOException,
			InterruptedException {

		mWord = new Word();
		mContext = new WordContext();
		mCount = new IntWritable();
	}

	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

		String[] splitted = value.toString().split("\t");

		if (splitted.length != 5)
			return;

		mCount.set(Integer.parseInt(splitted[2]));
		
		splitted = splitted[0].split(" ");
				
		if (splitted.length != 5)
			return;

		mContext.set(splitted[0], splitted[1], splitted[2], splitted[3], splitted[4]);
		mContext.setNumOfOccurrences(mCount);

		for (int i = 0; i < 5; i++){
			
			mWord.setWord(splitted[i]);
			context.write(mWord, mContext);
		}
	}
}
