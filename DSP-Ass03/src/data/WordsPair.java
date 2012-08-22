package data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class WordsPair implements WritableComparable<WordsPair>{

	protected String mW1;
	protected String mW2;
	protected String mPositivity;
	
	public WordsPair() {
		this("","","");
	}
	
	public WordsPair(String pW1, String pW2, String pPositivity) {
		mW1 = pW1;
		mW2 = pW2;
		mPositivity = pPositivity;
	}

	public void revertWords() {
		String tmp = mW2;
		mW2 = mW1;
		mW1 = tmp;
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		mW1 = in.readUTF();
		mW2 = in.readUTF();
		mPositivity = in.readUTF();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(mW1);
		out.writeUTF(mW2);
		out.writeUTF(mPositivity);
	}

	@Override
	public int compareTo(WordsPair pOther) {
		
		int result = mW1.compareTo(pOther.getW1());
		if(result != 0)	return result;
		
		result = mW2.compareTo(pOther.getW2());
		if(result != 0)	return result;
		
		return mPositivity.compareTo(pOther.getPositivity());
	}
	
	@Override
	public boolean equals(Object pObj) {
		
		if(! (pObj instanceof WordsPair))
			return false;
		
		return this.compareTo((WordsPair)pObj) == 0;
	}

	@Override
	public int hashCode() {
		return mW1.hashCode();
	}
	
	public String getW1() {
		return mW1;
	}

	public void setW1(String pW1) {
		mW1 = pW1;
	}

	public String getW2() {
		return mW2;
	}

	public void setW2(String pW2) {
		mW2 = pW2;
	}

	public String getPositivity() {
		return mPositivity;
	}

	public void setPositivity(String pPositivity) {
		mPositivity = pPositivity;
	}

}
