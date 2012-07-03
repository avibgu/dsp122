package data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class Pattern implements WritableComparable<Pattern> {

	protected Word mPrefix;
	protected Word mCW1;
	protected Word mInfix;
	protected Word mCW2;
	protected Word mPostfix;
	
	@Override
	public void readFields(DataInput arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public int compareTo(Pattern o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Word getPrefix() {
		return mPrefix;
	}

	public void setPrefix(Word pPrefix) {
		mPrefix = pPrefix;
	}

	public Word getCW1() {
		return mCW1;
	}

	public void setCW1(Word pCW1) {
		mCW1 = pCW1;
	}

	public Word getInfix() {
		return mInfix;
	}

	public void setInfix(Word pInfix) {
		mInfix = pInfix;
	}

	public Word getCW2() {
		return mCW2;
	}

	public void setCW2(Word pCW2) {
		mCW2 = pCW2;
	}
	
	public Word getPostfix() {
		return mPostfix;
	}

	public void setPostfix(Word pPostfix) {
		mPostfix = pPostfix;
	}

	public void set(String pPrefix, String pCW1, String pInfix,
			String pCW2, String pPostfix) {
		
		mPrefix.setWord(pPrefix);
		mCW1.setWord(pCW1);
		mInfix.setWord(pInfix);
		mCW2.setWord(pCW2);
		mPostfix.setWord(pPostfix);
	}
}
