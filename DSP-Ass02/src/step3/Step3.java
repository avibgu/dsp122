package step3;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import step2.Mapper2;
import step2.Reducer2;
import step2.Step2;

public class Step3 {

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();

	    Job job = new Job(conf, "step3");
	    
	    job.setJarByClass(Step3.class);
	    job.setMapperClass(Mapper3.class);
	    job.setReducerClass(Reducer3.class);
	    
	    job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);
	    
	    job.setInputFormatClass(SequenceFileInputFormat.class);
//	    job.setOutputFormatClass(SequenceFileOutputFormat.class);	TODO
		job.setOutputFormatClass(TextOutputFormat.class);

	    FileInputFormat.addInputPath(job, new Path(args[1]));
	    FileOutputFormat.setOutputPath(job, new Path(args[2]));
		
	    System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
