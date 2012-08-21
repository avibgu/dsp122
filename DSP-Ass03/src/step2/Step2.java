package step2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import data.Pattern;
import data.Word;
import data.WordContext;

public class Step2 {

	public static void main(String[] args) throws Exception {
	
		Configuration conf = new Configuration();

	    Job job = new Job(conf, "step2");
	    
	    job.setJarByClass(Step2.class);
	    job.setMapperClass(Mapper2.class);
	    job.setReducerClass(Reducer2.class);
	    
	    job.setMapOutputKeyClass(WordContext.class);
		job.setMapOutputValueClass(Word.class);
	    job.setOutputKeyClass(Word.class);
	    job.setOutputValueClass(Pattern.class);
	    
	    job.setInputFormatClass(SequenceFileInputFormat.class);			//TODO: change it back to SequenceFileInputFormat
		job.setOutputFormatClass(TextOutputFormat.class);		//TODO: change it back to SequenceFileOutputFormat

	    FileInputFormat.addInputPath(job, new Path(args[1]));
	    FileOutputFormat.setOutputPath(job, new Path(args[2]));
	    
	    System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
