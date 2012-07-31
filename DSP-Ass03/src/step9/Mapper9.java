package step9;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Mapper;

import data.Cluster;

public class Mapper9 extends Mapper<Cluster, Cluster, Cluster, Cluster> {

	protected void map(Cluster cluster1, Cluster cluster2, Context context)
			throws IOException, InterruptedException {
		
		if (null == cluster2)
			context.write(cluster1, null);
		
		else if (cluster1.getId().compareTo(cluster2.getId()) < 0)
			context.write(cluster1, cluster2);
		
		else
			context.write(cluster2, cluster1);
	}
}
