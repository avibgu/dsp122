package data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class Word implements WritableComparable<Word> {

	protected Text mWord;
	protected IntWritable mCount;
	protected WordType mType;
	
	public Word() {
		this("");
	}
	
	public Word(String pWord) {
		this(pWord, -1);
	}

	public Word(String pWord, int pCount) {
		mWord = new Text(pWord);
		mCount = new IntWritable(pCount);
		mType = WordType.UNKNOWN;
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
	public int compareTo(Word other) {
		// check implementation
		if(this.mWord == other.getWord())
			return 1;
		
		return 0;
	}

	public Text getWord() {
		return mWord;
	}

	public void setWord(Text pWord) {
		this.mWord = pWord;
	}
	
	public void setWord(String pWord) {
		this.mWord.set(pWord);
	}

	public IntWritable getCount() {
		return mCount;
	}

	public void setCount(IntWritable pCount) {
		this.mCount = pCount;
	}

	public void setCount(int pCount) {
		this.mCount.set(pCount);
	}

	public WordType getType() {
		return mType;
	}

	public void setType(WordType pType) {
		this.mType = pType;
	}

	@Override
	public String toString() {
		return mWord.toString() + "\t" + mCount.toString();
	}
	
	@Override
	public boolean equals(Object pObj) {
		
		if (!(pObj instanceof Word))
			return false;
		
		return this.compareTo((Word) pObj) == 0;
	}
}
