package common.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;

import com.amazonaws.auth.AWSCredentials;
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

import example.AwsConsoleApp;

public class EC2Controller {

	private static final String KEY_PAIR = "AviKeyPair";
	private static final String MANAGER_TAG = "MANAGER";

	private AmazonEC2 mAmazonEC2;
	private ArrayList<String> mWorkers;

	private EC2Controller() {

		mAmazonEC2 = null;

		try {

			AWSCredentials credentials = new PropertiesCredentials(
					AwsConsoleApp.class
							.getResourceAsStream("../AwsCredentials.properties"));

			mAmazonEC2 = new AmazonEC2Client(credentials);
		}

		catch (IOException e) {
			e.printStackTrace();
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
		// TEST Checks if a Manager node is active on the EC2 cloud. If it is
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
			tags.add(new Tag(MANAGER_TAG,""));
			
			mAmazonEC2.createTags(new CreateTagsRequest(ids,tags));
		}

		else if (!instance.getState().getName()
				.equals(InstanceStateName.Running.toString())){
			//TODO
			System.err.println("MANGER INSTACE EXISTS BUT NOT RUNNING");
		}
		
		else{
			
			//TODO
			System.err.println("MANGER INSTACE EXISTS AND RUNNING");
		}
	}

	public void startWorkers(int pNumOfWorkers, int pNumOfURLsPerWorker) {
		// TEST The manager should create a worker for every n messages.
		// Note that while the manager creates a node for every n messages, it
		// does not delegate messages to specific nodes. All of the worker nodes
		// take their messages from the same SQS queue, so it might be the case
		// that with 2n messages and hence two worker nodes, one node processed
		// n+(n/2) messages, while the other processed only n/2.

		RunInstancesRequest request = prepareWorkerRequest(pNumOfWorkers,
				pNumOfURLsPerWorker);

		RunInstancesResult runInstancesResult = mAmazonEC2
				.runInstances(request);

		for (Instance tInstance : runInstancesResult.getReservation()
				.getInstances())
			mWorkers.add(tInstance.getInstanceId());
	}

	public void stopWorkers() {
		// TEST The manager should turn off all the workers when there is no
		// more work to be done (0 messages).

		mAmazonEC2.terminateInstances(new TerminateInstancesRequest(mWorkers));
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
			if (instance.getTags().contains(new Tag(MANAGER_TAG,"")))
				return instance;

		return null;
	}

	protected RunInstancesRequest prepareManagerRequest(int pNumOfURLsPerWorker) {
		return prepareRequest(1, getManagerScript(pNumOfURLsPerWorker));
	}

	protected RunInstancesRequest prepareWorkerRequest(int pNumOfWorkers,
			int pNumOfURLsPerWorker) {
		return prepareRequest(pNumOfWorkers,
				getWorkerScript(pNumOfURLsPerWorker));
	}

	private String getManagerScript(int pNumOfURLsPerWorker) {

		ArrayList<String> lines = new ArrayList<String>();

		// TODO: make the script

		lines.add("#! /bin/bash");
		lines.add("apt-get install wget");
		// lines.add("# TODO: get manager.jar from s3, using wget");
		// lines.add("# TODO: make sure we have java installed");
		// lines.add("java -jar manager.jar " + pNumOfURLsPerWorker);
		// lines.add("shutdown -h 0");

		lines.add("wget http://www.cs.bgu.ac.il/~dsp122/Main -O dsp.html"); // TODO:
																			// remove

		return new String(Base64.encodeBase64(join(lines, "\n").getBytes()));
	}

	private String getWorkerScript(int pNumOfURLs) {

		ArrayList<String> lines = new ArrayList<String>();

		// TODO: make the script

		lines.add("#! /bin/bash");
		lines.add("apt-get install wget");
		// lines.add("# TODO: get worker.jar from s3, using wget");
		// lines.add("# TODO: make sure we have java installed");
		// lines.add("java -jar worker.jar " + pNumOfURLs);
		// lines.add("shutdown -h 0");

		lines.add("wget http://www.cs.bgu.ac.il/~dsp122/Main -O dsp.html"); // TODO:
																			// remove

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
