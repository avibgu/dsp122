package step1;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer1 extends Reducer<Text, Text, Text, Text> {

	protected void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {

		int sum = 0; 
		
		StringBuilder sb = new StringBuilder();
	
		Map<Text, Integer> map = new HashMap<Text, Integer>();
		
		for (Text word : values){
			
			Integer count = map.get(word);

			if (null == count)
				count = new Integer(0);
			
			map.put(word, count + 1);
		}

		Object[] s = map.keySet().toArray();
		
		for (Object word : s){
		
			int count = map.get((Text)word);
			sb.append(((Text)word).toString() + "," + count + " ");
			sum += count * count;
		}
		
		context.write(new Text(key.toString() + "," + Math.sqrt(sum)), new Text(sb.toString()));
	};
}
