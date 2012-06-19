package step2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer2 extends Reducer<Text, Text, Text, Text> {

	private static final double N = 4;

	protected List<String[]> list;
	protected Text value;

	protected void setup(Context context) throws IOException,
			InterruptedException {

		list = new ArrayList<String[]>();
		value = new Text();
	}

	protected void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {

		list.clear();

		for (Text value : values) {

			String[] tmp = value.toString().split("\t");

			if (Double.parseDouble(tmp[1]) > N)
				list.add(tmp);
		}

		for (int i = 0; i < list.size(); i++) {

			for (int j = i + 1; j < list.size(); j++) {

				// if((Double.parseDouble(list.get(i)[2]) +
				// Double.parseDouble(list.get(j)[2])) / 2 > N)

				value.set(list.get(i)[0] + "\t" + list.get(j)[0] + "\t"
						+ list.get(i)[1] + "\t" + list.get(j)[1] + "\t"
						+ list.get(i)[2] + "\t" + list.get(j)[2]);

				// System.out.println(value.toString());

				context.write(key, value);
			}
		}
	}
}
