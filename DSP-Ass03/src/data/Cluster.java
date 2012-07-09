package data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;
import java.util.Vector;

import org.apache.hadoop.io.WritableComparable;

public class Cluster implements WritableComparable<Cluster> {

	protected String mId;

	protected Word mHookWord;
	protected Vector<Pattern> mCorePatters;
	protected Vector<Pattern> mUnconfirmedPatters;
	protected boolean mAllUnconfirmed;

	public Cluster() {
		mId = UUID.randomUUID().toString();
		mHookWord = null;
		mCorePatters = new Vector<Pattern>();
		mUnconfirmedPatters = new Vector<Pattern>();
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

		if (!mAllUnconfirmed)
			return 1;

		return size() - pO.size();
	}

	private int size() {
		return mCorePatters.size() + mUnconfirmedPatters.size();
	}

	public void add(Pattern pPattern) {
		
		if (pPattern.getType() == PatternType.UNCONFIRMED)
			mUnconfirmedPatters.add(pPattern);
		
		else
			mCorePatters.add(pPattern);
	}

	public int calcSharedPatternsPercents(Cluster pCluster) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void mergeClusters(Cluster pCluster1, Cluster pCluster2) {

		mCorePatters.clear();
		mUnconfirmedPatters.clear();
		mCorePatters.addAll(pCluster1.getCorePatters());
		mUnconfirmedPatters.addAll(pCluster2.getUnconfirmedPatters());
	}

	public Word getHookWord() {
		return mHookWord;
	}

	public void setHookWord(Word pHookWord) {
		mHookWord = pHookWord;
	}

	public boolean isAllUnconfirmed() {
		return mAllUnconfirmed;
	}

	public void setNotAllUnconformed() {

		if (mAllUnconfirmed) {

			mAllUnconfirmed = false;
			// TODO dec the num of clusters counter..
		}
	}
	
	public void mergeWithOtherClusterAndMarkCorePatterns(Cluster pCluster) {

		xxx(pCluster.getCorePatters());
		xxx(pCluster.getUnconfirmedPatters());
	}

	protected void xxx(Vector<Pattern> pPatterns) {
		
		for (Pattern pattern : pPatterns) {

			int index = mCorePatters.indexOf(pattern);

			if (-1 != index)
				continue;
			
			else{
				
				index = mUnconfirmedPatters.indexOf(pattern);
				
				if (-1 == index)
					add(pattern);
				
				else{
					
					Pattern tmpPattern = mUnconfirmedPatters.remove(index);
					
					tmpPattern.setType(PatternType.CORE);
					
					mCorePatters.add(tmpPattern);
				}
			}
		}
	}

	public String getId() {
		return mId;
	}

	public void setId(String pId) {
		mId = pId;
	}

	public Vector<Pattern> getCorePatters() {
		return mCorePatters;
	}

	public void setCorePatters(Vector<Pattern> pCorePatters) {
		mCorePatters = pCorePatters;
	}

	public Vector<Pattern> getUnconfirmedPatters() {
		return mUnconfirmedPatters;
	}

	public void setUnconfirmedPatters(Vector<Pattern> pUnconfirmedPatters) {
		mUnconfirmedPatters = pUnconfirmedPatters;
	}

	public void setAllUnconfirmed(boolean pAllUnconfirmed) {
		mAllUnconfirmed = pAllUnconfirmed;
	}

	public boolean areShareAllCorePatterns(Cluster pOtherCluster) {
		
		if (pOtherCluster.size() != size())
			return false;
		
		for (Pattern pattern : pOtherCluster.getCorePatters())
			if (!mCorePatters.contains(pattern))
				return false;
		
		return true;
	}
}
