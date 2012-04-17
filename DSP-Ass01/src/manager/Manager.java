package manager;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Vector;

import com.amazonaws.services.sqs.model.Message;

import common.controller.EC2Controller;
import common.controller.S3Controller;
import common.controller.SQSController;
import common.utilities.FileManipulator;

public class Manager {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		int numOfURLsPerWorker = -1;

		try {

			numOfURLsPerWorker = Integer.parseInt(args[0]);
		} catch (Exception e) {
			throw new Exception("please provide: numOfURLsPerWorker");
		}

		EC2Controller ec2 = EC2Controller.getInstance();
		S3Controller s3 = S3Controller.getInstance();
		SQSController sqs = SQSController.getInstance();

		while (true) {

			// Manager downloads list of images

			String imagesListFileLocation = sqs
					.receiveMessageAboutTheLocationOfTheImagesListFile();

			InputStream listOfImagesFile = s3
					.downloadInputFile(imagesListFileLocation);

			// Manager creates an SQS message for each URL in the list of images
			Vector<URL> urls = FileManipulator
					.retrieveURLsFromInputFile(listOfImagesFile);

			for (URL url : urls)
				sqs.sendMessageAboutThisURL(url);

			// Manager bootstraps nodes to process messages

			int numOfWorkers = urls.size() / numOfURLsPerWorker;

			numOfWorkers += (0 != urls.size() % numOfURLsPerWorker) ? 1 : 0;

			ec2.startWorkers(numOfWorkers, numOfURLsPerWorker);

			// Manager reads all the Workers' messages from SQS and creates one
			// summary file
			sqs.waitForWorkersToFinishTheirWork();

			ec2.stopWorkers();

			Vector<Message> facesMessages = sqs.receiveFacesMessages();
			
			File summaryFile = FileManipulator.createSummaryFile(facesMessages);
			
			// Manager uploads summary file to S3
			String summaryFileLocation = s3.uploadSummaryFile(summaryFile);

			// Manager posts an SQS message about summary file
			sqs.sendMessageAboutTheLocationOfTheSummaryFile(summaryFileLocation);
		}
	}
}
