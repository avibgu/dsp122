package step6;

import java.io.File;

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

public class Step6 {

	protected static String inDir;
	protected static String outDir;
	
	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();

	    Job job = new Job(conf, "step6");

	    job.setJarByClass(Step6.class);
	    job.setMapperClass(Mapper6.class);
	    job.setReducerClass(Reducer6.class);

	    job.setMapOutputKeyClass(Cluster.class);
		job.setMapOutputValueClass(Word.class);
	    job.setOutputKeyClass(Word.class);
	    job.setOutputValueClass(Cluster.class);

	    job.setInputFormatClass(SequenceFileInputFormat.class);
	    job.setOutputFormatClass(TextOutputFormat.class);		// TODO: SequenceFileOutputFormat

	    inDir = args[1];
	    outDir = args[2];
	    
	    FileInputFormat.addInputPath(job, new Path(inDir));
	    FileOutputFormat.setOutputPath(job, new Path(outDir));

	    while (conf.getBoolean("ClustersCounter", true)){
		
	    	conf.setBoolean("ClustersCounter", false);
	    
	    	handleInOutDirectories();
	    	
	    	job.waitForCompletion(true);
	    	
	    }

	    System.exit(0);
	}

	private static void handleInOutDirectories() {

		File out = new File(outDir);
		File in = new File(inDir);
		
		in.delete();
		out.renameTo(in);		
		out.delete();
		
	}
}
