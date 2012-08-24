package step7;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Mapper;

import data.Cluster;
import data.CorePatternsList;
import data.Word;

public class Mapper7 extends Mapper<Word, Cluster, CorePatternsList, Cluster> {

	protected void map(Word hookWord, Cluster cluster, Context context)
			throws IOException, InterruptedException {
		
		CorePatternsList corePatterns = new CorePatternsList(cluster.getCorePatters());
		
		context.write(corePatterns, cluster);
	}
}
