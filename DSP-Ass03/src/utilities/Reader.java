package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import data.WordsPair;


public class Reader {
	
	private String fileName;
	private Set<String> writtenLines;
	
	public Reader(String fileName){
		this.fileName = fileName;
		writtenLines = new HashSet<String>();
	}
	
	public Vector<WordsPair> readWordPairs() throws Exception{
		
			File gf = new File(fileName);
			InputStream fis = new FileInputStream(gf);

			Vector<WordsPair> wordPairs = new Vector<WordsPair>();
			Vector<String> lines = FileManipulator.readFromInputStream(fis, false); 
			
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

	
	public void filterCorpus() throws IOException{
		
		File gf = new File(fileName);
		InputStream fis = new FileInputStream(gf);
		
		FileWriter fstream = new FileWriter("filteredCorpus", true);
		PrintWriter pw = new PrintWriter(fstream, true);		

		Vector<String> lines = new Vector<String>();
		
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {

			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);		
		
			for(int i = 0; i < 100000 && br.ready(); i++)
				lines.add(br.readLine());
			
			while(!lines.isEmpty()) {

				for(String line : lines)
					if(containsFiveWords(line))
						pw.write(line + "\n");
				
				lines.clear();
				System.gc();
				
				for(int i = 0; i < 100000 && br.ready(); i++)
					lines.add(br.readLine());
				
			}

		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
		
		fis.close();
		isr.close();
		br.close();
	}

	private boolean containsFiveWords(String line) {

		String[] splitted = line.split("\t");
		String[] splittedWords = splitted[0].split(" "); 
		
		if(splittedWords.length != 5)
			return false;
		
		for(int i = 0; i < 5; i++){
			int ch = splittedWords[i].toCharArray()[0];
			if((ch < 65 || ch > 90) && (ch < 97 || ch > 122 ))
				return false;
		}
		
		if(writtenLines.contains(splitted[0]))
			return false;
		
		else writtenLines.add(splitted[0]);
		
		return true;
	}
	
	
	
	
	
	
	
	
	
}
