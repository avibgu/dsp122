package step4;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Mapper4 extends Mapper<LongWritable, Text, Text, Text> {

	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

		String[] splitted = value.toString().split("\t");

		String w1w2 = splitted[0];
		String fitness = "-" + splitted[1];
		
		String mehane = Double.toString(Double.parseDouble(splitted[5]) * Double.parseDouble(splitted[6]));
		String counts = Double.toString(Double.parseDouble(splitted[3]) * Double.parseDouble(splitted[4]));
		
		context.write(new Text(fitness), new Text(w1w2));
	}
}
