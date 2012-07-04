package data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Vector;

import org.apache.hadoop.io.WritableComparable;

public class Cluster implements WritableComparable<Cluster> {

	protected Word mHookWord;
	protected Vector<Pattern> mPatters;
	private boolean mAllUnconfirmed;
	
	public Cluster() {
		mHookWord = null;
		mPatters = new Vector<Pattern>();
		mAllUnconfirmed = true;
	}
	
	@Override
	public void readFields(DataInput pArg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(DataOutput pArg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public int compareTo(Cluster pO) {
		// TODO check if the order is ok..
		
		if (!mAllUnconfirmed)
			return 1;
		
		return mPatters.size() - pO.getPatters().size();
	}

	public void add(Pattern pPattern) {
		mPatters.add(pPattern);
	}

	public int calcSharedPatternsPercents(Cluster pCluster) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void mergeClusters(Cluster pCluster1, Cluster pCluster2) {
		mPatters.clear();
		mPatters.addAll(pCluster1.getPatters());
		mPatters.addAll(pCluster2.getPatters());
	}

	public Word getHookWord() {
		return mHookWord;
	}

	public void setHookWord(Word pHookWord) {
		mHookWord = pHookWord;
	}

	public Vector<Pattern> getPatters() {
		return mPatters;
	}

	public void setPatters(Vector<Pattern> pPatters) {
		mPatters = pPatters;
	}

	public boolean isAllUnconfirmed() {
		return mAllUnconfirmed;
	}

	public void setNotAllUnconformed(){
		
		if (mAllUnconfirmed){
		
			mAllUnconfirmed = false;
			//TODO dec the num of clusters counter..
		}
	}

	public boolean areSharedAllCorePatterns(Cluster pCluster) {
		// TODO Auto-generated method stub
		return true;
	}

	public void mergeWithOtherClusterAndMarkCorePatterns(Cluster pCluster) {
		
		for (Pattern pattern : pCluster.getPatters()){
			
			int index = mPatters.indexOf(pattern);
			
			if (-1 == index){
				mPatters.get(index).setType(PatternType.CORE);
				setNotAllUnconformed();
			}
			
			else
				mPatters.add(pattern);
		}
	}
}
