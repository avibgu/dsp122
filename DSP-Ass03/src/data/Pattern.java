package data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class Pattern implements WritableComparable<Pattern> {

	protected Word mPrefix;
	protected Word mInfix;
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

	public Word getInfix() {
		return mInfix;
	}

	public void setInfix(Word pInfix) {
		mInfix = pInfix;
	}

	public Word getPostfix() {
		return mPostfix;
	}

	public void setPostfix(Word pPostfix) {
		mPostfix = pPostfix;
	}
}
