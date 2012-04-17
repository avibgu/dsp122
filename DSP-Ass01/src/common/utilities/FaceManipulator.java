package common.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import jjil.algorithm.Gray8Rgb;
import jjil.algorithm.RgbAvgGray;
import jjil.core.Image;
import jjil.core.RgbImage;
import jjil.j2se.RgbImageJ2se;
import jjil.algorithm.Gray8DetectHaarMultiScale;

public class FaceManipulator {

	public FaceManipulator() {
	}

	public static void findFaces(BufferedImage bi, int minScale, int maxScale,
			File output) {

		try {
			InputStream is = FaceManipulator.class
					.getResourceAsStream("/common/utilities/haar/HCSB.txt");
			
			Gray8DetectHaarMultiScale detectHaar = new Gray8DetectHaarMultiScale(
					is, minScale, maxScale);
			
			RgbImage im = RgbImageJ2se.toRgbImage(bi);
			
			RgbAvgGray toGray = new RgbAvgGray();
			
			toGray.push(im);
			
			detectHaar.push(toGray.getFront());
			
			Image i = detectHaar.getFront();
			
			Gray8Rgb g2rgb = new Gray8Rgb();
			
			g2rgb.push(i);
			
			RgbImageJ2se conv = new RgbImageJ2se();
			
			conv.toFile((RgbImage) g2rgb.getFront(), output.getCanonicalPath());
		}

		catch (Throwable e) {
			throw new IllegalStateException(e);
		}
	}

}
