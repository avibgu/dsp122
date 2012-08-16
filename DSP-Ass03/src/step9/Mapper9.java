package step9;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.mapreduce.Mapper;

import data.Cluster;
import data.Word;
import data.WordsPair;

public class Mapper9 extends Mapper<Word, Cluster, WordsPair, Cluster> {

	protected Set<WordsPair> mTrainingPairs;
	
	protected void setup(Context context) throws IOException,
			InterruptedException {
		
		mTrainingPairs = new HashSet<WordsPair>();
		
		// TODO: load training pairs..
	};

	protected void map(Word word, Cluster cluster, Context context)
			throws IOException, InterruptedException {

		for (WordsPair wordPair : mTrainingPairs)
			context.write(wordPair, cluster);
	}
}
