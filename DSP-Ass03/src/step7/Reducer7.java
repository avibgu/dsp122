package step7;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.mapreduce.Reducer;

import data.Cluster;
import data.CorePatternsList;
import data.Global;
import data.Word;

public class Reducer7 extends Reducer<CorePatternsList, Cluster, Word, Cluster> {

	protected List<Cluster> mClusters;

	protected void setup(Context context) throws IOException,
			InterruptedException {

		mClusters = new ArrayList<Cluster>();
	};

	protected void reduce(CorePatternsList corePatterns,
			Iterable<Cluster> clusters, Context context) throws IOException,
			InterruptedException {

		mClusters.clear();

		for (Cluster cluster : clusters)
			mClusters.add(cluster);

		for (int i = 0; i < mClusters.size(); i++) {

			Cluster clusterI = mClusters.get(i);

			for (int j = i + 1; j < mClusters.size(); j++) {

				Cluster clusterJ = mClusters.get(j);

				if (clusterI.calcSharedPatternsPercents(clusterJ) >= Global.S)

					clusterI.mergeWithOtherClusterAndMarkCorePatterns(clusterJ);

			}

			context.write(clusterI.getHookWord(), clusterI);

		}
	}
}
