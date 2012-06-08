package test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class TestStep {

	public static void main(String[] args) throws Exception {
	
		Configuration conf = new Configuration();
		
		// TODO
//		conf.set("mapred.map.tasks","20");
//		conf.set("mapred.max.split.size","6000000");
		
//		Job job = new Job(conf);
		
	    Job job = new Job(conf, "step1");
	    
	    job.setJarByClass(TestStep.class);
	    job.setMapperClass(TestMapper.class);
	    job.setReducerClass(TestReducer.class);
	    
	    job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);
	    
	    job.setInputFormatClass(SequenceFileInputFormat.class);
	    job.setOutputFormatClass(TextOutputFormat.class);
	    
	    FileInputFormat.addInputPath(job, new Path(args[1]));
	    FileOutputFormat.setOutputPath(job, new Path(args[2]));
	    
	    System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
