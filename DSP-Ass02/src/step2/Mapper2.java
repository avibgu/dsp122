package step2;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Mapper2 extends Mapper<Text, Text, Text, Text> {

	protected Text word;
	protected String mehane;

	protected Text outputKey;
	protected Text outputValue;

	protected void setup(Context context) throws IOException,
			InterruptedException {

		word = new Text();
		mehane = "";

		outputKey = new Text();
		outputValue = new Text();
	}

	protected void map(Text key, Text value, Context context)
			throws IOException, InterruptedException {

		String[] splitted = key.toString().split("\t");

		word.set(splitted[0]);
		mehane = splitted[1];

		// System.out.println("word:" + splitted[0]);
		// System.out.println("mehane:" + splitted[1]);

		splitted = value.toString().split("\t");

		for (int i = 0; i < splitted.length - 1; i += 2) {

			outputKey.set(splitted[i]);
			outputValue.set(word + "\t" + splitted[i + 1] + "\t" + mehane);

			// System.out.println("w:" + splitted[i]);
			// System.out.println("c:" + splitted[i+1]);

			context.write(outputKey, outputValue);
		}
	}
}
