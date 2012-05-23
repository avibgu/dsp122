package step2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer2 extends Reducer<Text, Text, Text, Text> {

	protected void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {

		int sum = 0; 
		
		StringBuilder sb = new StringBuilder();
	
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		for (Text word : values){
			
			Integer count = map.get(word.toString());

			if (null == count)
				count = new Integer(0);
			
			map.put(word.toString(), count + 1);
		}

		for (String word : map.keySet()){
		
			int count = map.get(word);
			sb.append(word + "\t" + count + "\t");
			sum += count * count;
		}
		
		context.write(new Text(key.toString() + "\t" + Math.sqrt(sum)), new Text(sb.toString()));
	};
}
