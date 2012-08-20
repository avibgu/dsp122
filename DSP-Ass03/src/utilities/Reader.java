package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Vector;

import data.WordsPair;


public class Reader {
	
	private String fileName;
	
	public Reader(String fileName){
		this.fileName = fileName;
	}
	
	public Vector<WordsPair> readWordPairs() throws Exception{
		
			File gf = new File(fileName);
			InputStream fis = new FileInputStream(gf);

			Vector<WordsPair> wordPairs = new Vector<WordsPair>();
			Vector<String> lines = FileManipulator.readFromInputStream(fis); 
			
			for(int i = 0; i < lines.size(); i++){
				String line = lines.elementAt(i);
				WordsPair wordPair = findWordPair(line);
				if(wordPair != null){
					wordPair.setPositivity(findPositivity(lines.elementAt(i+1)));
					if(lines.elementAt(i+1).indexOf("(e2,e1)") != -1)
						wordPair.revertWords();
					wordPairs.add(wordPair);
				}
			}
			
			return wordPairs;
	}


	private String findPositivity(String line) {
		
		String positivity = ""; 
		
		if(line.indexOf("true") != -1)
			positivity = "true";
		else if(line.indexOf("false") != -1)
			positivity = "false";
		
		return positivity;
	}
	
	private WordsPair findWordPair(String line) {

		int beginIndex = line.indexOf(62);
		int endIndex = line.indexOf(60, beginIndex);
		
		if(beginIndex == -1 || endIndex == -1)
			return null;
		
		String e1 = line.substring(beginIndex + 1, endIndex);
		
		beginIndex = line.indexOf("<e2>") + 4 ;
		endIndex = line.indexOf("</e2>");
		
		String e2 = line.substring(beginIndex, endIndex);
		
		
		
		WordsPair wordPair = new WordsPair(e1, e2, "true");
	
		return wordPair;
	}

}
