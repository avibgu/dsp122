package step4;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer4 extends Reducer<Text, Text, Text, Text> {

	private static final int K = 10;
	private static int counter = 0;

	protected void reduce(org.w3c.dom.Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		
		String fitness = key.toString().substring(1);
		
		for(Text value : values)
			if (counter < K){
				increaseCounter();
				context.write(new Text(fitness), value);
			}
		
	}
	
	private void increaseCounter(){
		counter++;
	}
}
