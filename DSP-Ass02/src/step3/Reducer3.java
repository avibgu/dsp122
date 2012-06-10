package step3;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer3 extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {

	private static final int M = 2;

	protected Text w1w2;
	protected DoubleWritable outputValue;

	protected void setup(Context context) throws IOException,
			InterruptedException {

		w1w2 = new Text();
		outputValue = new DoubleWritable();
	}

	protected void reduce(Text key, Iterable<DoubleWritable> values, Context context)
			throws IOException, InterruptedException {

		String[] splitted = key.toString().split("\t");

		w1w2.set(splitted[0] + ", " + splitted[1]);

		double mechane = Double.parseDouble(splitted[2].toString());

		double mone = 0.0;

		int counter = 0;

		for (DoubleWritable value : values) {

			mone += value.get();
			counter++;
		}

		if (counter > M) {

			outputValue.set(mone / mechane);

			if (!Double.isNaN(outputValue.get()))
				context.write(w1w2, outputValue);
		}
	}
}
