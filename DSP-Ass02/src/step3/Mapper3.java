package step3;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Mapper3 extends Mapper<Text, Text, Text, DoubleWritable> {

	protected Text outputKey;
	protected DoubleWritable outputValue;

	protected void setup(Context context) throws IOException,
			InterruptedException {

		outputKey = new Text();
		outputValue = new DoubleWritable();
	}

	protected void map(Text key, Text value, Context context)
			throws IOException, InterruptedException {

		String[] splitted = value.toString().split("\t");

		String w1 = splitted[0];
		String w2 = splitted[1];

		double mehane = Double.parseDouble(splitted[4])
				* Double.parseDouble(splitted[5]);

		double counts = Double.parseDouble(splitted[2])
				* Double.parseDouble(splitted[3]);

		outputKey.set(w1 + "\t" + w2 + "\t" + mehane);
		outputValue.set(counts);

		context.write(outputKey, outputValue);
	}
}
