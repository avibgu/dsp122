package loop6;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class ReducerLoop6 extends Reducer<LongWritable, Text, LongWritable, Text> {

	protected void reduce(LongWritable key, Iterable<Text> values,
			Context context) throws IOException, InterruptedException {
		for (Text value : values)
			context.write(key, value);
	}
	
	@Override
	protected void cleanup(Context pContext)
			throws IOException, InterruptedException {
		pContext.getCounter("BLAGroup", "BLACounter").increment(17);
	}
}
