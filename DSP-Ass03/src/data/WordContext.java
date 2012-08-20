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

		// TODO: DEBUG
		System.out.println("Context Start");
		System.out.flush();
		
		for (int i = 0; i < Global.CONTEXT_LENGTH; i++){
			mContext[i].readFields(in);
			
			// TODO: DEBUG
			System.out.println(mContext[i]);
			System.out.flush();
		}

		mNumOfOccurrences = in.readInt();
		
		// TODO: DEBUG
		System.out.println(mNumOfOccurrences);
		System.out.flush();
		
		mHookTargetCount = in.readInt();
		
		// TODO: DEBUG
		System.out.println(mHookTargetCount);
		System.out.flush();
	}

	@Override
	public void write(DataOutput out) throws IOException {

		for (Word word : mContext)
			word.write(out);

		out.writeInt(mNumOfOccurrences);
		out.writeInt(mHookTargetCount);
	}

	@Override
	public int compareTo(WordContext other) {
		return mContext[0].compareTo(other.getWordAt(0));
	}
	
	@Override
	public boolean equals(Object pObj) {

		if (!(pObj instanceof WordContext))
			return false;

		return this.compareTo((WordContext) pObj) == 0;
	}
	
	@Override
	public int hashCode() {
		return mContext[0].hashCode();
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
