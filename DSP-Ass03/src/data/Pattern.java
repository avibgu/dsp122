package data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.WritableComparable;

public class Pattern implements WritableComparable<Pattern> {

	protected String mPrefix;
	protected String mInfix;
	protected String mPostfix;

	protected List<String> mHooks;
	protected List<String> mTargets;

	protected PatternType mType;

	public Pattern() {

		mPrefix = "";
		mInfix = "";
		mPostfix = "";

		mType = PatternType.UNCONFIRMED;

		mHooks = new ArrayList<String>();
		mTargets = new ArrayList<String>();
	}

	public void set(PatternInstance pPatternInstance) {
		mPrefix = pPatternInstance.getPrefix().getWord();
		mInfix = pPatternInstance.getInfix().getWord();
		mPostfix = pPatternInstance.getPostfix().getWord();
	}

	@Override
	public void readFields(DataInput in) throws IOException {

		mPrefix = in.readUTF();
		mInfix = in.readUTF();
		mPostfix = in.readUTF();

		int size = in.readInt();

		for (int i = 0; i < size; i++)
			mHooks.add(in.readUTF());

		size = in.readInt();

		for (int i = 0; i < size; i++)
			mTargets.add(in.readUTF());

		mType = in.readBoolean() ? PatternType.CORE : PatternType.UNCONFIRMED;
	}

	@Override
	public void write(DataOutput out) throws IOException {

		out.writeUTF(mPrefix);
		out.writeUTF(mInfix);
		out.writeUTF(mPostfix);

		out.writeInt(mHooks.size());

		for (String hook : mHooks)
			out.writeUTF(hook);

		out.writeInt(mTargets.size());

		for (String target : mTargets)
			out.writeUTF(target);

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

		pattern.mPrefix = this.mInfix;
		pattern.mInfix = this.mInfix;
		pattern.mPostfix = this.mInfix;

		for (String hook : this.mHooks)
			pattern.mHooks.add(hook);

		for (String target : this.mTargets)
			pattern.mTargets.add(target);

		pattern.mType = this.mType;

		return pattern;
	}

	public void add(String pHook, String pTarget) {
		mHooks.add(pHook);
		mTargets.add(pTarget);
	}

	public List<String> getHookWords() {
		return mHooks;
	}

	public List<String> getTargets() {
		return mTargets;
	}

	public PatternType getType() {
		return mType;
	}

	public void setType(PatternType pType) {
		mType = pType;
	}

	public boolean isWordsPairContained(String pW1, String pW2) {

		for (int i = 0; i < mHooks.size(); i++) {

			if (mHooks.get(i).equals(pW1) && mTargets.get(i).equals(pW2))
				return true;

			else if (mHooks.get(i).equals(pW2) && mTargets.get(i).equals(pW1))
				return true;
		}

		return false;
	}
}
