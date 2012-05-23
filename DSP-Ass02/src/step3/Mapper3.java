package step3;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Mapper3 extends Mapper<LongWritable, Text, Text, Text> {

	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

		String[] splitted = value.toString().split("\t");

		String w1 = splitted[1];
		String w2 = splitted[2];
		
		String mehane = Integer.toString(Integer.parseInt(splitted[5]) * Integer.parseInt(splitted[6]));
		String counts = Integer.toString(Integer.parseInt(splitted[3]) * Integer.parseInt(splitted[4]));
		
		context.write(new Text(w1 + "\t" + w2 + "\t" + mehane), new Text(counts));
	}
}
