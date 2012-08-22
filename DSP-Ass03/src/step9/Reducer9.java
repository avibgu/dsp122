package step9;

import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import data.Cluster;
import data.WordsPair;

public class Reducer9 extends Reducer<WordsPair, Cluster, Text, Text> {

	protected SortedMap<String, Double> mHitsMap;
	
	protected void setup(Context context) throws IOException,
			InterruptedException {

		mHitsMap = new TreeMap<String, Double>();
	};

	protected void reduce(WordsPair wordsPair, Iterable<Cluster> clusters,
			Context context) throws IOException, InterruptedException {

		mHitsMap.clear();
		
		for (Cluster cluster : clusters)
			mHitsMap.put(cluster.getId(), cluster.clacHits(wordsPair));

		context.write(new Text(getAsARFFData() + ","
				+ wordsPair.getPositivity()), new Text());
	}
	
	protected String getAsARFFData(){
		
		// HITS(C1(W1,W2)):numeric,...,HITS(Cn(W1,W2)):numeric,{positive \ negative}

		String stringVector = "";
		
		for (Double hit : mHitsMap.values())
			stringVector += "," + hit;
		
		return stringVector.substring(1);
				
	}
}
