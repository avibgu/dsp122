package step6;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Reducer;

import data.Cluster;
import data.Word;

public class Reducer6 extends Reducer<Cluster, Word, Word, Cluster> {

	private static final int S = 10;

	protected Cluster mCluster;

	protected void setup(Context context) throws IOException,
			InterruptedException {
		mCluster = null;
	};

	protected void reduce(Cluster cluster, Iterable<Word> hookWords,
			Context context) throws IOException, InterruptedException {

		if (null == mCluster) {
			mCluster = cluster;
			return;
		}

		if (mCluster.getHookWord().equals(cluster.getHookWord()))
			return;

		if (mCluster.calcSharedPatternsPercents(cluster) >= S
				&& mCluster.areShareAllCorePatterns(cluster))
			mCluster.mergeWithOtherClusterAndMarkCorePatterns(cluster);
		
		else
			context.write(cluster.getHookWord(), cluster);
	};

	protected void cleanup(Context context) throws IOException,
			InterruptedException {
		if (!mCluster.isAllUnconfirmed())
			context.write(mCluster.getHookWord(), mCluster);
	};
}
