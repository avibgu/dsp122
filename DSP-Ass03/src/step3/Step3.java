package step3;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import data.Pattern;
import data.Word;

public class Step3 {

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();

	    Job job = new Job(conf, "step3");
	    
	    job.setJarByClass(Step3.class);
	    job.setMapperClass(Mapper3.class);
	    job.setReducerClass(Reducer3.class);
	    
	    job.setMapOutputKeyClass(Word.class);
		job.setMapOutputValueClass(Pattern.class);
	    job.setOutputKeyClass(Word.class);
	    job.setOutputValueClass(Pattern.class);
	    
	    job.setInputFormatClass(SequenceFileInputFormat.class);
	    job.setOutputFormatClass(SequenceFileOutputFormat.class); 

	    FileInputFormat.addInputPath(job, new Path(args[1]));
	    FileOutputFormat.setOutputPath(job, new Path(args[2]));
		
	    System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
