package common.utilities;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import org.junit.Test;

public class ImageManipulatorTest {

	@Test
	public void test() {
		
	}

	@Test
	public void findFacesTest() throws IOException{

		Vector<File> files = ImageManipulator.cropFacesFromImage(new File("nedStarck.jpg"));
	}
	
	@Test
	public void ImageManipulatorTest() throws Exception {
		URL url = new URL("http://www.nlm.nih.gov/medlineplus/images/leg.jpg");
		File file = ImageManipulator.downloadImage(url);
	}
}
