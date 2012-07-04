package step4;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.mapreduce.Reducer;

import data.Cluster;
import data.Pattern;
import data.Word;

public class Reducer4 extends Reducer<Word, Pattern, Word, Cluster> {

	private static final int S = 10;
	
	protected Map<Word, Cluster> mTargetToPatternsMap;

	protected void setup(Context context) throws IOException,
			InterruptedException {

		mTargetToPatternsMap = new HashMap<Word, Cluster>();
	};

	protected void reduce(Word hookWord, Iterable<Pattern> patterns,
			Context context) throws IOException, InterruptedException {

		for (Pattern pattern : patterns){

			Cluster cluster = mTargetToPatternsMap.get(pattern.getTarget());
			
			if (null == cluster){
				cluster = new Cluster();
				mTargetToPatternsMap.put(pattern.getTarget(), cluster);
			}
			
			cluster.add(pattern);
		}
		
		Cluster[] clusters = (Cluster[]) mTargetToPatternsMap.values().toArray();
		
		Cluster tmpCluster = new Cluster();
		
		for (int i = 0; i < clusters.length; i++){
			
			boolean merged = false;
			
			for (int j = i + 1; j < clusters.length; j++){
				
				if (clusters[i].calcSharedPercents(clusters[j]) > S){
					
					tmpCluster.mergeClusters(clusters[i],clusters[j]);
					context.write(hookWord, tmpCluster);
					merged = true;
				}
			}
			
			if (!merged)
				context.write(hookWord, clusters[i]);
		}
	};
}
