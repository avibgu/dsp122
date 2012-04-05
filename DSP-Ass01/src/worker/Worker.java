package worker;

import java.net.URL;
import java.util.Vector;

import utilities.ImageManipulator;

import common.controller.S3Controller;
import common.controller.SQSController;

public class Worker {

	//TODO
	private static final String FACE_FILE_LOCATION = null;
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		int numOfURLs = -1;

		try {

			numOfURLs = Integer.parseInt(args[0]);
		}
		catch (Exception e) {
			throw new Exception(
					"please provide: numOfURLs");
		}

		S3Controller s3 = S3Controller.getInstance();
		SQSController sqs = SQSController.getInstance();
		
		while (numOfURLs-- > 0){
		
			// Worker gets an image message from an SQS queue
			URL url = sqs.receiveMessageAboutURL();
			
			// Worker downloads the image indicated in the message.
			Object image = ImageManipulator.downloadImage(url);
			
			// Worker produces face crops and uploads the cropped images to S3
			Vector<Object> faces = ImageManipulator.cropFacesFromImage(image);
			
			for (Object face : faces)
				s3.uploadFaceImage(face, FACE_FILE_LOCATION);
			
			// Worker puts a message in an SQS queue indicating the original URL of
			// the image and the S3 urls of the new image files
			sqs.sendMessageAboutTheLocationOfTheFaceFile();
		}
	}

}
