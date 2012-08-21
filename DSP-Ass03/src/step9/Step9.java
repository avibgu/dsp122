package step9;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class Step9 {

	public static void main(String[] args) throws Exception {

		// TODO: run this step once for every relation-type (7 times..), and
		// write the output to 7 different directories

		// TODO: run this step also for the test sets - when the positivity is
		// "?" (check what it should be) - the output in another 7 folders

		Configuration conf = new Configuration();

		try {
			// 0 - train, 1 - test
			conf.setInt("fileIndex", Integer.valueOf(args[3]));
		} 
		catch (Exception e) {}

		Job job = new Job(conf, "step9");

		job.setJarByClass(Step9.class);
		job.setMapperClass(Mapper9.class);
		job.setReducerClass(Reducer9.class);

		job.setMapOutputKeyClass(DoubleWritable.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(DoubleWritable.class);

		job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(args[1]));
		FileOutputFormat.setOutputPath(job, new Path(args[2]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
