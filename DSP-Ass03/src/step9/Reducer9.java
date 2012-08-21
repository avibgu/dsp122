package step9;

import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import data.Cluster;
import data.FeatureVector;
import data.WordsPair;

public class Reducer9 extends Reducer<WordsPair, Cluster, Text, Text> {

	protected SortedMap<String, Double> mHitsMap;
	protected FeatureVector mFeatureVector;

	protected void setup(Context context) throws IOException,
			InterruptedException {

		mHitsMap = new TreeMap<String, Double>();
		mFeatureVector = new FeatureVector();
	};

	protected void reduce(WordsPair wordsPair, Iterable<Cluster> clusters,
			Context context) throws IOException, InterruptedException {

		mHitsMap.clear();
		mFeatureVector.clear();

		for (Cluster cluster : clusters)
			mHitsMap.put(cluster.getId(), cluster.clacHits(wordsPair));

		mFeatureVector.set(mHitsMap);

		context.write(new Text(mFeatureVector.getAsARFFData() + ","
				+ wordsPair.getPositivity()), new Text());
	}
}
