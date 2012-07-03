package step1;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import data.Word;

public class Mapper1 extends Mapper<LongWritable, Text, Word, IntWritable> {

	protected Word mWord;
	protected IntWritable mCount;
	
	protected void setup(Context context) throws IOException,
			InterruptedException {

		mWord = new Word();
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

		for (int i = 0; i < 5; i++){
			
			mWord.setWord(splitted[i]);
			context.write(mWord, mCount);
		}
	}
}
