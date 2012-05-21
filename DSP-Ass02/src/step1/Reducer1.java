package step1;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer1 extends Reducer<Text, Text, Text, Text> {

	protected void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {

		StringBuilder sb = new StringBuilder("=> ");
		
		Set<String> set = new HashSet<String>();
		
		for (Text value : values)
			set.add(value.toString());
		
		for (String w : set)
			sb.append(w + " ");
		
		context.write(key, new Text(sb.toString()));
	};
}
