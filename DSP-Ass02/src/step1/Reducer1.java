package step1;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer1 extends Reducer<Text, Text, Text, Text> {

	protected Text outputKey;
	protected Text outputValue;

	protected void setup(Context context) throws IOException,
			InterruptedException {

		outputKey = new Text();
		outputValue = new Text();
	}

	protected void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {

		int sum = 0;

		StringBuilder sb = new StringBuilder();

		Map<String, Integer> map = new HashMap<String, Integer>();

		for (Text word : values) {

			String[] splittedWord = word.toString().split("\t");

			Integer count = map.get(splittedWord[0]);

			// System.out.println("word:" + splittedWord[0]);
			// System.out.println("count:" + splittedWord[1]);

			if (null == count)
				count = new Integer(0);

			map.put(splittedWord[0], count + Integer.valueOf(splittedWord[1]));
		}

		for (String word : map.keySet()) {

			int count = map.get(word);
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
