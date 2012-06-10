package step1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import utilities.FileManipulator;

public class Mapper1 extends Mapper<LongWritable, Text, Text, Text> {

	protected Set<String> mStopWords;

	protected List<String> mListOfWords;
	protected List<String> mTempList1;
	protected List<String> mTempList2;
	
	protected Text word;
	protected Text outValue;

	protected void setup(Context context) throws IOException,
			InterruptedException {

		mStopWords = FileManipulator.readFromInputStream(Mapper1.class
				.getResourceAsStream("hebrew-stop-words.txt"));

		mListOfWords = new ArrayList<String>();
		mTempList1 = new ArrayList<String>();
		mTempList2 = new ArrayList<String>();
		
		word = new Text();
		outValue = new Text();
	}

	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		
		String[] splitted = value.toString().split("\t");

		if (splitted.length != 5)
			return;

		String occurrences = splitted[2];

		splitted = splitted[0].split(" ");

		if (splitted.length != 5 || isStopWord(splitted[2]))
			return;

		word.set(cleanWord(splitted[2]));

		mListOfWords.clear();

		try {

			mListOfWords.add(splitted[0]);
			mListOfWords.add(splitted[1]);
			mListOfWords.add(splitted[3]);
			mListOfWords.add(splitted[4]);
		} catch (Exception e) {
		}

		mListOfWords = cleanWords(mListOfWords);
		mListOfWords = filterStopWords(mListOfWords);

		for (String w : mListOfWords) {

			outValue.set(w + "\t" + occurrences);

			context.write(word, outValue);

//			 System.out.println("word:" + word);
//			 System.out.println("value:" + outValue);
		}
	}

	private List<String> cleanWords(List<String> words) {

		mTempList1.clear();

		for (String word : words)
			mTempList1.add(cleanWord(word));

		return mTempList1;
	}

	private String cleanWord(String word) {
		
		if (word.startsWith("\""))
			word = word.substring(1);
		
		if (word.endsWith("\""))
			word = word.substring(0, word.length()-1);
		
		return word;
				
//		return (word.replace('\"', ' ')).trim();
	}

	private boolean isStopWord(String word) {
		return word.length() < 2 || mStopWords.contains(word);
	}

	private List<String> filterStopWords(List<String> words) {

		mTempList2.clear();

		for (String word : words)
			if (!isStopWord(word))
				mTempList2.add(word);

		return mTempList2;
	}
}
