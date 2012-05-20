package step1;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer1 extends Reducer<Text, Text, Text, Text> {

	protected void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {

		for (Text value : values)
			context.write(key, value);
	};
}
