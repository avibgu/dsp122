package utilities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class FileManipulator {
	
	public static Vector<String> readFromInputStream(
			InputStream pInputStream, boolean limited) {

		Vector<String> result = new Vector<String>();
		
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {

			isr = new InputStreamReader(pInputStream);
			br = new BufferedReader(isr);		
			
			if(!limited)
				while (br.ready())
					result.add(br.readLine());
			else
				for(int i = 0; i < 1000000 && br.ready(); i++)
					result.add(br.readLine());

			isr.close();
			br.close();
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
}
