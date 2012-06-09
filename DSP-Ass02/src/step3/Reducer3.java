package step3;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer3 extends Reducer<Text, Text, Text, Text> {

	private static final int M = 2;

	protected Text w1w2;
	protected Text outputValue;

	protected void setup(Context context) throws IOException,
			InterruptedException {

		w1w2 = new Text();
		outputValue = new Text();
	}

	protected void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {

		String[] splitted = key.toString().split("\t");

		w1w2.set(splitted[0] + ", " + splitted[1]);

		Double mechane = Double.parseDouble(splitted[2].toString());

		Double mone = 0.0;

		int counter = 0;

		for (Text value : values) {

			mone += Double.parseDouble(value.toString());
			counter++;
		}

		if (counter > M) {

			outputValue.set(Double.toString(mone / mechane));

			context.write(w1w2, outputValue);
		}
	}
}
