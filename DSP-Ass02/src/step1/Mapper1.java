package step1;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Mapper1 extends Mapper<LongWritable, Text, Text, Text> {

	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

		String[] splitted = value.toString().split("\t");

		Text word = new Text(splitted[2]);
		
		context.write(word, new Text(splitted[0]));
		context.write(word, new Text(splitted[1]));
		context.write(word, new Text(splitted[3]));
		context.write(word, new Text(splitted[4]));
	};
}
