package data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class WordsPair implements WritableComparable<WordsPair>{

	protected String mW1;
	protected String mW2;
	
	public WordsPair(String pW1, String pW2) {
		mW1 = pW1;
		mW2 = pW2;
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
	public int compareTo(WordsPair pO) {
		// TODO Auto-generated method stub
		return 0;
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
}
