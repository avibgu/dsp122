package step8old;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.mapreduce.Reducer;

import data.Cluster;
import data.Global;
import data.Word;

public class Reducer8old extends Reducer<Cluster, Cluster, Word, Cluster> {

	protected Set<Cluster> mClusters;

	protected void setup(Context context) throws IOException,
			InterruptedException {
		
		mClusters = new HashSet<Cluster>();
	};

	protected void reduce(Cluster cluster, Iterable<Cluster> clusters,
			Context context) throws IOException, InterruptedException {
		
		mClusters.clear();
		
		for (Cluster tmpCluster : clusters)
			mClusters.add(tmpCluster);

		for (Cluster tmpCluster : mClusters){
			
			if (null == tmpCluster)
				continue;
				
			if (cluster.calcSharedPatternsPercents(tmpCluster) >= Global.S
					&& cluster.areShareAllCorePatterns(tmpCluster))
				cluster.mergeWithOtherClusterAndMarkCorePatterns(tmpCluster);
		}

		context.write(cluster.getHookWord(), cluster);
	}
}
