package step5;

import java.io.IOException;

import org.apache.hadoop.mapreduce.Reducer;

import data.Cluster;
import data.Word;

public class Reducer5 extends Reducer<Cluster, Word, Word, Cluster> {

	protected void reduce(Cluster cluster, Iterable<Word> hookWords,
			Context context) throws IOException, InterruptedException {

		int size = 0;
		
		for (Word hookWord : hookWords){
			
			if (size++ > 1){
				context.write(hookWord, cluster);
				return;
			}					
		}
	};
}
