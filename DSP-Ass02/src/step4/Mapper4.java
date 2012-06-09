package step4;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Mapper4 extends Mapper<Text, Text, DoubleWritable, Text> {

	protected void map(Text key, Text value, Context context)
			throws IOException, InterruptedException {

		context.write(new DoubleWritable(-Double.parseDouble(value.toString())),
				key);
	}
}
