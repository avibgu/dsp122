package data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;
import java.util.Vector;

import org.apache.hadoop.io.WritableComparable;

public class Cluster implements WritableComparable<Cluster> {

	protected String mId;

	protected String mHookWord;
	protected Vector<Pattern> mCorePatters;
	protected Vector<Pattern> mUnconfirmedPatters;

	protected boolean mMerged;

	public Cluster() {
		
		mId = UUID.randomUUID().toString();

		mHookWord = "";
		mCorePatters = new Vector<Pattern>();
		mUnconfirmedPatters = new Vector<Pattern>();

		mMerged = false;
	}

	@Override
	public void readFields(DataInput in) throws IOException {

		mId = in.readUTF();

		mHookWord = in.readUTF();

		int size = in.readInt();

		mCorePatters.clear();
		
		for (int i = 0; i < size; i++) {

			Pattern pattern = new Pattern();
			pattern.readFields(in);
			mCorePatters.add(pattern);
		}

		size = in.readInt();

		mUnconfirmedPatters.clear();
		
		for (int i = 0; i < size; i++) {

			Pattern pattern = new Pattern();
			pattern.readFields(in);
			mUnconfirmedPatters.add(pattern);
		}

		mMerged = in.readBoolean();
	}

	@Override
	public void write(DataOutput out) throws IOException {

		out.writeUTF(mId);

		out.writeUTF(mHookWord);

		out.writeInt(mCorePatters.size());

		for (Pattern pattern : mCorePatters)
			pattern.write(out);

		out.writeInt(mUnconfirmedPatters.size());

		for (Pattern pattern : mUnconfirmedPatters)
			pattern.write(out);

		out.writeBoolean(mMerged);
	}

	@Override
	public int compareTo(Cluster pO) {

		if (!isAllUnconfirmed())
			return 1;

		return size() - pO.size();
	}

	@Override
	public boolean equals(Object pObj) {

		if (!(pObj instanceof Cluster))
			return false;

		return mId.equals(((Cluster) pObj).getId());
	}

	@Override
	public int hashCode() {
		return mId.hashCode();
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

		int sharedPatterns = 0;

		for (Pattern pattern : pCluster.getCorePatters())
			if (mCorePatters.contains(pattern)
					|| mUnconfirmedPatters.contains(pattern))
				sharedPatterns++;

		for (Pattern pattern : pCluster.getUnconfirmedPatters())
			if (mCorePatters.contains(pattern)
					|| mUnconfirmedPatters.contains(pattern))
				sharedPatterns++;

		return (sharedPatterns / this.size()) * 100;
	}

	@Deprecated
	public void mergeClusters(Cluster pCluster1, Cluster pCluster2) {

		mCorePatters.clear();
		mUnconfirmedPatters.clear();
		mCorePatters.addAll(pCluster1.getCorePatters());
		mUnconfirmedPatters.addAll(pCluster2.getUnconfirmedPatters());

		if (null == mHookWord)
			mHookWord = pCluster1.getHookWord();

		if (null == mHookWord)
			mHookWord = pCluster2.getHookWord();
	}

	public void mergeWith(Cluster pCluster) {

		for (Pattern pattern : pCluster.getCorePatters())
			if (!mCorePatters.contains(pattern))
				mCorePatters.add(pattern);

		for (Pattern pattern : pCluster.getUnconfirmedPatters())
			if (!mUnconfirmedPatters.contains(pattern))
				mUnconfirmedPatters.add(pattern);
	}

	public String getHookWord() {
		return mHookWord;
	}

	public void setHookWord(String pHookWord) {
		mHookWord = pHookWord;
	}

	public boolean isAllUnconfirmed() {
		return mCorePatters.size() == 0;
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
	 * and unconfirmed patterns. HITS(C, (w1,w2)) = |{p; (w1,w2) appears in p
	 * belongs to Pcore}| /n + alfa |{p; (w1,w2) appears in p belongs to Punconf
	 * }| /m.
	 */

	public Double clacHits(WordsPair pWordsPair) {

		int appearsInPcore = 0;
		int appearsInPunconf = 0;

		for (Pattern pattern : mCorePatters)
			if (pattern.isWordsPairContained(pWordsPair.mW1, pWordsPair.mW2))
				appearsInPcore++;

		for (Pattern pattern : mUnconfirmedPatters)
			if (pattern.isWordsPairContained(pWordsPair.mW1, pWordsPair.mW2))
				appearsInPunconf++;

		double hit = 0.0;

		try {
			hit += appearsInPcore / mCorePatters.size();
		} catch (Exception e) {
		}

		try {
			hit += Global.alfa
					* (appearsInPunconf / mUnconfirmedPatters.size());
		} catch (Exception e) {
		}

		return hit;
	}

	public String getId() {
		return mId;
	}

	public void setId(String pId) {
		mId = pId;
	}

	// public Vector<Pattern> getAllPatterns() {
	//
	// Vector<Pattern> allPatterns = new Vector<Pattern>();
	// allPatterns.addAll(mCorePatters);
	// allPatterns.addAll(mUnconfirmedPatters);
	//
	// return allPatterns;
	// }

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

	public boolean areShareAllCorePatterns(Cluster pOtherCluster) {

		if (pOtherCluster.getCorePatters().size() != this.mCorePatters.size())
			return false;

		for (Pattern pattern : pOtherCluster.getCorePatters())
			if (!mCorePatters.contains(pattern))
				return false;

		return true;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {

		Cluster cluster = new Cluster();

		cluster.setId(mId);

		cluster.setHookWord(mHookWord);

		Vector<Pattern> patterns = new Vector<Pattern>();

		for (Pattern pattern : mCorePatters)
			patterns.add((Pattern) pattern.clone());

		cluster.setCorePatters(patterns);

		patterns = new Vector<Pattern>();

		for (Pattern pattern : mUnconfirmedPatters)
			patterns.add((Pattern) pattern.clone());

		cluster.setUnconfirmedPatters(patterns);

		return cluster;
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("\n" + mId + "\t" + mHookWord + "\n");

		for (Pattern pattern : mCorePatters)
			builder.append(pattern + "\n");

		for (Pattern pattern : mUnconfirmedPatters)
			builder.append(pattern + "\n");

		return builder.toString();
	}

	public void clear() {

		mId = UUID.randomUUID().toString();
		mHookWord = "";
		mCorePatters.clear();
		mUnconfirmedPatters.clear();
	}

	public boolean hasMerged() {
		return mMerged;
	}

	public void markAsMerged() {
		mMerged = true;
	}
}
