package step7;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Mapper;

import data.Cluster;
import data.Pattern;
import data.Word;

public class Mapper7 extends Mapper<Word, Cluster, Pattern, Cluster> {

	protected void map(Word hookWord, Cluster cluster, Context context)
			throws IOException, InterruptedException {
		
		for (Pattern corePattern : cluster.getCorePatters())
			context.write(corePattern, cluster);
	}
}
