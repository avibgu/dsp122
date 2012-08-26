package loop6;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import step1.Step1;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import data.Global;

public class Loop6 {

	protected static String inDir;
	protected static String outDir;

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();

		while (conf.getBoolean("ClustersCounter", true)) {

			// conf.setBoolean("ClustersCounter", false);

			Job job = new Job(conf, "loop6");

			job.setJarByClass(Loop6.class);
			job.setMapperClass(MapperLoop6.class);
			job.setReducerClass(ReducerLoop6.class);

			job.setMapOutputKeyClass(LongWritable.class);
			job.setMapOutputValueClass(Text.class);
			job.setOutputKeyClass(LongWritable.class);
			job.setOutputValueClass(Text.class);

			job.setInputFormatClass(TextInputFormat.class);
			job.setOutputFormatClass(TextOutputFormat.class);

			inDir = args[1];
			outDir = args[2];

			FileInputFormat.addInputPath(job, new Path(inDir));
			FileOutputFormat.setOutputPath(job, new Path(outDir));

			job.waitForCompletion(true);
			
			System.out.println("\n\n"
					+ job.getCounters().getGroup("BLAGroup")
							.findCounter("BLACounter").getValue() + "\n\n");

			// handleLocalInOutDirectories();
		}

		System.exit(0);
	}

	private static void handleLocalInOutDirectories() {
		new File(inDir).delete();
		new File(outDir).renameTo(new File(inDir));
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
