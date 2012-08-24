package step7old;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import data.Cluster;
import data.Pattern;

public class Step7old {

	public static void main(String[] args) throws Exception {
	
		Configuration conf = new Configuration();

	    Job job = new Job(conf, "step7");
	    
	    job.setJarByClass(Step7old.class);
	    job.setMapperClass(Mapper7old.class);
	    job.setReducerClass(Reducer7old.class);
	    
	    job.setMapOutputKeyClass(Pattern.class);
		job.setMapOutputValueClass(Cluster.class);
	    job.setOutputKeyClass(Cluster.class);
	    job.setOutputValueClass(Cluster.class);
	    
	    job.setInputFormatClass(SequenceFileInputFormat.class);
	    job.setOutputFormatClass(SequenceFileOutputFormat.class);
	    
	    FileInputFormat.addInputPath(job, new Path(args[1]));
	    FileOutputFormat.setOutputPath(job, new Path(args[2]));
	    
	    System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
