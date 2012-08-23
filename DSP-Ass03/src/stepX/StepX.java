package stepX;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import data.Cluster;
import data.Pattern;
import data.PatternInstance;
import data.Word;

public class StepX {

	public static void main(String[] args) throws Exception {
	
		Configuration conf = new Configuration();

	    Job job = new Job(conf, "stepX");
	    
	    job.setJarByClass(StepX.class);
	    job.setMapperClass(MapperX.class);
	    job.setReducerClass(ReducerX.class);
	    
	    job.setMapOutputKeyClass(Pattern.class);
		job.setMapOutputValueClass(PatternInstance.class);
	    job.setOutputKeyClass(Word.class);
	    job.setOutputValueClass(Pattern.class);
	    
	    job.setInputFormatClass(SequenceFileInputFormat.class);
	    job.setOutputFormatClass(SequenceFileOutputFormat.class); 
	    
	    FileInputFormat.addInputPath(job, new Path(args[1]));
	    FileOutputFormat.setOutputPath(job, new Path(args[2]));
	    
	    System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
