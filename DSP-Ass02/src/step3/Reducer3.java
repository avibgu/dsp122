package step3;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer3 extends Reducer<Text, Text, Text, Text> {

	private static final int M = 10;

	protected void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {

		String[] splitted = key.toString().split("\t");

		Text w1w2 = new Text(splitted[0] + ", " + splitted[1]);
		
		Double mechane = Double.parseDouble(splitted[2].toString());
		
		Double mone = 0.0;
		
		int counter = 0;
		
		for (Text value : values){
			
			mone += Double.parseDouble(value.toString());
			counter++;
		}
			
		if (counter > M)
			context.write(w1w2, new Text(Double.toString(mone/mechane)));
	}
}
