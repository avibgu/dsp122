package common.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import jjil.algorithm.Gray8Rgb;
import jjil.algorithm.RgbAvgGray;
import jjil.core.Image;
import jjil.core.Rect;
import jjil.core.RgbImage;
import jjil.j2se.RgbImageJ2se;


public class FaceManipulator {

	public FaceManipulator() {
	}

	public static void findFaces(BufferedImage bi, int minScale, int maxScale,
			File output) {

		try {
			File profileface = new File("src/common/utilities/haar/profileface.txt");
			InputStream is = new FileInputStream(profileface); 

			Gray8DetectHaarMultiScale detectHaar = new Gray8DetectHaarMultiScale(
					is, minScale, maxScale);

			RgbImage im = RgbImageJ2se.toRgbImage(bi);

			RgbAvgGray toGray = new RgbAvgGray();

			toGray.push(im);

			List<Rect> results = detectHaar.pushAndReturn(toGray.getFront());

			System.out.println("Found " + results.size() + " faces");

			Image i = detectHaar.getFront();

			Gray8Rgb g2rgb = new Gray8Rgb();

			g2rgb.push(i);

			RgbImageJ2se conv = new RgbImageJ2se();
			
			for (Rect rect : results){
				
				BufferedImage tBi = bi.getSubimage(rect.getTopLeft().getX(), rect.getTopLeft().getY(), rect.getWidth(), rect.getHeight());
				RgbImage face = RgbImageJ2se.toRgbImage(tBi);
				
				conv.toFile(face, new File(Double.toString(Math.random()) + ".jpg").getCanonicalPath());
			}
		}

		catch (Throwable e) {
			throw new IllegalStateException(e);
		}
	}

}
