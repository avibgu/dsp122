package data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;


public class WordContext implements WritableComparable<WordContext> {

	private static final int CONTEXT_LENGTH = 5;

	protected Word[] mContext;
	protected int mNumOfOccurrences;
	protected int mHookTargetCount;

	public WordContext() {

		mContext = new Word[CONTEXT_LENGTH];

		for (int i = 0; i < CONTEXT_LENGTH; i++)
			mContext[i] = new Word();

		mHookTargetCount = 0;
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
	public int compareTo(WordContext pO) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void set(String word1, String word2, String word3, String word4, String word5){

		mContext[0].setWord(word1);
		mContext[1].setWord(word2);
		mContext[2].setWord(word3);
		mContext[3].setWord(word4);
		mContext[4].setWord(word5);
	}

	public void setNumOfOccurrences(int pCount) {
		mNumOfOccurrences = pCount;
	}

	public int getNumOfOccurrences() {
		return mNumOfOccurrences;
	}

	public Word getWordAt(int pIndex) {
		return mContext[pIndex];
	}

	public void setHookTargetCount(Integer pCount) {
		mHookTargetCount = pCount;
	}

	public Integer getHookTargetCount() {
		return mHookTargetCount;
	}
}
