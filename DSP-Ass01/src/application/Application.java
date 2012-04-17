package application;

import java.io.File;
import java.io.InputStream;


import common.controller.EC2Controller;
import common.controller.S3Controller;
import common.controller.SQSController;
import common.utilities.FileManipulator;

public class Application {

	public static void main(String[] args) throws Exception {

		File inputFile = null;
		File outputFile = null;

		int numOfURLsPerWorker = -1;

		try {
			
			inputFile = new File(args[0]);
			outputFile = new File(args[1]);
			numOfURLsPerWorker = Integer.parseInt(args[2]);
		}
		catch (Exception e) {
			throw new Exception(
					"please provide: inputFileName, outputFileName, numOfURLsPerWorker");
		}

		EC2Controller ec2 = EC2Controller.getInstance();
		S3Controller s3 = S3Controller.getInstance();
		SQSController sqs = SQSController.getInstance();

		// Local Application uploads the file with the list of images to S3
		String inputFileLocation = s3.uploadInputFile(inputFile);

		// Local Application sends a message (queue) stating of the location of
		// the images list on S3
		sqs.sendMessageAboutTheLocationOfTheImagesListFile(inputFileLocation);

		// Local Application starts the manager
		ec2.startTheManager(numOfURLsPerWorker);

		// .....

		// Local Application reads SQS message
		String summaryFileLocation = sqs.checkIfTheProcessIsDone();

		// Local Application downloads summary file from S3
		InputStream summaryFileInputStream = s3.downloadSummaryFile(summaryFileLocation);

		// Local Application creates html output files
		FileManipulator.convertSummaryFileToOutputFile(summaryFileInputStream, outputFile);
	}
}
