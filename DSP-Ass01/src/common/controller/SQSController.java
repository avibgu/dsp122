package common.controller;

import java.net.URL;

public class SQSController {

	private SQSController() {
	}

	private static class SQSControllerHolder {
		public static final SQSController instance = new SQSController();
	}

	public static SQSController getInstance() {
		return SQSControllerHolder.instance;
	}

	public void sendMessageAboutTheLocationOfTheImagesListFile() {
		// TODO The application will send a message to a specified SQS queue,
		// stating the location of the images list on S3

	}

	public void checkIfTheProcessIsDone() {
		// TODO The application will check a specified SQS queue for a message
		// indicating the process is done and the response is available on S3.

	}

	public void sendMessageAboutThisURL(URL url) {
		// TODO The Manager creates an SQS message for each URL in the images
		// list.

	}

	public void waitForWorkersToFinishTheirWork() {
		// TODO Auto-generated method stub

	}

	public void sendMessageAboutTheLocationOfTheSummaryFile() {
		// TODO The Manager send a message to the user queue with the location
		// of the file.

	}
}
