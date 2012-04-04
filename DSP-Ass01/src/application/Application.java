package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.codec.binary.Base64;

import utilities.HTMLBuilder;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import common.controller.EC2Controller;
import common.controller.S3Controller;
import common.controller.SQSController;

public class Application {

	public static void main(String[] args) throws Exception {

		File inputFile = null;
		File outputFile = null;

		int numOfURLsPerWorker = -1;

		try {

			if (3 != args.length)
				throw new Exception();

			inputFile = new File(args[0]);
			outputFile = new File(args[1]);
			numOfURLsPerWorker = Integer.parseInt(args[2]);
		}
		catch (Exception e) {
			throw new Exception(
					"please provide: inputFileName, outputFileName, n");
		}

		EC2Controller ec2 = EC2Controller.getInstance();
		S3Controller s3 = S3Controller.getInstance();
		SQSController sqs = SQSController.getInstance();
		
		// Local Application uploads the file with the list of images to S3
		s3.uploadInputFile(inputFile);
		
		// Local Application sends a message (queue) stating of the location of
		// 		 the images list on S3
		sqs.sendMessageAboutTheLocationOfTheImagesListFile();
		
		// Local Application starts the manager
		ec2.startTheManager(numOfURLsPerWorker);
		
		// .....
		
		// Local Application reads SQS message
		sqs.checkIfTheProcessIsDone();
		
		// Local Application downloads summary file from S3
		Object response = s3.getTheResponse();
		
		// Local Application creates html output files
		HTMLBuilder.createHTMLFile(response, outputFile);
	}

	public class AWSTest {

		public void test(String[] args) {

			AWSCredentials credentials = new BasicAWSCredentials("access-key",
					"secret-access-key");
			AmazonEC2Client ec2 = new AmazonEC2Client(credentials);
			RunInstancesRequest request = new RunInstancesRequest();

			request.setInstanceType(InstanceType.M1Small.toString());
			request.setMinCount(1);
			request.setMaxCount(1);
			request.setImageId("ami-84db39ed");
			request.setKeyName("linux-keypair");
			request.setUserData(getUserDataScript());

			ec2.runInstances(request);
		}

		private String getUserDataScript() {

			ArrayList<String> lines = new ArrayList<String>();

			lines.add("#! /bin/bash");
			lines.add("curl http://www.google.com > google.html");
			lines.add("shutdown -h 0");

			return new String(Base64.encodeBase64(join(lines, "\n").getBytes()));
		}

		private String join(Collection<String> s, String delimiter) {

			StringBuilder builder = new StringBuilder();
			Iterator<String> iter = s.iterator();

			while (iter.hasNext()) {

				builder.append(iter.next());

				if (!iter.hasNext())
					break;

				builder.append(delimiter);
			}

			return builder.toString();
		}

	}
}
