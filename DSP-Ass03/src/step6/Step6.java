package step6;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

public class Step6 {

	public static void main(String[] args) throws Exception {
	
		Configuration conf = new Configuration();

	    Job job = new Job(conf, "step6");
	    
	    job.setJarByClass(Step6.class);
	    job.setMapperClass(Mapper6.class);
	    job.setReducerClass(Reducer6.class);
	    
	    job.setMapOutputKeyClass(DoubleWritable.class);
		job.setMapOutputValueClass(Text.class);
	    job.setOutputKeyClass(Text.class);
	    job.setOutputValueClass(DoubleWritable.class);
	    
	    job.setInputFormatClass(SequenceFileInputFormat.class);
	    job.setOutputFormatClass(SequenceFileOutputFormat.class);
	    
	    FileInputFormat.addInputPath(job, new Path(args[1]));
	    FileOutputFormat.setOutputPath(job, new Path(args[2]));
	    
	    while (true){
	    	job.waitForCompletion(true);
	    	//TODO: handle in and out directories..
	    }
	    		
//	    System.exit(0);
	}
}
