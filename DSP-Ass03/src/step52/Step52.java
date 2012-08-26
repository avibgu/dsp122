package step52;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import step1.Step1;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import data.Cluster;
import data.Global;

public class Step52 {

	protected static String inDir = "";
	protected static String outDir = "";

	public static void main(String[] args) throws Exception {

		inDir = args[1];
		outDir = args[2];

		boolean status = true;

		boolean toStop = false;

		while (!toStop && status) {

			Configuration conf = new Configuration();

			Job job = new Job(conf, "step52");

			job.setJarByClass(Step52.class);
			job.setMapperClass(Mapper52.class);
			job.setReducerClass(Reducer52.class);

			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(Cluster.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Cluster.class);

			job.setInputFormatClass(SequenceFileInputFormat.class);
			job.setOutputFormatClass(SequenceFileOutputFormat.class);

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
				Step1.class.getResourceAsStream("AwsCredentials.properties")));

		for (S3ObjectSummary objectSummary : mAmazonS3.listObjects(
				Global.BUCKET_NAME).getObjectSummaries()) {

			String key = objectSummary.getKey();

			if (key.startsWith(inDir))
				mAmazonS3.deleteObject(new DeleteObjectRequest(
						Global.BUCKET_NAME, key));

			if (key.startsWith(outDir)) {

				mAmazonS3.copyObject(new CopyObjectRequest(Global.BUCKET_NAME,
						key, Global.BUCKET_NAME, key
								.replaceFirst(outDir, inDir)));

				mAmazonS3.deleteObject(new DeleteObjectRequest(
						Global.BUCKET_NAME, key));
			}
		}
	}
}
