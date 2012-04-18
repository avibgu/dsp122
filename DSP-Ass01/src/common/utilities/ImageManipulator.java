package common.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;

import jjil.algorithm.RgbAvgGray;
import jjil.core.Rect;
import jjil.core.RgbImage;
import jjil.j2se.RgbImageJ2se;

public class ImageManipulator {

	private static final int MIN_SCALE = 1;
	private static final int MAX_SCALE = 10;

	public static File downloadImage(URL pUrl) throws IOException {
		// TEST download the image file indicated in the message (the url
		// provided).

		BufferedImage image = null;

		File file = new File(String.valueOf(new Date().getTime()) + ".jpg");

		try {

			image = ImageIO.read(pUrl);
			ImageIO.write(image, "jpg", file);
		}

		catch (Exception e) {
			// TODO: handle exception
		}

		return file;
	}

	public static Vector<File> cropFacesFromImage(File pImage) {
		// TEST Identify faces in the image (if any), and save each face found
		// in a separate image file.

		Vector<File> faces = new Vector<File>();

		BufferedImage bufferedImage = null;
		InputStream inputStream = null;
		Gray8DetectHaarMultiScale detectHaar = null;
		RgbImage rgbImage = null;
		RgbAvgGray toGray = new RgbAvgGray();
		List<Rect> rectangles = null;
		RgbImageJ2se converter = new RgbImageJ2se();
		BufferedImage tBufferedImage = null;
		RgbImage face = null;
		File tFile = null;

		try {

			bufferedImage = ImageIO.read(new FileInputStream(pImage));

			inputStream = new FileInputStream(new File(
					"src/common/utilities/haar/profileface.txt"));

			detectHaar = new Gray8DetectHaarMultiScale(inputStream, MIN_SCALE,
					MAX_SCALE);

			rgbImage = RgbImageJ2se.toRgbImage(bufferedImage);

			toGray.push(rgbImage);

			rectangles = detectHaar.pushAndReturn(toGray.getFront());

			System.out.println("Found " + rectangles.size() + " faces");

			for (Rect rect : rectangles) {

				tBufferedImage = bufferedImage.getSubimage(rect.getTopLeft()
						.getX(), rect.getTopLeft().getY(), rect.getWidth(),
						rect.getHeight());

				face = RgbImageJ2se.toRgbImage(tBufferedImage);

				tFile = new File(String.valueOf(new Date().getTime()) + ".jpg");

				converter.toFile(face, tFile.getCanonicalPath());

				faces.add(tFile);
			}
		}

		catch (Throwable e) {
			// TODO : handle Exception
		}

		return faces;
	}

	// // TODO.. change the file path
	// String filePath = "out.jpg";
	// java.io.InputStream in = pUrl.openStream();
	// OutputStream out = new BufferedOutputStream(new FileOutputStream(
	// filePath));
	// for (int b; (b = in.read()) != -1;) {
	// out.write(b);
	// System.out.print("check");
	// }
	// out.close();
	// in.close();
	// String ans = "success";
	// System.out.println("The image has been successfully downloaded.");
	//
	// return ans;
}
