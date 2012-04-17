package common.utilities;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
//import java.io.InputStream;
import java.net.URL;
import java.util.Vector;

import org.omg.CORBA.portable.InputStream;

public class ImageManipulator {

	public static String downloadImage(URL url) throws IOException {
		// TODO change the file path
		String filePath = "out.jpg";
        java.io.InputStream in = url.openStream();
        OutputStream out = new BufferedOutputStream(new FileOutputStream(filePath));
        for (int b; (b = in.read()) != -1; ) {
            out.write(b);
            System.out.print("check");
        }
        out.close();
        in.close();
        String ans = "success";
        System.out.println("The image has been successfully downloaded.");
	 
		return ans;
	}

	public static Vector<Object> cropFacesFromImage(Object image) {
		// TODO Identify faces in the image (if any), and save each face found
		// in a separate image file.
		return null;
	}

}
