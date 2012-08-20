package step9;

import java.io.IOException;
import java.util.Vector;

import org.apache.hadoop.mapreduce.Mapper;

import utilities.Reader;

import data.Cluster;
import data.Word;
import data.WordsPair;

public class Mapper9 extends Mapper<Word, Cluster, WordsPair, Cluster> {

	protected Vector<WordsPair> mTrainingPairs;
	
	protected void setup(Context context) throws IOException,
			InterruptedException {
		
		Reader reader = new Reader("try");
		
		try {
			mTrainingPairs = reader.readWordPairs();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	};

	protected void map(Word word, Cluster cluster, Context context)
			throws IOException, InterruptedException {

		for (WordsPair wordPair : mTrainingPairs)
			context.write(wordPair, cluster);
	}
}


