package step4;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer4 extends
		Reducer<DoubleWritable, Text, Text, DoubleWritable> {

	private static final int K = 10;
	private static int counter = 0;

	protected void reduce(DoubleWritable key, Iterable<Text> values,
			Context context) throws IOException, InterruptedException {

		for (Text value : values) {

			if (counter < K) {

				counter++;
				context.write(value, new DoubleWritable(-key.get()));
			}
		}
	}
}
