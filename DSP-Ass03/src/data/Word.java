package data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class Word implements WritableComparable<Word> {

	protected String mWord;
	protected int mCount;
	protected WordType mType;

	public Word() {
		this("");
	}

	public Word(String pWord) {
		this(pWord, -1);
	}

	public Word(String pWord, int pCount) {
		mWord = pWord;
		mCount = pCount;
		mType = WordType.UNKNOWN;
	}

	@Override
	public void readFields(DataInput in) throws IOException {

		mWord = in.readUTF();
		
		mCount = in.readInt();
		
		int type = in.readInt();
		
		switch (type){
		
		case 0:
			mType = WordType.UNKNOWN;
			break;
			
		case 1:
			mType = WordType.HOOK;
			break;
			
		case 2:
			mType = WordType.CW;
			break;
			
		case 3:
			mType = WordType.HFW;
			break;
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {

		out.writeUTF(mWord);
		out.writeInt(mCount);
		
		if (mType == WordType.UNKNOWN)
			out.writeInt(0);

		else if (mType == WordType.HOOK)
			out.writeInt(1);

		else if (mType == WordType.CW)
			out.writeInt(2);

		else if (mType == WordType.HFW)
			out.writeInt(3);
	}

	@Override
	public int compareTo(Word other) {
		return this.mWord.compareTo(other.getWord());
	}

	public String getWord() {
		return mWord;
	}

	public void setWord(String pWord) {
		this.mWord = pWord;
	}

	public int getCount() {
		return mCount;
	}

	public void setCount(int pCount) {
		this.mCount = pCount;
	}

	public WordType getType() {
		return mType;
	}

	public void setType(WordType pType) {
		this.mType = pType;
	}

	@Override
	public String toString() {
		return mWord + "\t" + mCount +"\t" + mType;
	}

	@Override
	public boolean equals(Object pObj) {

		if (!(pObj instanceof Word))
			return false;

		return this.compareTo((Word) pObj) == 0;
	}
	
	@Override
	public int hashCode() {
		return mWord.hashCode();
	}
}
