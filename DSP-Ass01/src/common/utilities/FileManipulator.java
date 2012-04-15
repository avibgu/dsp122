package common.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Vector;

public class FileManipulator {

	public static void convertSummaryFileToOutputFile(File summaryFile,
			File outputFile) {

		writeToFile(convertToHTML(readFromFile(summaryFile)), outputFile);
	}

	public static Vector<URL> retrieveURLsFromInputFile(File listOfImagesFile) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Vector<String> readFromFile(File pFile) {

		Vector<String> result = new Vector<String>();
		
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {

			fis = new FileInputStream(pFile);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);		
			
			while (br.ready())
				result.add(br.readLine() + "\n");

			fis.close();
			isr.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	//TODO: test it..
	public static Vector<String> convertToHTML(Vector<String> x){

		Vector<String> result = new Vector<String>();
		
		result.add("<html>");
		result.add("<title>Faces! - Avi & Batel</title>");
		result.add("<body>");
		
		for (String line : x){
			
			String[] params = line.split("\t");
			
			result.add("<a href=\"" + params[0] + "\">");
			result.add("<img src=\"" + params[1] + "\"/>");
			result.add("</a>");
		}
		
		result.add("</body>");
		result.add("</html>");
		
		return result;
	}
	
	public static void writeToFile(Vector<String> pContent, File pFile){
		// TODO Auto-generated method stub
	}
}
