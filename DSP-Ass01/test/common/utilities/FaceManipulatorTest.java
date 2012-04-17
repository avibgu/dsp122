package common.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;



public class FaceManipulatorTest {

	private FaceManipulator fm = new FaceManipulator();
	
	@Test
	public void findFacesTest() throws IOException{
		
		File output = new File("face-test.jpg");
		BufferedImage bi = ImageIO.read(new FileInputStream("nedStarck.jpg"));

		fm.findFaces(bi, 1, 40, output);
		
	}
}
