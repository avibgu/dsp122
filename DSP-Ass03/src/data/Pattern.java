package data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.WritableComparable;

public class Pattern implements WritableComparable<Pattern> {

	protected Word mPrefix;
	protected Word mInfix;
	protected Word mPostfix;

	protected List<Word[]> mHookTargetPairs;

	protected List<Word> mHooks;
	protected List<Word> mTargets;

	protected PatternType mType;

	public Pattern() {

		mPrefix = new Word();
		mInfix = new Word();
		mPostfix = new Word();

		mHookTargetPairs = new ArrayList<Word[]>();

		mHooks = new ArrayList<Word>();
		mTargets = new ArrayList<Word>();
	}

	public void set(PatternInstance pPatternInstance) {
		mPrefix = pPatternInstance.getPrefix();
		mInfix = pPatternInstance.getInfix();
		mPostfix = pPatternInstance.getPostfix();
	}

	@Override
	public void readFields(DataInput in) throws IOException {

		mPrefix.readFields(in);
		mInfix.readFields(in);
		mPostfix.readFields(in);

		int size = in.readInt();

		for (int i = 0; i < size; i++) {

			Word hook = new Word();
			hook.readFields(in);

			Word target = new Word();
			target.readFields(in);

			mHookTargetPairs.add(new Word[] { hook, target });
		}

		size = in.readInt();

		for (int i = 0; i < size; i++) {
			Word hook = new Word();
			hook.readFields(in);
			mHooks.add(hook);
		}

		size = in.readInt();

		for (int i = 0; i < size; i++) {
			Word target = new Word();
			target.readFields(in);
			mTargets.add(target);
		}

		mType = in.readBoolean() ? PatternType.CORE : PatternType.UNCONFIRMED;
	}

	@Override
	public void write(DataOutput out) throws IOException {

		mPrefix.write(out);
		mInfix.write(out);
		mPostfix.write(out);

		out.writeInt(mHookTargetPairs.size());

		for (Word[] pair : mHookTargetPairs) {
			pair[0].write(out);
			pair[1].write(out);
		}

		out.writeInt(mHooks.size());

		for (Word hook : mHooks)
			hook.write(out);

		out.writeInt(mTargets.size());

		for (Word target : mTargets)
			target.write(out);

		out.writeBoolean((PatternType.CORE == mType) ? true : false);
	}

	@Override
	public int compareTo(Pattern pOther) {

		int answer = mPrefix.compareTo(pOther.mPrefix);
		if (0 != answer)
			return answer;

		answer = mInfix.compareTo(pOther.mInfix);
		if (0 != answer)
			return answer;

		answer = mPostfix.compareTo(pOther.mPostfix);
		return answer;
	}

	@Override
	public boolean equals(Object pOther) {

		if (!(pOther instanceof Pattern))
			return false;

		return this.compareTo((Pattern) pOther) == 0;
	}

	@Override
	public int hashCode() {
		return mPrefix.hashCode() / 3 + mInfix.hashCode() / 3
				+ mPostfix.hashCode() / 3;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {

		Pattern pattern = new Pattern();

		pattern.mPrefix = (Word) this.mInfix.clone();
		pattern.mInfix = (Word) this.mInfix.clone();
		pattern.mPostfix = (Word) this.mInfix.clone();

		for (Word[] pair : this.mHookTargetPairs)
			pattern.mHookTargetPairs.add(new Word[] { (Word) pair[0].clone(),
					(Word) pair[0].clone() });

		for (Word hook : this.mHooks)
			pattern.mHooks.add((Word) hook.clone());

		for (Word target : this.mHooks)
			pattern.mTargets.add((Word) target.clone());

		pattern.mType = this.mType;

		return pattern;
	}

	public void add(Word pHook, Word pTarget) {

		mHookTargetPairs.add(new Word[] { pHook, pTarget });

		mHooks.add(pHook);
		mTargets.add(pTarget);
	}

	public List<Word> getHookWords() {
		return mHooks;
	}

	public List<Word> getTargets() {
		return mTargets;
	}

	public PatternType getType() {
		return mType;
	}

	public void setType(PatternType pType) {
		mType = pType;
	}

	public boolean isWordsPairContained(String pW1, String pW2) {

		for (Word[] pair : mHookTargetPairs) {

			if (pair[0].getWord().equals(pW1) && pair[1].getWord().equals(pW2))
				return true;

			else if (pair[0].getWord().equals(pW2)
					&& pair[1].getWord().equals(pW1))
				return true;
		}

		return false;
	}
}
