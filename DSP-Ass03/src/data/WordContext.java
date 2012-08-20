package data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;


public class WordContext implements WritableComparable<WordContext> {

	protected Word[] mContext;
	protected int mNumOfOccurrences;
	protected int mHookTargetCount;

	public WordContext() {

		mContext = new Word[Global.CONTEXT_LENGTH];

		for (int i = 0; i < Global.CONTEXT_LENGTH; i++)
			mContext[i] = new Word();

		mNumOfOccurrences = 0;
		mHookTargetCount = 0;
	}

	@Override
	public void readFields(DataInput in) throws IOException {

		for (int i = 0; i < Global.CONTEXT_LENGTH; i++){
			mContext[i].readFields(in);
		}

		mNumOfOccurrences = in.readInt();
		mHookTargetCount = in.readInt();
	}

	@Override
	public void write(DataOutput out) throws IOException {

		for (Word word : mContext)
			word.write(out);

		out.writeInt(mNumOfOccurrences);
		out.writeInt(mHookTargetCount);
	}

	@Override
	public int compareTo(WordContext pO) {
		// TODO can we keep it?..
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
