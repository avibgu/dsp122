package application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.codec.binary.Base64;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.RunInstancesRequest;

public class Application {

	public static void main(String[] args) {

		// Local Application uploads the file with the list of images to S3
		// Local Application sends a message (queue) stating of the location of
		// the images list on S3
		// Local Application starts the manager

		// Local Application reads SQS message
		// Local Application downloads summary file from S3
		// Local Application creates html output files
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
