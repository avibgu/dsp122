package common.utilities;

import java.io.File;
import java.io.IOException;
import java.util.Vector;


import org.junit.Test;



public class FaceManipulatorTest {
	
	@Test
	public void findFacesTest() throws IOException{

		Vector<File> files = ImageManipulator.cropFacesFromImage(new File("nedStarck.jpg"));
	}
}
