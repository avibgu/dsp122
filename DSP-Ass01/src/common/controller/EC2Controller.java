package common.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceStateName;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;

public class EC2Controller {

	private static final String KEY_PAIR = "AviKeyPair";
	private static final String MANAGER_TAG = "MANAGER";
	private static final String WORKER_TAG = "WORKER";

	private AmazonEC2 mAmazonEC2;
	private ArrayList<String> mWorkers;

	private EC2Controller() {

		mAmazonEC2 = null;

		try {

			mAmazonEC2 = new AmazonEC2Client(new PropertiesCredentials(
					EC2Controller.class
							.getResourceAsStream("AwsCredentials.properties")));
		}

		catch (IOException e) {

			System.err.println("EC2Controller wan't created..");
			return;
		}

		mWorkers = new ArrayList<String>();
	}

	private static class EC2ControllerHolder {
		public static final EC2Controller instance = new EC2Controller();
	}

	public static EC2Controller getInstance() {
		return EC2ControllerHolder.instance;
	}

	public void startTheManager(int numOfURLsPerWorker) {
		// Checks if a Manager node is active on the EC2 cloud. If it is
		// not, the application will start the manager node.

		Instance instance = getManagerInstance();

		if (null == instance) {

			RunInstancesRequest request = prepareManagerRequest(numOfURLsPerWorker);

			RunInstancesResult runInstancesResult = mAmazonEC2
					.runInstances(request);

			ArrayList<String> ids = new ArrayList<String>();

			for (Instance tInstance : runInstancesResult.getReservation()
					.getInstances())
				ids.add(tInstance.getInstanceId());

			ArrayList<Tag> tags = new ArrayList<Tag>();
			tags.add(new Tag(MANAGER_TAG, ""));

			mAmazonEC2.createTags(new CreateTagsRequest(ids, tags));
		}

		else {

			System.out
					.println("MANAGER INSTACE IS RUNNING - we shouldn't do anything..");
		}
	}

	public void startWorkers(int pNumOfWorkers) {
		// The manager should create a worker for every n messages.
		// Note that while the manager creates a node for every n messages, it
		// does not delegate messages to specific nodes. All of the worker nodes
		// take their messages from the same SQS queue, so it might be the case
		// that with 2n messages and hence two worker nodes, one node processed
		// n+(n/2) messages, while the other processed only n/2.

		RunInstancesRequest request = prepareWorkerRequest(pNumOfWorkers);

		RunInstancesResult runInstancesResult = mAmazonEC2
				.runInstances(request);

		ArrayList<Tag> tags = new ArrayList<Tag>();
		tags.add(new Tag(WORKER_TAG, ""));

		for (Instance tInstance : runInstancesResult.getReservation()
				.getInstances())
			mWorkers.add(tInstance.getInstanceId());

		mAmazonEC2.createTags(new CreateTagsRequest(mWorkers, tags));
	}

	public void stopWorkers() {
		// The manager should turn off all the workers when there is no
		// more work to be done (0 messages).

		mAmazonEC2.terminateInstances(new TerminateInstancesRequest(mWorkers));

		mWorkers.clear();
	}

	protected Instance getManagerInstance() {

		DescribeInstancesResult describeInstancesRequest = mAmazonEC2
				.describeInstances();

		List<Reservation> reservations = describeInstancesRequest
				.getReservations();

		Set<Instance> instances = new HashSet<Instance>();

		for (Reservation reservation : reservations)
			instances.addAll(reservation.getInstances());

		for (Instance instance : instances)
			if (instance.getTags().contains(new Tag(MANAGER_TAG, ""))
					&& instance.getState().getName()
							.equals(InstanceStateName.Running.toString()))
				return instance;

		return null;
	}

	protected RunInstancesRequest prepareManagerRequest(int pNumOfURLsPerWorker) {
		return prepareRequest(1, getManagerScript());
	}

	protected RunInstancesRequest prepareWorkerRequest(int pNumOfWorkers) {
		return prepareRequest(pNumOfWorkers, getWorkerScript());
	}

	private String getManagerScript() {

		ArrayList<String> lines = new ArrayList<String>();

		lines.add("#! /bin/bash");
		lines.add("apt-get install wget");
		lines.add("wget https://s3.amazonaws.com/dsp122-avi-batel/manager.jar");
		lines.add("java -jar manager.jar");
		lines.add("shutdown -h 0");

		return new String(Base64.encodeBase64(join(lines, "\n").getBytes()));
	}

	private String getWorkerScript() {

		ArrayList<String> lines = new ArrayList<String>();

		lines.add("#! /bin/bash");
		lines.add("apt-get install wget");
		lines.add("wget https://s3.amazonaws.com/dsp122-avi-batel/worker.jar");
		lines.add("java -jar worker.jar");
		lines.add("shutdown -h 0");

		return new String(Base64.encodeBase64(join(lines, "\n").getBytes()));
	}

	protected String join(Collection<String> s, String delimiter) {

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

	protected RunInstancesRequest prepareRequest(int numOfInstances,
			String script) {

		RunInstancesRequest request = new RunInstancesRequest();

		request.setInstanceType(InstanceType.T1Micro.toString());
		request.setMinCount(numOfInstances);
		request.setMaxCount(numOfInstances);
		request.setImageId("ami-31814f58");
		request.setKeyName(KEY_PAIR);
		request.setUserData(script);

		return request;
	}
}
