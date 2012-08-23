package step7;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.mapreduce.Reducer;

import data.Cluster;
import data.PatternInstance;

public class Reducer7 extends Reducer<PatternInstance, Cluster, Cluster, Cluster> {

	protected List<Cluster> mClusters;

	protected void setup(Context context) throws IOException,
			InterruptedException {
		
		mClusters = new ArrayList<Cluster>();
	};

	protected void reduce(PatternInstance pattern, Iterable<Cluster> clusters,
			Context context) throws IOException, InterruptedException {
		
		mClusters.clear();
		
		for (Cluster cluster : clusters)
			mClusters.add(cluster);
		
		for (int i = 0; i < mClusters.size(); i++){
			
			Cluster clusterI = mClusters.get(i);
			
			boolean writed = false;
			
			for (int j = i + 1; j < mClusters.size(); j++){
				
				Cluster clusterJ = mClusters.get(j);
				
				if (clusterI.areShareAllCorePatterns(clusterJ)){
					context.write(clusterI, clusterJ);
					writed = true;
				}
			}
			
			if (!writed)
				context.write(clusterI, null);
		}
	}
}
