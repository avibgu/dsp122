package common.controller;

import java.io.File;

import org.junit.Test;

public class S3ControllerTest {

	S3Controller mS3Controller = S3Controller.getInstance();
	
	@Test
	public void uploadFile() throws Exception {
		mS3Controller.uploadFileToS3(new File("input.txt"), "", ".jpg");
	}
	
	@Test
	public void downloadFile() throws Exception{
		String file = mS3Controller.uploadFileToS3(new File("input.txt"), "", ".jpg");;
		mS3Controller.downloadFileFromS3(file);
	}
}
