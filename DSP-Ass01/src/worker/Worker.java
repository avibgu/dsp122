package worker;

import java.io.File;
import java.net.URL;
import java.util.Vector;


import common.controller.S3Controller;
import common.controller.SQSController;
import common.utilities.ImageManipulator;

public class Worker {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		S3Controller s3 = S3Controller.getInstance();
		SQSController sqs = SQSController.getInstance();
		
		URL url = null; 
			
		// Worker gets an image message from an SQS queue
		while (null != (url = sqs.receiveMessageAboutURL())){
			
			// TODO: does File is ok?..
			
			// Worker downloads the image indicated in the message.
			File image = ImageManipulator.downloadImage(url);
			
			// Worker produces face crops and uploads the cropped images to S3
			Vector<File> faces = ImageManipulator.cropFacesFromImage(image);
			
			for (File face : faces){
				
				String faceFileLocation = s3.uploadFaceImage(face);
				
				// Worker puts a message in an SQS queue indicating the original URL of
				// the image and the S3 urls of the new image files
				sqs.sendMessageAboutTheLocationOfTheFaceFile(url, faceFileLocation);
			}
		}
		
		sqs.sendWorkerFinishMessage();
	}

}
