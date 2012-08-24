package step5;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.mapreduce.Reducer;

import data.Cluster;
import data.Global;
import data.Pattern;
import data.Word;

public class Reducer5 extends Reducer<Word, Pattern, Word, Cluster> {

	protected Map<Word, Cluster> mTargetToPatternsMap;

	protected void setup(Context context) throws IOException,
			InterruptedException {

		mTargetToPatternsMap = new HashMap<Word, Cluster>();
	};

	protected void reduce(Word hookWord, Iterable<Pattern> patterns,
			Context context) throws IOException, InterruptedException {

		mTargetToPatternsMap.clear();

		//Union by Target word
		for (Pattern pattern : patterns) {

			try {

				for (Word target : pattern.getTargets()) {

					Cluster cluster = mTargetToPatternsMap.get(target.clone());

					if (null == cluster) {

						cluster = new Cluster();
						cluster.setHookWord(hookWord);
						mTargetToPatternsMap
								.put((Word) target.clone(), cluster);
					}

					cluster.add((Pattern) pattern.clone());
				}
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		
		Collection<Cluster> clustersCollection = mTargetToPatternsMap.values();

		Cluster[] clusters = new Cluster[clustersCollection.size()];

		int i = 0;

		for (Cluster cluster : clustersCollection)
			clusters[i++] = cluster;

		Cluster tmpCluster = new Cluster();

		//merge clusters that share more than S percent of their patterns
		for (i = 0; i < clusters.length; i++) {

			boolean merged = false;

			for (int j = i + 1; j < clusters.length; j++) {

				if (clusters[i].calcSharedPatternsPercents(clusters[j]) > Global.S) {

					tmpCluster.mergeClusters(clusters[i], clusters[j]);
					context.write(hookWord, tmpCluster);

					merged = true;
				}
			}

			if (!merged)
				context.write(hookWord, clusters[i]);

		}
	};
}
