package step2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer2 extends Reducer<Text, Text, Text, Text> {

	private static final double N = 10;

	protected void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {

		List<String[]> list = new ArrayList<String[]>();

		for (Text value : values)
			list.add(value.toString().split("\t"));

		for (int i = 0; i < list.size(); i++)
			
			for (int j = i + 1; j < list.size(); j++)
				
				if((Double.parseDouble(list.get(i)[2]) + Double.parseDouble(list.get(j)[2])) / 2 > N)
					
					context.write(key,
							new Text(list.get(i)[0] + "\t" + list.get(j)[0] + "\t"
									+ list.get(i)[1] + "\t" + list.get(j)[1] + "\t"
									+ list.get(i)[2] + "\t" + list.get(j)[2]));
	}
}
