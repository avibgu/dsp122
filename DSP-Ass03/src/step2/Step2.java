package step2;

import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

import data.Global;
import data.PatternInstance;
import data.Word;
import data.WordContext;

public class Step2 {

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();

		AmazonSQS mAmazonSQS = new AmazonSQSClient(new PropertiesCredentials(
				Step2.class.getResourceAsStream("AwsCredentials.properties")));

		String queueUrl = mAmazonSQS.createQueue(
				new CreateQueueRequest(Global.QUEUE_NAME)).getQueueUrl();

		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(
				queueUrl);

		receiveMessageRequest.setMaxNumberOfMessages(1);

		List<Message> messages = mAmazonSQS.receiveMessage(
				receiveMessageRequest).getMessages();

		conf.setLong("totalCounter", Long.valueOf(messages.get(0).getBody()));

		mAmazonSQS.deleteMessage(new DeleteMessageRequest(queueUrl, messages
				.get(0).getReceiptHandle()));

		Job job = new Job(conf, "step2");

		job.setJarByClass(Step2.class);
		job.setMapperClass(Mapper2.class);
		job.setReducerClass(Reducer2.class);

		job.setMapOutputKeyClass(WordContext.class);
		job.setMapOutputValueClass(Word.class);
		job.setOutputKeyClass(Word.class);
		job.setOutputValueClass(PatternInstance.class);

		job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(args[1]));
		FileOutputFormat.setOutputPath(job, new Path(args[2]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
