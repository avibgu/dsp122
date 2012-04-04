package common.controller;

import java.io.File;

public class S3Controller {

	private S3Controller() {
	}

	private static class S3ControllerHolder {
		public static final S3Controller instance = new S3Controller();
	}

	public static S3Controller getInstance() {
		return S3ControllerHolder.instance;
	}

	public void uploadInputFile(File inputFile) {
		// TODO The application will upload the file with the list of images to
		// S3.

	}

	public Object getTheResponse() {
		// TODO The application will download the response from S3.
		return null;
	}
}
