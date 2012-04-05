package common.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.codec.binary.Base64;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.RunInstancesRequest;

public class EC2Controller {

	private EC2Controller() {
	}

	private static class EC2ControllerHolder {
		public static final EC2Controller instance = new EC2Controller();
	}

	public static EC2Controller getInstance() {
		return EC2ControllerHolder.instance;
	}

	public void startTheManager(int numOfURLsPerWorker) {
		// TODO Checks if a Manager node is active on the EC2 cloud. If it is
		// not, the application will start the manager node.

	}

	public void startWorkers(int pNumOfWorkers) {
		// TODO The manager should create a worker for every n messages.
		// Note that while the manager creates a node for every n messages, it
		// does not delegate messages to specific nodes. All of the worker nodes
		// take their messages from the same SQS queue, so it might be the case
		// that with 2n messages and hence two worker nodes, one node processed
		// n+(n/2) messages, while the other processed only n/2.
	}

	public void stopWorkers() {
		// TODO The manager should turn off all the workers when there is no
		// more work to be done (0 messages).

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
