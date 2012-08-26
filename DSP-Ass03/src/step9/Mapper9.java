package step9;

import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import utilities.Reader;

import data.Cluster;
import data.Global;
import data.WordsPair;

public class Mapper9 extends Mapper<Text, Cluster, WordsPair, Cluster> {

	protected Vector<WordsPair> mTrainingPairs;
	protected int mFileIndex;
	protected String mFileName;

	protected void setup(Context context) throws IOException,
			InterruptedException {

		mFileIndex = context.getConfiguration().getInt("fileIndex", 0);

		if (0 == mFileIndex)
			mFileName = "train/relation-1-train.txt";
		else if (1 == mFileIndex)
			mFileName = "test/relation-1-test.txt";
		else if (2 == mFileIndex)
			mFileName = "answer/relation-1-score.txt";

		mFileName = "https://s3.amazonaws.com/" + Global.BUCKET_NAME + "/"
				+ mFileName;

		Reader reader = new Reader(mFileName);

		try {
			mTrainingPairs = reader.readWordPairs(new URL(mFileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		filterPairs();
	};

	private void filterPairs() {

		Vector<WordsPair> tmp = new Vector<WordsPair>();
		
		for (WordsPair pair : mTrainingPairs)
			if (!pair.getW1().contains(" ") && !pair.getW2().contains(" "))
				tmp.add(pair);
		
		mTrainingPairs = tmp;
	}

	protected void map(Text word, Cluster cluster, Context context)
			throws IOException, InterruptedException {

		for (WordsPair wordPair : mTrainingPairs)
			context.write(wordPair, cluster);
	}
}
