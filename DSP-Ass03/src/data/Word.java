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

		String[] splitted = in.readUTF().split("\t");

		mWord = splitted[0];
		mCount = Integer.valueOf(splitted[1]);

		if (splitted[2].equals("UNKNOWN"))
			mType = WordType.UNKNOWN;

		else if (splitted[2].equals("HOOK"))
			mType = WordType.HOOK;

		else if (splitted[2].equals("CW"))
			mType = WordType.CW;

		else if (splitted[2].equals("HFW"))
			mType = WordType.HFW;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(mWord + "\t" + mCount + "\t" + mType);
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
		return mWord.toString() + "\t" + mCount;
	}

	@Override
	public boolean equals(Object pObj) {

		if (!(pObj instanceof Word))
			return false;

		return this.compareTo((Word) pObj) == 0;
	}
}
