package jjilexample;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;

import jjil.algorithm.Gray8Rgb;
import jjil.algorithm.RgbAvgGray;
import jjil.core.Image;
import jjil.core.RgbImage;
import jjil.j2se.RgbImageJ2se;

public class Main {
	public static void findFaces(BufferedImage bi, int minScale, int maxScale,
			File output) {
		try {
			
			// step #2 - convert BufferedImage to JJIL Image
			RgbImage im = RgbImageJ2se.toRgbImage(bi);
			
			// step #3 - convert image to greyscale 8-bits
			RgbAvgGray toGray = new RgbAvgGray();
			toGray.push(im);
			
			// step #4 - initialise face detector with correct Haar profile
			InputStream is = Main.class
					.getResourceAsStream("/jjilexample/haar/HCSB.txt");
			Gray8DetectHaarMultiScale detectHaar = new Gray8DetectHaarMultiScale(
					is, minScale, maxScale);
			
			// step #5 - apply face detector to grayscale image
			detectHaar.push(toGray.getFront());
			
			// step #6 - retrieve resulting face detection mask
			Image i = detectHaar.getFront();
			
			// finally convert back to RGB image to write out to .jpg file
			Gray8Rgb g2rgb = new Gray8Rgb();
			g2rgb.push(i);
			RgbImageJ2se conv = new RgbImageJ2se();
			conv.toFile((RgbImage) g2rgb.getFront(), output.getCanonicalPath());
		}
		
		catch (Throwable e) {
			throw new IllegalStateException(e);
		}
	}

	public static void main(String[] args) throws Exception {
		
		// step #1 - read source image
		BufferedImage bi = ImageIO.read(Main.class
				.getResourceAsStream("test.jpg"));
		
		// onto following steps...
		findFaces(bi, 1, 40, new File("c:/Temp/result.jpg")); // change as
																// needed
	}
}