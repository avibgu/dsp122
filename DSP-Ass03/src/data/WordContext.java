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

		for (int i = 0; i < Global.CONTEXT_LENGTH; i++)
			mContext[i].readFields(in);

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
	public int compareTo(WordContext other) {
		
		int result = mContext[0].compareTo(other.getWordAt(0));
		if (0 != result) return result;
				
		result = mContext[1].compareTo(other.getWordAt(1));
		if (0 != result) return result;
		
		result = mContext[2].compareTo(other.getWordAt(2));
		if (0 != result) return result;
		
		result = mContext[3].compareTo(other.getWordAt(3));
		if (0 != result) return result;
		
		result = mContext[4].compareTo(other.getWordAt(4));

		return result;
	}
	
	@Override
	public boolean equals(Object pObj) {

		if (!(pObj instanceof WordContext))
			return false;

		WordContext other = (WordContext) pObj;
		
		return mContext[0].equals(other.getWordAt(0)) &&
				mContext[1].equals(other.getWordAt(1)) &&
				mContext[2].equals(other.getWordAt(2)) &&
				mContext[3].equals(other.getWordAt(3)) &&
				mContext[4].equals(other.getWordAt(4));
	}
	
	public void setHookTargetCount(int pHookTargetCount) {
		mHookTargetCount = pHookTargetCount;
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
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();

		for (Word word : mContext)
			builder.append(word +"\n");

		builder.append(mNumOfOccurrences +"\t" + mHookTargetCount + "\n");
		
		return builder.toString();
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		
		WordContext wordContext = new WordContext();
		
		for (int i = 0; i < Global.CONTEXT_LENGTH; i++)
			wordContext.setWordAt(i, (Word)mContext[i].clone());

		wordContext.setNumOfOccurrences(mNumOfOccurrences);
		wordContext.setHookTargetCount(mHookTargetCount);
		
		return wordContext;
	}

	public void setWordAt(int i, Word word) {
		mContext[i] = word;
	}
}
