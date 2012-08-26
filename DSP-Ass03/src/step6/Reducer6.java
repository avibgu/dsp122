package step6;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import data.Cluster;
import data.Global;
import data.Word;

public class Reducer6 extends Reducer<Cluster, Text, Text, Cluster> {

	protected Cluster mCluster;
	protected Text mTextWord;

	protected void setup(Context context) throws IOException,
			InterruptedException {
		mCluster = null;
		mTextWord = new Text();
	};

	protected void reduce(Cluster cluster, Iterable<Word> hookWords,
			Context context) throws IOException, InterruptedException {

		if (null == mCluster) {
			mCluster = cluster;
			return;
		}

		if (mCluster.getHookWord().equals(cluster.getHookWord()))
			return;

		if (mCluster.calcSharedPatternsPercents(cluster) >= Global.S
				&& mCluster.areShareAllCorePatterns(cluster))
			mCluster.mergeWithOtherClusterAndMarkCorePatterns(cluster);
		
		else{
			mTextWord.set(cluster.getHookWord());
			context.write(mTextWord, cluster);
		}
	};

	protected void cleanup(Context context) throws IOException,
			InterruptedException {
		if (!mCluster.isAllUnconfirmed()){
			mTextWord.set(mCluster.getHookWord());
			context.write(mTextWord, mCluster);
		}
	};
}
