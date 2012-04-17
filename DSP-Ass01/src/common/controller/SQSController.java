package common.controller;

import java.net.URL;

public class SQSController {

	// TODO
	private final static String APPLICATION_MANAGER_QUEUE = null;

	// TODO
	private final static String MANAGER_APPLICATION_QUEUE = null; 
	
	// TODO
	private final static String MANAGER_WORKERS_QUEUE = null;
	
	// TODO
	private final static String WORKER_MANAGER_QUEUE = null;
	
	private SQSController() {
	}

	private static class SQSControllerHolder {
		public static final SQSController instance = new SQSController();
	}

	public static SQSController getInstance() {
		return SQSControllerHolder.instance;
	}

	public void sendMessageAboutTheLocationOfTheImagesListFile(String pInputFileLocation) {
		// TODO The Application will send a message to a specified SQS queue,
		// stating the location of the images list on S3

		// TODO should notify the manager
	}

	public String checkIfTheProcessIsDone() {
		// TODO The Application will check a specified SQS queue for a message
		// indicating the process is done and the response is available on S3.

		// TODO return the location of the summary file
		return null;
	}

	public String receiveMessageAboutTheLocationOfTheImagesListFile() {
		// TODO The Manager will receive a message from a specified SQS queue,
		// stating the location of the images list on S3
		
		// TODO blocking.. wait when the queue is empty
		
		return null;
	}

	public void sendMessageAboutThisURL(URL url) {
		// TODO The Manager creates an SQS message for each URL in the images
		// list.

	}

	public void waitForWorkersToFinishTheirWork() {
		// TODO The Manager waits until the images queue count is 0,

	}

	public void sendMessageAboutTheLocationOfTheSummaryFile() {
		// TODO The Manager sends a message to the user queue with the location
		// of the file.

	}

	public URL receiveMessageAboutURL() {
		// TODO The Worker gets an image message from an SQS queue.
		// TODO The Worker removes the image message from the SQS queue.
		return null;
	}

	public void sendMessageAboutTheLocationOfTheFaceFile(URL url,
			String faceFileLocation) {
		// TODO put a message in an SQS queue indicating the original URL of the
		// image and the S3 url of the new images file.

	}
}
