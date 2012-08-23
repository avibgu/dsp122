package step1;

import java.io.File;
import java.io.PrintWriter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import data.Global;
import data.Word;
import data.WordContext;

public class Step1 {

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();

		Job job = new Job(conf, "step1");

		job.setJarByClass(Step1.class);
		job.setMapperClass(Mapper1.class);
		job.setReducerClass(Reducer1.class);

		job.setMapOutputKeyClass(Word.class);
		job.setMapOutputValueClass(WordContext.class);
		job.setOutputKeyClass(Word.class);
		job.setOutputValueClass(WordContext.class);

		job.setInputFormatClass(TextInputFormat.class); // TODO: SequenceFileInputFormat
		job.setOutputFormatClass(SequenceFileOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(args[1]));
		FileOutputFormat.setOutputPath(job, new Path(args[2]));

		boolean status = job.waitForCompletion(true);

		long total = conf.getLong("totalCounter", 0);

		AmazonS3 mAmazonS3 = new AmazonS3Client(new PropertiesCredentials(
				Step1.class.getResourceAsStream("AwsCredentials.properties")));

		File counterFile = new File("totalCounter");
		PrintWriter pw = new PrintWriter(counterFile);
		pw.write(String.valueOf(total));
		pw.close();

		mAmazonS3.putObject(Global.BUCKET_NAME, "totalCounter", counterFile);

		System.exit(status ? 0 : 1);
	}
}
