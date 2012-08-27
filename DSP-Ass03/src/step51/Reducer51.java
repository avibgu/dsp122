package step51;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import data.Cluster;
import data.HookTargetPair;
import data.Pattern;

public class Reducer51 extends Reducer<HookTargetPair, Pattern, Text, Cluster> {

	protected Cluster mCluster;
	protected Text mTextWord;
	
	@Override
	protected void setup(Context pContext)
			throws IOException, InterruptedException {
		 mCluster = new Cluster();
		 mTextWord = new Text();
	}
	
	protected void reduce(HookTargetPair hookTargetPair, Iterable<Pattern> patterns,
			Context context) throws IOException, InterruptedException {

		mCluster.clear();
				
		mCluster.setHookWord(hookTargetPair.getHook());
		
		for (Pattern pattern : patterns){

			try {
				mCluster.add((Pattern) pattern.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}

		mTextWord.set(mCluster.getHookWord());
		context.write(mTextWord, mCluster);
	};
}
