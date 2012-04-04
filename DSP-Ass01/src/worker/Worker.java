package worker;

public class Worker {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// Worker gets an image message from an SQS queue
		// Worker downloads the image indicated in the message.
		// Worker produces face crops and uploads the cropped images to S3
		// Worker puts a message in an SQS queue indicating the original URL of
		// the image and the S3 urls of the new image files
	}

}
