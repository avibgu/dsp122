package step5;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import data.Cluster;
import data.Word;

public class Step5 {

	public static void main(String[] args) throws Exception {
	
		Configuration conf = new Configuration();

		// TODO: why?..
//		conf.set("mapred.reduce.tasks","1");

	    Job job = new Job(conf, "step5");
	    
	    job.setJarByClass(Step5.class);
	    job.setMapperClass(Mapper5.class);
	    job.setReducerClass(Reducer5.class);
	    
	    job.setMapOutputKeyClass(Cluster.class);
		job.setMapOutputValueClass(Word.class);
	    job.setOutputKeyClass(Word.class);
	    job.setOutputValueClass(Cluster.class);
	    
	    job.setInputFormatClass(SequenceFileInputFormat.class);
	    job.setOutputFormatClass(TextOutputFormat.class);	// TODO: SequenceFileOutputFormat
	    
	    FileInputFormat.addInputPath(job, new Path(args[1]));
	    FileOutputFormat.setOutputPath(job, new Path(args[2]));
	    
	    System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
