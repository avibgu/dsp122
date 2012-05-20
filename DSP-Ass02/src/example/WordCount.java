package example;

import java.io.IOException;

import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.io.LongWritable;

public class WordCount {

	public static class MapClass extends
			Mapper<LongWritable, Text, Text, IntWritable> {

		private final static IntWritable one = new IntWritable(1);

		private Text word = new Text();

		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {

			StringTokenizer itr = new StringTokenizer(value.toString());

			while (itr.hasMoreTokens()) {

				word.set(itr.nextToken());
				context.write(word, one);
			}
		}
	}

	public static class ReduceClass extends
			Reducer<Text, IntWritable, Text, IntWritable> {

		@Override
		public void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {

			int sum = 0;

			for (IntWritable value : values)
				sum += value.get();

			context.write(key, new IntWritable(sum));
		}
	}

	public static class PartitionerClass extends Partitioner<Text, IntWritable> {

		@Override
		public int getPartition(Text key, IntWritable value, int numPartitions) {
			return getLanguage(key) % numPartitions;
		}

		private int getLanguage(Text key) {

			if (key.getLength() > 0) {

				int c = key.charAt(0);

				if (c >= Long.decode("0x05D0").longValue()
						&& c <= Long.decode("0x05EA").longValue())
					return 1;
			}

			return 0;
		}
	}

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();

		// conf.set("mapred.map.tasks","10");
		// conf.set("mapred.reduce.tasks","2");

		Job job = new Job(conf, "word count");

		job.setJarByClass(WordCount.class);
		job.setMapperClass(MapClass.class);
		job.setPartitionerClass(PartitionerClass.class);
		job.setCombinerClass(ReduceClass.class);
		job.setReducerClass(ReduceClass.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(job, new Path(args[1]));
		FileOutputFormat.setOutputPath(job, new Path(args[2]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}