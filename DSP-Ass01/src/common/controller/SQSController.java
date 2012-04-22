package common.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import example.SimpleQueueServiceSample;

public class SQSController {

	private final static String APPLICATION_MANAGER_QUEUE = "DSP122-AVI-BATEL-APPLICATION-MANAGER";
	private final static String MANAGER_APPLICATION_QUEUE = "DSP122-AVI-BATEL-MANAGER-APPLICATION";
	private final static String MANAGER_WORKERS_QUEUE = "DSP122-AVI-BATEL-MANAGER-WORKERS";
	private final static String WORKER_MANAGER_QUEUE = "DSP122-AVI-BATEL-WORKER-MANAGER";

	private String mApplicationManagerQueueUrl;
	private String mManagerApplicationQueueUrl;
	private String mManagerWorkersQueueUrl;
	private String mWorkerManagerQueueUrl;

	private AmazonSQS mAmazonSQS;

	private SQSController() {

		mAmazonSQS = null;

		try {

			mAmazonSQS = new AmazonSQSClient(
					new PropertiesCredentials(
							SimpleQueueServiceSample.class
									.getResourceAsStream("../AwsCredentials.properties")));
		}

		catch (IOException e) {
			e.printStackTrace();
		}

		initQueues();
	}

	private static class SQSControllerHolder {
		public static final SQSController instance = new SQSController();
	}

	public static SQSController getInstance() {
		return SQSControllerHolder.instance;
	}

	private void initQueues() {

		boolean amq = false;
		boolean maq = false;
		boolean mwq = false;
		boolean wmq = false;

		for (String queueUrl : mAmazonSQS.listQueues().getQueueUrls()) {

			if (queueUrl.contains(APPLICATION_MANAGER_QUEUE)) {
				amq = true;
				mApplicationManagerQueueUrl = queueUrl;
			} else if (queueUrl.contains(MANAGER_APPLICATION_QUEUE)) {
				maq = true;
				mManagerApplicationQueueUrl = queueUrl;
			} else if (queueUrl.contains(MANAGER_WORKERS_QUEUE)) {
				mwq = true;
				mManagerWorkersQueueUrl = queueUrl;
			} else if (queueUrl.contains(WORKER_MANAGER_QUEUE)) {
				wmq = true;
				mWorkerManagerQueueUrl = queueUrl;
			}
		}

		if (!amq)
			mApplicationManagerQueueUrl = mAmazonSQS.createQueue(
					new CreateQueueRequest(APPLICATION_MANAGER_QUEUE))
					.getQueueUrl();

		if (!maq)
			mManagerApplicationQueueUrl = mAmazonSQS.createQueue(
					new CreateQueueRequest(MANAGER_APPLICATION_QUEUE))
					.getQueueUrl();

		if (!mwq)
			mManagerWorkersQueueUrl = mAmazonSQS.createQueue(
					new CreateQueueRequest(MANAGER_WORKERS_QUEUE))
					.getQueueUrl();

		if (!wmq)
			mWorkerManagerQueueUrl = mAmazonSQS.createQueue(
					new CreateQueueRequest(WORKER_MANAGER_QUEUE)).getQueueUrl();
	}

	public void sendMessageAboutTheLocationOfTheImagesListFile(
			String pApplicationId, String pInputFileLocation,
			int pNumOfURLsPerWorker) {

		// TEST The Application will send a message to a specified SQS queue,
		// stating the location of the images list on S3

		String message = "NEW_TASK\t" + pApplicationId + "\t"
				+ pInputFileLocation + "\t" + pNumOfURLsPerWorker;

		SendMessageRequest sendMessageRequest = new SendMessageRequest(
				mApplicationManagerQueueUrl, message);

		mAmazonSQS.sendMessage(sendMessageRequest);

		// TODO should notify the manager, otherwise we are BUSY WAITING!!..
	}

	public String checkIfTheProcessIsDone(String pApplicationId) {
		// TODO The Application will check a specified SQS queue for a message
		// indicating the process is done and the response is available on S3.

		// TODO return the location of the summary file
		return null;
	}

	public List<Message> receiveMessagesAboutTheLocationOfTheImagesListFile() {
		// TEST The Manager will receive a message from a specified SQS queue,
		// stating the location of the images list on S3

		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(
				mApplicationManagerQueueUrl);

		while (true) {

			List<Message> messages = mAmazonSQS.receiveMessage(
					receiveMessageRequest).getMessages();

			// TODO blocking.. wait when the queue is empty, BUSY WAIT!!
			if (messages.isEmpty()) {

				try {

					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO: handle exception..
				}
			}

			else
				return messages;
		}
	}

	public void sendMessageAboutThisURL(URL pImageUrl) {
		// TEST The Manager creates an SQS message for each URL in the images
		// list.

		String message = "NEW_IMAGE_TASK\t" + pImageUrl;

		SendMessageRequest sendMessageRequest = new SendMessageRequest(
				mManagerWorkersQueueUrl, message);

		mAmazonSQS.sendMessage(sendMessageRequest);
	}

	public void waitForWorkersToFinishTheirWork() {
		// TODO The Manager waits until the images queue count is 0,

	}

	public Vector<Message> receiveFacesMessages() {
		// TODO The Manager should read all the messages from the results queue
		return null;
	}

	public void sendMessageAboutTheLocationOfTheSummaryFile(String pTaskId,
			String pSummaryFileLocation) {
		// TODO The Manager sends a message to the user queue with the location
		// of the file.

	}

	public void deleteMessages(List<Message> pMessages) {
		// TODO The Manager deletes the messages that he handled.

	}

	public URL receiveMessageAboutURL() {
		// TEST The Worker gets an image message from an SQS queue.
		// TEST The Worker removes the image message from the SQS queue.

		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(
				mManagerWorkersQueueUrl);

		receiveMessageRequest.setMaxNumberOfMessages(1);

		List<Message> messages = mAmazonSQS.receiveMessage(
				receiveMessageRequest).getMessages();

		if (messages.isEmpty())
			return null;

		else {

			mAmazonSQS
					.deleteMessage(new DeleteMessageRequest(
							mManagerWorkersQueueUrl, messages.get(0)
									.getReceiptHandle()));

			try {
				return new URL(messages.get(0).getBody().split("\t")[1]);
			}

			catch (MalformedURLException e) {
				// TODO handle exception..
				return null;
			}
		}
	}

	public void sendMessageAboutTheLocationOfTheFaceFile(URL pOriginalImageUrl,
			String pFaceFileLocation) {
		// TEST put a message in an SQS queue indicating the original URL of the
		// image and the S3 url of the new images file.

		String message = "DONE_IMAGE_TASK\t" + pOriginalImageUrl + "\t"
				+ pFaceFileLocation;

		SendMessageRequest sendMessageRequest = new SendMessageRequest(
				mWorkerManagerQueueUrl, message);

		mAmazonSQS.sendMessage(sendMessageRequest);
	}

	public void sendWorkerFinishMessage() {
		// TEST The worker tells the Manager that finished his job..
		
		String message = "WORKER_DONE";

		// TODO: create new queue..
		SendMessageRequest sendMessageRequest = new SendMessageRequest(
				null, message);

		mAmazonSQS.sendMessage(sendMessageRequest);
	}
}
