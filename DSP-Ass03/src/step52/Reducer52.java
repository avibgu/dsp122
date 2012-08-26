package step52;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import data.Cluster;
import data.Global;

public class Reducer52 extends Reducer<Text, Cluster, Text, Cluster> {

	protected void reduce(Text hookWord, Iterable<Cluster> clusters,
			Context context) throws IOException, InterruptedException {

		Cluster clusterI = null;

		boolean found = false;
		
		for (Cluster cluster : clusters) {
		
			if (!cluster.hasMerged()) {
			
				try {
					clusterI = (Cluster) cluster.clone();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				
				found = true;
				break;
			}
		}
		
		if (!found)
			return;
		
		context.getCounter("Counters", "toStop").increment(1);

		clusterI.markAsMerged();

		for (Cluster clusterJ : clusters) {

			if (!clusterJ.hasMerged()
					&& clusterI.calcSharedPatternsPercents(clusterJ) > Global.S) {

				try {
					((Cluster) (clusterJ.clone())).mergeWith(clusterI);
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}

				context.write(hookWord, clusterJ);

				return;
			}
		}

		context.write(hookWord, clusterI);
	};
}
