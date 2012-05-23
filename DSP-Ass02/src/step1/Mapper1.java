package step1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Mapper1 extends Mapper<LongWritable, Text, Text, Text> {

	protected Set<String> mStopWords;
	
	protected void setup(Context context) throws IOException,
			InterruptedException {
		
		mStopWords = new HashSet<String>();
		
		loadStopWords();
	}

	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

		String[] splitted = value.toString().split("\t");

		if (isStopWord(splitted[2]))
			return;

		Text word = new Text(splitted[2]);

		List<String> list = new ArrayList<String>();

		list.add(splitted[0]);
		list.add(splitted[1]);
		list.add(splitted[3]);
		list.add(splitted[4]);

		list = filterStopWords(list);

		for (String w : list)
			context.write(word, new Text(w));
	}

	private void loadStopWords() {
		// TODO Auto-generated method stub
		
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
