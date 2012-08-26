package step7;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Mapper;

import data.Cluster;
import data.CorePatternsList;
import data.Word;

public class Mapper7 extends Mapper<Word, Cluster, CorePatternsList, Cluster> {

	protected CorePatternsList corePatternsList;
	
	@Override
	protected void setup(Context pContext)
			throws IOException, InterruptedException {
		corePatternsList = new CorePatternsList();
	}
	
	protected void map(Word hookWord, Cluster cluster, Context context)
			throws IOException, InterruptedException {
		
		corePatternsList.setCorePatterns(cluster.getCorePatters());
		context.write(corePatternsList, cluster);
	}
}
