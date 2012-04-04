package manager;

public class Manager {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// Manager downloads list of images
		// Manager creates an SQS message for each URL in the list of images
		// Manager bootstraps nodes to process messages

		// Manager reads all the Workers' messages from SQS and creates one
		// summary file
		// Manager uploads summary file to S3
		// Manager posts an SQS message about summary file
	}

}
