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
		
		String mehane = Double.toString(Double.parseDouble(splitted[5]) * Double.parseDouble(splitted[6]));
		String counts = Double.toString(Double.parseDouble(splitted[3]) * Double.parseDouble(splitted[4]));
		
		context.write(new Text(w1 + "\t" + w2 + "\t" + mehane), new Text(counts));
	}
}
