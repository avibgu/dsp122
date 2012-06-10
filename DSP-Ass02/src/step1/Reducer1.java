package step1;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer1 extends Reducer<Text, Text, Text, Text> {

	protected Map<String, Integer> map;
	
	protected Text outputKey;
	protected Text outputValue;

	protected void setup(Context context) throws IOException,
			InterruptedException {

		map = new HashMap<String, Integer>();

		outputKey = new Text();
		outputValue = new Text();
	}

	protected void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {

		int sum = 0;

		StringBuilder sb = new StringBuilder();

		map.clear();

		for (Text value : values) {

			String[] splittedWord = value.toString().split("\t");

			String word = splittedWord[0];
			
			Integer count = map.get(word);

			// System.out.println("word:" + splittedWord[0]);
			// System.out.println("count:" + splittedWord[1]);

			if (null == count)
				count = 0;

			int occurrences = 1;
			
			try {
				
				occurrences = Integer.valueOf(splittedWord[1]);
				occurrences = (0 == occurrences) ? 1 : occurrences;
			}
			catch (Exception e) {}
			
			map.put(word, count + occurrences);
		}

		for (String word : map.keySet()) {

			Integer count = map.get(word);
			
			if (null == count)
				continue;
				
			sb.append(word + "\t" + count + "\t");
			sum += count * count;
		}

		sb.deleteCharAt(sb.length() - 1);
		
		outputKey.set(key.toString() + "\t" + Math.sqrt(sum));

		outputValue.set(sb.toString());

		context.write(outputKey, outputValue);

		// System.out.println("key:" + outputKey);
		// System.out.println("value:" + outputValue);
	}
}
