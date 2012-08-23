package step3;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.hadoop.mapreduce.Reducer;

import data.Global;
import data.PatternInstance;
import data.Word;

public class Reducer3 extends Reducer<Word, PatternInstance, Word, PatternInstance> {


	protected Set<PatternInstance> sortedPatternInstances;
	protected Set<PatternInstance> patternInstancesToRemove;

	protected void setup(Context context) throws IOException,
			InterruptedException {

		sortedPatternInstances = new TreeSet<PatternInstance>();
		patternInstancesToRemove = new HashSet<PatternInstance>();
	}

	protected void reduce(Word hookWord, Iterable<PatternInstance> patterns, Context context)
			throws IOException, InterruptedException {

		sortedPatternInstances.clear();
		patternInstancesToRemove.clear();
		
		for (PatternInstance pattern : patterns){
			
			try {
				sortedPatternInstances.add((PatternInstance)pattern.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}

		int filterIndex = sortedPatternInstances.size() * Global.L / 100;
		
		int i = -1;

		for (PatternInstance pattern : sortedPatternInstances){
		
			i++;
			
			if (i < filterIndex)
				patternInstancesToRemove.add(pattern);
			
			else if (i > sortedPatternInstances.size() - filterIndex)
				patternInstancesToRemove.add(pattern);	
		}
		
		for (PatternInstance pattern : sortedPatternInstances)
			if (!patternInstancesToRemove.contains(pattern))
				context.write(hookWord, pattern);
	}
}
