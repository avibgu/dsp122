package step4;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Mapper4 extends Mapper<Text, DoubleWritable, DoubleWritable, Text> {

	protected DoubleWritable outputKey; 
	
	protected void setup(Context context) throws IOException,
			InterruptedException {
		
		outputKey = new DoubleWritable();
	}

	protected void map(Text key, DoubleWritable value, Context context)
			throws IOException, InterruptedException {

		outputKey.set(-value.get());
		
		context.write(outputKey, key);
	}
}
