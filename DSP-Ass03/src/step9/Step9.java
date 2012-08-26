package step9;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import data.Cluster;
import data.WordsPair;

public class Step9 {

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();

		try {
			// 0 - train, 1 - test
			conf.setInt("fileIndex", Integer.valueOf(args[3]));
		} 
		catch (Exception e) {}

		Job job = new Job(conf, "step9");

		conf.set("mapred.map.child.java.opts", "-Xmx5120m");
		conf.set("mapred.reduce.child.java.opts", "-Xmx5120m");
		
		job.setJarByClass(Step9.class);
		job.setMapperClass(Mapper9.class);
		job.setReducerClass(Reducer9.class);

		job.setMapOutputKeyClass(WordsPair.class);
		job.setMapOutputValueClass(Cluster.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(args[1]));
		FileOutputFormat.setOutputPath(job, new Path(args[2]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
