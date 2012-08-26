package step6;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import data.Cluster;
import data.Global;

public class Step6 {

	protected static String inDir = "output52";
	protected static String outDir = "output56";

	public static void main(String[] args) throws Exception {

		inDir = args[1];
		outDir = args[2];
		
		boolean status = true;

		boolean toStop = false;

		while (!toStop && status) {

			Configuration conf = new Configuration();

			Job job = new Job(conf, "step6");

			// need only one reducer - the clusters should come in order,
			// sorted by their num of patterns,
			// so the minimal cluster would be chosen first
			conf.set("mapred.reduce.tasks", "1");

			conf.set("mapred.map.child.java.opts", "-Xmx5120m");
			conf.set("mapred.reduce.child.java.opts", "-Xmx5120m");
			
			job.setJarByClass(Step6.class);
			job.setMapperClass(Mapper6.class);
			job.setReducerClass(Reducer6.class);

			job.setMapOutputKeyClass(Cluster.class);
			job.setMapOutputValueClass(Text.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Cluster.class);

			job.setInputFormatClass(SequenceFileInputFormat.class);
			job.setOutputFormatClass(TextOutputFormat.class); // TODO:
																// SequenceFileOutputFormat
			FileInputFormat.addInputPath(job, new Path(inDir));
			FileOutputFormat.setOutputPath(job, new Path(outDir));

			status = job.waitForCompletion(true);

			toStop = (0 == job.getCounters().getGroup("Counters")
					.findCounter("toStop").getValue());

			handleInOutDirectories();
		}

		System.exit(status ? 0 : 1);
	}

	public static void handleInOutDirectories() throws FileNotFoundException,
			IllegalArgumentException, IOException {

		AmazonS3 mAmazonS3 = new AmazonS3Client(new PropertiesCredentials(
				Step6.class.getResourceAsStream("AwsCredentials.properties")));

		for (S3ObjectSummary objectSummary : mAmazonS3.listObjects(
				Global.BUCKET_NAME, inDir).getObjectSummaries())
			mAmazonS3.deleteObject(new DeleteObjectRequest(Global.BUCKET_NAME,
					objectSummary.getKey()));

		for (S3ObjectSummary objectSummary : mAmazonS3.listObjects(
				Global.BUCKET_NAME, outDir).getObjectSummaries()) {

			String key = objectSummary.getKey();

			mAmazonS3.copyObject(new CopyObjectRequest(Global.BUCKET_NAME, key,
					Global.BUCKET_NAME, key.replaceFirst(outDir, inDir)));

			mAmazonS3.deleteObject(new DeleteObjectRequest(Global.BUCKET_NAME,
					key));
		}

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		/*
		 * File out = new File(outDir); File in = new File(inDir);
		 * 
		 * in.delete(); out.renameTo(in); out.delete();
		 */
	}
}
