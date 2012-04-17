package common.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class FileManipulator {

	public static void convertSummaryFileToOutputFile(File summaryFile,
			File outputFile) {

		writeToFile(convertToHTML(readFromFile(summaryFile)), outputFile);
		
	}

	public static Vector<URL> retrieveURLsFromInputFile(File listOfImagesFile) throws MalformedURLException {

		Vector<URL> result = new Vector<URL>();
		Vector<String> urls = readFromFile(listOfImagesFile);
	
		for(String str : urls){
			try{
				result.add(new URL(str));
			}catch (MalformedURLException e) {
				System.out.println("Mal formed url entered");
			}
		}
		
		return result;
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
				
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;

		try {

			fos = new FileOutputStream(pFile);
			osw = new OutputStreamWriter(fos);
			bw = new BufferedWriter(osw);		
			
			for(String str : pContent)
				bw.write(str + "\n");
				
			bw.close();	
			osw.close();
			fos.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
