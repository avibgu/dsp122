package step1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import data.Word;
import data.WordContext;

public class Step1 {

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();

		Job job = new Job(conf, "step1");

		job.setJarByClass(Step1.class);
		job.setMapperClass(Mapper1.class);
		job.setReducerClass(Reducer1.class);

		job.setMapOutputKeyClass(Word.class);
		job.setMapOutputValueClass(WordContext.class);
		job.setOutputKeyClass(Word.class);
		job.setOutputValueClass(WordContext.class);

		job.setInputFormatClass(TextInputFormat.class);			//TODO: change it back to SequenceFileInputFormat
		job.setOutputFormatClass(SequenceFileOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(args[1]));
		FileOutputFormat.setOutputPath(job, new Path(args[2]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
