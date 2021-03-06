package step4;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer4 extends
		Reducer<DoubleWritable, Text, Text, DoubleWritable> {

	protected int K;
	protected int counter;

	protected DoubleWritable outputValue;
			
	protected void setup(Context context) throws IOException,
			InterruptedException {
		
		K = context.getConfiguration().getInt("K", 30);
		counter = 0;
		
		outputValue = new DoubleWritable();
	}

	protected void reduce(DoubleWritable key, Iterable<Text> values,
			Context context) throws IOException, InterruptedException {

		for (Text value : values) {

			if (counter < K) {

				counter++;
				
				outputValue.set(-key.get());
				
				context.write(value, outputValue);
			}
		}
	}
}
