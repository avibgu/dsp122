package step56;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Mapper;

import data.Cluster;
import data.Word;

public class Mapper56 extends Mapper<Word, Cluster, Word, Cluster> {

	protected void map(Word hookWord, Cluster cluster, Context context)
			throws IOException, InterruptedException {
		context.write(hookWord, cluster);
	}
}
