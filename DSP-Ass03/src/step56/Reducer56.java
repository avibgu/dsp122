package step56;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.mapreduce.Reducer;

import data.Cluster;
import data.Global;
import data.Word;

public class Reducer56 extends Reducer<Word, Cluster, Word, Cluster> {

	protected ArrayList<Cluster> mClustersArray;
	
	protected void setup(Context context) throws IOException,
			InterruptedException {
		mClustersArray = new ArrayList<Cluster>();
	};

	protected void reduce(Word hookWord, Iterable<Cluster> clusters,
			Context context) throws IOException, InterruptedException {

		mClustersArray.clear();

		for (Cluster cluster : clusters){
			
			try {
				mClustersArray.add((Cluster) cluster.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		
		// merge clusters that share more than S percent of their patterns
		for (int i = 0; i < mClustersArray.size(); i++) {

			boolean merged = false;

			for (int j = i + 1; j < mClustersArray.size(); j++) {

				if (mClustersArray.get(i).calcSharedPatternsPercents(
						mClustersArray.get(j)) > Global.S) {

					mClustersArray.get(j).mergeWith(mClustersArray.get(i));

					merged = true;

					break;
				}
			}

			if (!merged)
				context.write(hookWord, mClustersArray.get(i));
			
			mClustersArray.set(i, null);
			
//			System.gc();
		}
	};
}
