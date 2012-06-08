package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import main.Main;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import utilities.FileManipulator;

public class TestMapper extends Mapper<LongWritable, Text, Text, Text> {

	protected Set<String> mStopWords;

	protected void setup(Context context) throws IOException,
			InterruptedException {

		mStopWords = new HashSet<String>(); // TODO
		// mStopWords = FileManipulator.readFromInputStream(Mapper1.class
		// .getResourceAsStream("english-stop-words.txt"));
	}

	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

		String[] splitted = value.toString().split("\t");

		context.write(new Text(UUID.randomUUID().toString()), new Text(splitted[0]));
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
