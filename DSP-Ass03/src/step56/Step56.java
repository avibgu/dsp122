package step56;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import data.Cluster;
import data.Word;

public class Step56 {

	public static void main(String[] args) throws Exception {
	
		Configuration conf = new Configuration();

	    Job job = new Job(conf, "step56");
	    
	    job.setJarByClass(Step56.class);
	    job.setMapperClass(Mapper56.class);
	    job.setReducerClass(Reducer56.class);
	    
	    job.setMapOutputKeyClass(Word.class);
		job.setMapOutputValueClass(Cluster.class);
	    job.setOutputKeyClass(Word.class);
	    job.setOutputValueClass(Cluster.class);
	    
	    job.setInputFormatClass(SequenceFileInputFormat.class);
	    job.setOutputFormatClass(SequenceFileOutputFormat.class);
	    
	    FileInputFormat.addInputPath(job, new Path(args[1]));
	    FileOutputFormat.setOutputPath(job, new Path(args[2]));
	    
	    System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
