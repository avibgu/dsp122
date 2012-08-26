package step6;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import data.Cluster;

public class Mapper6 extends Mapper<Text, Cluster, Cluster, Text> {

	protected void map(Text hookWord, Cluster cluster, Context context)
			throws IOException, InterruptedException {
		
		if(cluster.isAllUnconfirmed())
			context.getConfiguration().setBoolean("ClustersCounter", true);
		
		context.write(cluster, hookWord);
	}
}
