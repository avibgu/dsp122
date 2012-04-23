package common.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class S3Controller {

	public static final String BUCKET_NAME = "dsp122-avi-batel";
	public static final String IMAGES_LIST_FILE_BASE_NAME = "IMAGES-LIST-FILE";
	public static final String SUMMARY_FILE_BASE_NAME = "SUMMARY-FILE";
	public static final String FACE_FILE_BASE_NAME = "FACE-FILE";
	private static final String S3_URL = "https://s3.amazonaws.com/"
			+ BUCKET_NAME + "/";

	private AmazonS3 mAmazonS3;

	private S3Controller() {

		mAmazonS3 = null;

		try {

			mAmazonS3 = new AmazonS3Client(new PropertiesCredentials(new File(
					"AwsCredentials.properties")));
		}

		catch (IOException e) {
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
		return uploadFileToS3(pInputFile, IMAGES_LIST_FILE_BASE_NAME, ".txt");
	}

	public String uploadFileToS3(File pInputFile, String pBaseName, String pFileExtension) {

		String fileLocation = pBaseName + "-" + UUID.randomUUID() + pFileExtension;

		fileLocation = fileLocation.replaceAll("/", "-");

		try {

			PutObjectRequest putObjectRequest = new PutObjectRequest(
					BUCKET_NAME, fileLocation, pInputFile);

			putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);

			mAmazonS3.putObject(putObjectRequest);

		} catch (Exception e) {
			// TODO: handle exception, change the name of the file and try again
		}

		return fileLocation;
	}

	public InputStream downloadSummaryFile(String pSummaryFileLocation) {
		// The application will download the response from S3.
		return downloadFileFromS3(pSummaryFileLocation);
	}

	public InputStream downloadInputFile(String pImagesListFileLocation) {
		// The Manager Downloads the images list from S3.
		return downloadFileFromS3(pImagesListFileLocation);
	}

	public InputStream downloadFileFromS3(String pFileLocation) {

		S3Object object = mAmazonS3.getObject(new GetObjectRequest(BUCKET_NAME,
				pFileLocation));

		return object.getObjectContent();
	}

	public String uploadSummaryFile(File pSummaryFile) {
		// upload the output file to S3, and return the location
		return uploadFileToS3(pSummaryFile, SUMMARY_FILE_BASE_NAME, ".txt");
	}

	public String uploadFaceImage(File pFace) {
		// upload the images file to S3. (return the location)
		return S3_URL + uploadFileToS3(pFace, FACE_FILE_BASE_NAME, ".jpg");
	}
}
