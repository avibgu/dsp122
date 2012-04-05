package common.controller;

import java.io.File;

public class S3Controller {

	//TODO
	public static final String IMAGES_LIST_FILE_LOCATION = null;
	
	//TODO
	public static final String SUMMARY_FILE_LOCATION = null;
	
	//TODO
	public static final String FACE_FILE_LOCATION = null;

	private S3Controller() {
	}

	private static class S3ControllerHolder {
		public static final S3Controller instance = new S3Controller();
	}

	public static S3Controller getInstance() {
		return S3ControllerHolder.instance;
	}

	public void uploadInputFile(File pInputFile) {
		// TODO The application will upload the file with the list of images to
		// S3.
		
		// TODO use: IMAGES_LIST_FILE_LOCATION

	}

	public File downloadSummaryFile() {
		// TODO The application will download the response from S3.
		
		// TODO use: SUMMARY_FILE_LOCATION
		
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
