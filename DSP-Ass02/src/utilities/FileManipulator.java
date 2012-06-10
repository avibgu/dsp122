package utilities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class FileManipulator {
	
	public static Set<String> readFromInputStream(
			InputStream pInputStream) {

		Set<String> result = new HashSet<String>();
		
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {

			isr = new InputStreamReader(pInputStream, "UTF-8");
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
