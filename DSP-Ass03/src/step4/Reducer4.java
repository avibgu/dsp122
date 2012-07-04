package step4;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.mapreduce.Reducer;

import data.Cluster;
import data.Pattern;
import data.Word;

public class Reducer4 extends Reducer<Word, Pattern, Word, Cluster> {

	Map<Word, Set<Pattern>> mTargetToPatternsMap;

	protected void setup(Context context) throws IOException,
			InterruptedException {

		mTargetToPatternsMap = new HashMap<Word, Set<Pattern>>();
	};

	protected void reduce(Word hookWord, Iterable<Pattern> patterns,
			Context context) throws java.io.IOException, InterruptedException {

	};
}
