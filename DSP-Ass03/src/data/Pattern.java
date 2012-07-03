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

	protected Word mHook;
	protected Word mTarget;

	public Pattern() {
		this(null, null, null, null, null, null, null);
	}

	public Pattern(Word pPrefix, Word pCW1, Word pInfix, Word pCW2,
			Word pPostfix, Word pHook, Word pTarget) {
		set(pPrefix, pCW1, pInfix, pCW2, pPostfix, pHook, pTarget);
	}

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

	public Word getHook() {
		return mHook;
	}

	public void setHook(Word pHook) {
		mHook = pHook;
	}

	public Word getTarget() {
		return mTarget;
	}

	public void setTarget(Word pTarget) {
		mTarget = pTarget;
	}

	public void set(String pPrefix, String pCW1, String pInfix, String pCW2,
			String pPostfix) {

		mPrefix.setWord(pPrefix);
		mCW1.setWord(pCW1);
		mInfix.setWord(pInfix);
		mCW2.setWord(pCW2);
		mPostfix.setWord(pPostfix);
	}

	public void set(Word pPrefix, Word pCW1, Word pInfix, Word pCW2,
			Word pPostfix, Word pHook, Word pTarget) {

		mPrefix = pPrefix;
		mCW1 = pCW1;
		mInfix = pInfix;
		mCW2 = pCW2;
		mPostfix = pPostfix;

		mHook = pHook;
		mTarget = pTarget;
	}

	public void set(Word pPrefix, Word pCW1, Word pInfix, Word pCW2,
			Word pPostfix) {
		set(pPrefix, pCW1, pInfix, pCW2, pPostfix, null, null);
	}
	
	public boolean isLegal() {

		if (	mPrefix.getType() == WordType.HFW &&
				mInfix.getType() == WordType.HFW &&
				mPostfix.getType() == WordType.HFW){
		
			if (mCW1.getType() == WordType.HOOK && mCW2.getType() == WordType.CW){

				setHook(mCW2);
				setTarget(mCW1);
				return true;
			}
			
			else if (mCW1.getType() == WordType.CW && mCW2.getType() == WordType.HOOK){
				
				setHook(mCW2);
				setTarget(mCW1);
				return true;
			}
		}
		
		return false;
	}
}
