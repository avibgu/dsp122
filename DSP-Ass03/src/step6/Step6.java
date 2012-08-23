package step6;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
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
		job.setOutputFormatClass(TextOutputFormat.class); // TODO: SequenceFileOutputFormat

		inDir = args[1];
		outDir = args[2];

		FileInputFormat.addInputPath(job, new Path(inDir));
		FileOutputFormat.setOutputPath(job, new Path(outDir));

		while (conf.getBoolean("ClustersCounter", true)) {

			conf.setBoolean("ClustersCounter", false);

			handleInOutDirectories();

			job.waitForCompletion(true);
		}

		System.exit(0);
	}

	public static void handleInOutDirectories() throws FileNotFoundException,
			IllegalArgumentException, IOException {

		AmazonS3 mAmazonS3 = new AmazonS3Client(new PropertiesCredentials(
				Step6.class.getResourceAsStream("AwsCredentials.properties")));

		for (S3ObjectSummary objectSummary : mAmazonS3.listObjects(
				Global.BUCKET_NAME).getObjectSummaries()) {

			String key = objectSummary.getKey();

			if (key.startsWith("output5"))
				mAmazonS3.deleteObject(new DeleteObjectRequest(
						Global.BUCKET_NAME, key));

			if (key.startsWith("output6")) {

				mAmazonS3.copyObject(new CopyObjectRequest(Global.BUCKET_NAME,
						key, Global.BUCKET_NAME, key.replaceFirst("6", "5")));

				mAmazonS3.deleteObject(new DeleteObjectRequest(
						Global.BUCKET_NAME, key));
			}
		}

		/*
		 * File out = new File(outDir); File in = new File(inDir);
		 * 
		 * in.delete(); out.renameTo(in); out.delete();
		 */
	}
}
