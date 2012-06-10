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

	protected Text word;
	protected Text outValue;

	protected void setup(Context context) throws IOException,
			InterruptedException {

		mStopWords = FileManipulator.readFromInputStream(Mapper1.class
				.getResourceAsStream("hebrew-stop-words.txt"));

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

		List<String> list = new ArrayList<String>();

		try {

			list.add(splitted[0]);
			list.add(splitted[1]);
			list.add(splitted[3]);
			list.add(splitted[4]);
		} catch (Exception e) {
		}

		list = cleanWords(list);
		list = filterStopWords(list);

		for (String w : list) {

			outValue.set(w + "\t" + occurrences);

			context.write(word, outValue);

			// System.out.println("word:" + word);
			// System.out.println("value:" + outValue);
		}
	}

	private List<String> cleanWords(List<String> words) {

		List<String> tList = new ArrayList<String>();

		for (String word : words)
			tList.add(cleanWord(word));

		return tList;
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

		List<String> tList = new ArrayList<String>();

		for (String word : words)
			if (!isStopWord(word))
				tList.add(word);
		// else
		// System.out.println("stop:" + word);

		return tList;
	}
}
