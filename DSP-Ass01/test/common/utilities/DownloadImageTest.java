package common.utilities;

import java.io.File;
import java.net.URL;

import org.junit.*;

import common.utilities.ImageManipulator;

public class DownloadImageTest {
	
	private ImageManipulator imageManipulator = new ImageManipulator();
	
	@Test
	public void ImageManipulatorTest() throws Exception {
		URL url = new URL("http://www.nlm.nih.gov/medlineplus/images/leg.jpg");
		File file = imageManipulator.downloadImage(url);
	}
	
	

	
	
}
