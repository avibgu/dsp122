package common.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import example.S3Sample;

public class S3Controller {

	// TODO
	public static final String BUCKET_NAME = null;

	// TODO
	public static final String IMAGES_LIST_FILE_LOCATION = null;

	// TODO
	public static final String SUMMARY_FILE_LOCATION = null;

	// TODO
	public static final String FACE_FILE_LOCATION = null;

	private AmazonS3 mAmazonS3;

	private S3Controller() {

		mAmazonS3 = null;

		try {

			mAmazonS3 = new AmazonS3Client(
					new PropertiesCredentials(
							S3Sample.class
									.getResourceAsStream("../AwsCredentials.properties")));
		}

		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		initBacket();
	}

	private static class S3ControllerHolder {
		public static final S3Controller instance = new S3Controller();
	}

	public static S3Controller getInstance() {
		return S3ControllerHolder.instance;
	}

	private void initBacket() {

		// checks if we already have a bucket
		for (Bucket bucket : mAmazonS3.listBuckets())
			if (bucket.getName().equals(BUCKET_NAME))
				return;

		// in case we don't - create one
		mAmazonS3.createBucket(BUCKET_NAME);
	}

	public String uploadInputFile(File pInputFile) {
		// The application will upload the file with the list of images to S3.
		// use IMAGES_LIST_FILE_LOCATION as base.

		String fileLocation = IMAGES_LIST_FILE_LOCATION + "-"
				+ pInputFile.getName() + UUID.randomUUID();
		fileLocation = fileLocation.replaceAll("/", "-");

		mAmazonS3.putObject(new PutObjectRequest(BUCKET_NAME, fileLocation,
				pInputFile));

		// return the location
		return fileLocation;
	}

	public File downloadSummaryFile(String pSummaryFileLocation) {
		// TODO The application will download the response from S3.

		// TODO use: SUMMARY_FILE_LOCATION

		S3Object object = mAmazonS3.getObject(new GetObjectRequest(BUCKET_NAME, pSummaryFileLocation));
		
		return null;
	}

	public File downloadInputFile(String pImagesListFileLocation) {
		// TODO The Manager Downloads the images list from S3.
		return null;
	}

	public void createAndUploadSummaryFile(String pSummaryFileLocation) {
		// TODO The Manager should read all the messages from the results queue,
		// create the output file accordingly, upload the output file to S3

	}

	public String uploadFaceImage(Object face) {
		// TODO upload the images file to S3. (return the location)
		return null;
	}
}
