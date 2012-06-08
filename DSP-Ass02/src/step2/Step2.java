package step2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

public class Step2 {

	public static void main(String[] args) throws Exception {
	
		Configuration conf = new Configuration();

	    Job job = new Job(conf, "step2");
	    
	    job.setJarByClass(Step2.class);
	    job.setMapperClass(Mapper2.class);
	    job.setReducerClass(Reducer2.class);
	    
	    job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(Text.class);
	    
	    job.setInputFormatClass(SequenceFileInputFormat.class);
	    job.setOutputFormatClass(SequenceFileOutputFormat.class);
	    
	    FileInputFormat.addInputPath(job, new Path(args[1]));
	    FileOutputFormat.setOutputPath(job, new Path(args[2]));
	    
	    System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
