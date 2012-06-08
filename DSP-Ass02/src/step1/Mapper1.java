package step1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.Main;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import utilities.FileManipulator;

public class Mapper1 extends Mapper<LongWritable, Text, Text, Text> {

	protected Set<String> mStopWords;

	protected void setup(Context context) throws IOException,
			InterruptedException {

		mStopWords = new HashSet<String>();	//TODO
//		mStopWords = FileManipulator.readFromInputStream(Mapper1.class
//				.getResourceAsStream("english-stop-words.txt"));
	}

	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

		String[] splitted = value.toString().split("\t");

		String occurrences = splitted[2];
		
		splitted = splitted[0].split(" ");
				
		if (splitted.length < 3 || isStopWord(splitted[2]))
			return;

		Text word = new Text(splitted[2]);

		List<String> list = new ArrayList<String>();

		try{
		
			list.add(splitted[0]);
			list.add(splitted[1]);
			list.add(splitted[3]);
			list.add(splitted[4]);
		}
		catch (Exception e) {}
		
		list = filterStopWords(list);

		for (String w : list)
			context.write(word, new Text(w + "\t" + occurrences));
	}

	private boolean isStopWord(String word) {
		return mStopWords.contains(word);
	}

	private List<String> filterStopWords(List<String> words) {

		List<String> tList = new ArrayList<String>();

		for (String word : words)
			if (!isStopWord(word))
				tList.add(word);

		return tList;
	}
}
