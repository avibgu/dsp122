package utilities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class FileManipulator {
	
	public static Vector<String> readFromInputStream(
			InputStream pInputStream) {

		Vector<String> result = new Vector<String>();
		
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {

			isr = new InputStreamReader(pInputStream);
			br = new BufferedReader(isr);		
			
			while (br.ready())
				result.add(br.readLine());

			pInputStream.close();
			isr.close();
			br.close();
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
