package step7;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import data.Cluster;
import data.CorePatternsList;
import data.Global;

public class Reducer7 extends Reducer<CorePatternsList, Cluster, Text, Cluster> {

	protected List<Cluster> mClusters;
	protected Text mTextWord;

	protected void setup(Context context) throws IOException,
			InterruptedException {
		
		mTextWord = new Text();
		mClusters = new ArrayList<Cluster>();
	};

	protected void reduce(CorePatternsList corePatterns,
			Iterable<Cluster> clusters, Context context) throws IOException,
			InterruptedException {

		mClusters.clear();

		for (Cluster cluster : clusters) {

			try {
				mClusters.add((Cluster) cluster.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < mClusters.size(); i++) {

			Cluster clusterI = mClusters.get(i);

			for (int j = i + 1; j < mClusters.size(); j++) {

				Cluster clusterJ = mClusters.get(j);

				if (clusterI.calcSharedPatternsPercents(clusterJ) >= Global.S)
					clusterI.mergeWithOtherClusterAndMarkCorePatterns(clusterJ);
			}

			mTextWord.set(clusterI.getHookWord());
			context.write(mTextWord, clusterI);
		}
	}
}
