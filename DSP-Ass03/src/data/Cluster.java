package data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.io.WritableComparable;

public class Cluster implements WritableComparable<Cluster> {

	protected Word mHookWord;
	protected Set<Pattern> mPatters;
	
	public Cluster() {
		mHookWord = null;
		mPatters = new HashSet<Pattern>();
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
		// TODO Auto-generated method stub
		return 0;
	}

	public void add(Pattern pPattern) {
		mPatters.add(pPattern);
	}

	public int calcSharedPercents(Cluster pCluster) {
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

	public Set<Pattern> getPatters() {
		return mPatters;
	}

	public void setPatters(Set<Pattern> pPatters) {
		mPatters = pPatters;
	}

}
