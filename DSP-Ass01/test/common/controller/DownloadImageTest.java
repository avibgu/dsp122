package common.controller;

import static org.junit.Assert.*;

import java.net.URL;

import org.junit.*;
import junit.framework.TestCase;

import common.utilities.ImageManipulator;

public class DownloadImageTest {
	
	private ImageManipulator imageManipulator = new ImageManipulator();
	
	@Test
	public void ImageManipulatorTest() throws Exception {
		URL url = new URL("http://www.nlm.nih.gov/medlineplus/images/leg.jpg");
		String ans = imageManipulator.downloadImage(url);
		assertTrue(ans.equals("success"));
	}
	
	

	
	
}
