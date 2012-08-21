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
	protected int mFileIndex;
	protected String mFileName;

	protected void setup(Context context) throws IOException,
			InterruptedException {

		mFileIndex = context.getConfiguration().getInt("fileIndex", 0);

		if (mFileIndex == 0)
			mFileName = "train/relation-1-train.txt";
		else
			mFileName = "test/relation-1-test.txt";

		Reader reader = new Reader(mFileName);

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
