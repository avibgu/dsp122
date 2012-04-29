package common.utilities;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class ImageManipulatorTest {

	@Test
	public void findFacesTest() throws IOException{

		ImageManipulator.cropFacesFromImage(new File("pic.jpg"));
	}
}
