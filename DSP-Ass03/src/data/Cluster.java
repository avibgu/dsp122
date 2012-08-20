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

		Vector<Pattern> allPatterns = new Vector<Pattern>();
		allPatterns.addAll(pCluster.getCorePatters());
		allPatterns.addAll(pCluster.getUnconfirmedPatters());

		int sharedPatterns = 0;

		for (Pattern pattern : allPatterns)
			if (mCorePatters.contains(pattern)
					|| mUnconfirmedPatters.contains(pattern))
				sharedPatterns++;

		return (sharedPatterns / this.size()) * 100;
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
			// TODO dec the num of clusters counter.. ???? Who calls it?
		}
	}

	public void mergeWithOtherClusterAndMarkCorePatterns(Cluster pCluster) {

		mergeAndMark(pCluster.getCorePatters());
		mergeAndMark(pCluster.getUnconfirmedPatters());
	}

	protected void mergeAndMark(Vector<Pattern> pPatterns) {

		for (Pattern pattern : pPatterns) {

			int index = mCorePatters.indexOf(pattern);

			if (-1 != index)
				continue;

			else {

				index = mUnconfirmedPatters.indexOf(pattern);

				if (-1 == index)
					add(pattern);

				else {

					Pattern tmpPattern = mUnconfirmedPatters.remove(index);

					tmpPattern.setType(PatternType.CORE);

					mCorePatters.add(tmpPattern);
				}
			}
		}
	}

	/*
	 * n - core patterns size m - unconfirmed patterns size alfa - between
	 * (0..1), is a parameter that lets us modify the relative weight of core
	 * and unconfirmed patterns. 
	 * HITS(C, (w1,w2)) = |{p; (w1,w2) appears in p
	 * belongs to Pcore}| /n + alfa × |{p; (w1,w2) appears in p belongs to
	 * Punconf }| /m.
	 */

	public Double clacHits(WordsPair pWordsPair) {

		int appearsInPcore = 0;
		int appearsInPubconf = 0;

		for (Pattern pattern : mCorePatters)
			if (pattern.isWordContained(pWordsPair.mW1)
					&& pattern.isWordContained(pWordsPair.mW2))
				appearsInPcore++;

		for (Pattern pattern : mUnconfirmedPatters)
			if (pattern.isWordContained(pWordsPair.mW1)
					&& pattern.isWordContained(pWordsPair.mW2))
				appearsInPubconf++;

		double hit = (appearsInPcore / mCorePatters.size()) +
						Global.alfa * (appearsInPubconf / mUnconfirmedPatters.size());

		return hit;
	}

	public String getId() {
		return mId;
	}

	public void setId(String pId) {
		mId = pId;
	}

	public Vector<Pattern> getAllPatterns() {

		Vector<Pattern> allPatterns = new Vector<Pattern>();
		allPatterns.addAll(mCorePatters);
		allPatterns.addAll(mUnconfirmedPatters);

		return allPatterns;

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

		if (pOtherCluster.getCorePatters().size() != this.mCorePatters.size()) // originally:
																				// pOtherCluster.size()
																				// !=
																				// size()
			return false;

		for (Pattern pattern : pOtherCluster.getCorePatters())
			if (!mCorePatters.contains(pattern))
				return false;

		return true;
	}
}
