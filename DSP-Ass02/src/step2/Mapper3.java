package step2;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Mapper3 extends Mapper<LongWritable, Text, Text, Text> {

	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

		String[] splitted = value.toString().split("\t");

		Text word = new Text(splitted[0]);
		String mehane = splitted[1];

		for (int i = 2; i < splitted.length - 1; i += 2)
			context.write(new Text(splitted[i]), new Text(word + "\t"
					+ splitted[i + 1] + "\t" + mehane));
	}
}
