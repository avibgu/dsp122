package step6;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Mapper;

import data.Cluster;
import data.Word;

public class Mapper6 extends Mapper<Word, Cluster, Cluster, Word> {

	protected void map(Word hookWord, Cluster cluster, Context context)
			throws IOException, InterruptedException {
		context.write(cluster, hookWord);
	}
}
