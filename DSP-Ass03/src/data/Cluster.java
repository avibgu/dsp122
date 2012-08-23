package data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.UUID;
import java.util.Vector;

import org.apache.hadoop.io.WritableComparable;

public class Cluster implements WritableComparable<Cluster> {

	public static int idgen = 0;
	
	protected String mId;

	protected Word mHookWord;
	protected Vector<Pattern> mCorePatters;
	protected Vector<Pattern> mUnconfirmedPatters;

	public Cluster() {
//		mId = UUID.randomUUID().toString();
		
		mId = String.valueOf(idgen++);
		
		mHookWord = new Word();
		mCorePatters = new Vector<Pattern>();
		mUnconfirmedPatters = new Vector<Pattern>();
	}

	@Override
	public void readFields(DataInput in) throws IOException {

		mId = in.readUTF();

		mHookWord.readFields(in);

		int size = in.readInt();

		for (int i = 0; i < size; i++) {

			Pattern pattern = new Pattern();
			pattern.readFields(in);
			mCorePatters.add(pattern);
		}

		size = in.readInt();

		for (int i = 0; i < size; i++) {

			Pattern pattern = new Pattern();
			pattern.readFields(in);
			mUnconfirmedPatters.add(pattern);
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {

		out.writeUTF(mId);

		mHookWord.write(out);

		out.writeInt(mCorePatters.size());

		for (Pattern pattern : mCorePatters)
			pattern.write(out);

		out.writeInt(mUnconfirmedPatters.size());

		for (Pattern pattern : mUnconfirmedPatters)
			pattern.write(out);
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
		
		return mId.equals(((Cluster)pObj).getId());
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
		
		if (null == mHookWord)
			mHookWord = pCluster1.getHookWord();
		
		if (null == mHookWord)
			mHookWord = pCluster2.getHookWord();
	}

	public Word getHookWord() {
		return mHookWord;
	}

	public void setHookWord(Word pHookWord) {
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
	 * belongs to Pcore}| /n + alfa |{p; (w1,w2) appears in p belongs to
	 * Punconf }| /m.
	 */

	public Double clacHits(WordsPair pWordsPair) {

		int appearsInPcore = 0;
		int appearsInPubconf = 0;

		for (Pattern pattern : mCorePatters)
			if (pattern.isWordsPairContained(pWordsPair.mW1, pWordsPair.mW2))
				appearsInPcore++;

		for (Pattern pattern : mUnconfirmedPatters)
			if (pattern.isWordsPairContained(pWordsPair.mW1, pWordsPair.mW2))
				appearsInPubconf++;

		double hit = (appearsInPcore / mCorePatters.size()) + Global.alfa
				* (appearsInPubconf / mUnconfirmedPatters.size());

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

	public boolean areShareAllCorePatterns(Cluster pOtherCluster) {

		// originally: pOtherCluster.size() != size()
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
}
