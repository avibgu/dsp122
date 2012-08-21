package data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class Pattern implements WritableComparable<Pattern> {

	protected Word mPrefix;
	protected Word mCW1;
	protected Word mInfix;
	protected Word mCW2;
	protected Word mPostfix;

	protected Word mHook;
	protected Word mTarget;

	protected Integer mHookTargetCount;

	protected Double mPMI;

	protected PatternType mType;

	public Pattern() {
		this(new Word(), new Word(), new Word(), new Word(), new Word(),
				new Word(), new Word(), 0);
	}

	public Pattern(Word pPrefix, Word pCW1, Word pInfix, Word pCW2,
			Word pPostfix, Word pHook, Word pTarget, Integer pHookTargetCount) {
		set(pPrefix, pCW1, pInfix, pCW2, pPostfix, pHook, pTarget,
				pHookTargetCount);
	}

	public boolean isLegal() {

		if (mPrefix.getType() == WordType.HFW
				&& mInfix.getType() == WordType.HFW
				&& mPostfix.getType() == WordType.HFW) {

			if (mCW1.getType() == WordType.HOOK
					&& mCW2.getType() == WordType.CW) {

				setHook(mCW1);
				setTarget(mCW2);
				return true;
			}

			else if (mCW1.getType() == WordType.CW
					&& mCW2.getType() == WordType.HOOK) {

				setHook(mCW2);
				setTarget(mCW1);
				return true;
			}
		}

		return false;
	}

	public void calcPMI(long pTotal) {
		mPMI = Math.log(mHookTargetCount) + Math.log(pTotal)
				- Math.log(mHook.getCount()) - Math.log(mTarget.getCount());
	}

	@Override
	public void readFields(DataInput in) throws IOException {

		mPrefix.readFields(in);
		mCW1.readFields(in);
		mInfix.readFields(in);
		mCW2.readFields(in);
		mPostfix.readFields(in);

		mHook.readFields(in);
		mTarget.readFields(in);

		mHookTargetCount = in.readInt();

		mPMI = in.readDouble();

		mType = in.readBoolean() ? PatternType.CORE : PatternType.UNCONFIRMED;
	}

	@Override
	public void write(DataOutput out) throws IOException {

		mPrefix.write(out);
		mCW1.write(out);
		mInfix.write(out);
		mCW2.write(out);
		mPostfix.write(out);

		mHook.write(out);
		mTarget.write(out);

		out.writeInt(mHookTargetCount);

		out.writeDouble(mPMI);

		out.writeBoolean((PatternType.CORE == mType) ? true : false);
	}

	@Override
	public int compareTo(Pattern o) {

		if (mPMI < o.getPMI())
			return -1;

		else if (mPMI > o.getPMI())
			return 1;

		else
			return 0;
	}

	@Override
	public int hashCode() {
		return mHook.hashCode();
	}

	@Override
	public boolean equals(Object pObj) {

		if (!(pObj instanceof Pattern))
			return false;

		Pattern other = (Pattern) pObj;

		return mPrefix.equals(other.mPrefix) && mCW1.equals(other.mCW1)
				&& mInfix.equals(other.mInfix) && mCW2.equals(other.mCW2)
				&& mPostfix.equals(other.mPostfix) && mHook.equals(other.mHook)
				&& mTarget.equals(other.mTarget)
				&& mHookTargetCount == other.mHookTargetCount
				&& mPMI == other.mPMI && mType == other.mType;
	}

	public boolean isWordContained(String strWord) {

		Word word = new Word(strWord);

		if (mPrefix.compareTo(word) == 1 || mCW1.compareTo(word) == 1
				|| mInfix.compareTo(word) == 1 || mCW2.compareTo(word) == 1
				|| mPostfix.compareTo(word) == 1)

			return true;

		else
			return false;

	}

	public Word getPrefix() {
		return mPrefix;
	}

	public void setPrefix(Word pPrefix) {
		mPrefix = pPrefix;
	}

	public Word getCW1() {
		return mCW1;
	}

	public void setCW1(Word pCW1) {
		mCW1 = pCW1;
	}

	public Word getInfix() {
		return mInfix;
	}

	public void setInfix(Word pInfix) {
		mInfix = pInfix;
	}

	public Word getCW2() {
		return mCW2;
	}

	public void setCW2(Word pCW2) {
		mCW2 = pCW2;
	}

	public Word getPostfix() {
		return mPostfix;
	}

	public void setPostfix(Word pPostfix) {
		mPostfix = pPostfix;
	}

	public Word getHook() {
		return mHook;
	}

	public void setHook(Word pHook) {
		mHook = pHook;
	}

	public Word getTarget() {
		return mTarget;
	}

	public void setTarget(Word pTarget) {
		mTarget = pTarget;
	}

	public Integer getHookTargetCount() {
		return mHookTargetCount;
	}

	public void setHookTargetCount(Integer pHookTargetCount) {
		mHookTargetCount = pHookTargetCount;
	}

	public Double getPMI() {
		return mPMI;
	}

	public void setPMI(Double pPMI) {
		mPMI = pPMI;
	}

	public PatternType getType() {
		return mType;
	}

	public void setType(PatternType pType) {
		mType = pType;
	}

	public void set(String pPrefix, String pCW1, String pInfix, String pCW2,
			String pPostfix) {

		mPrefix.setWord(pPrefix);
		mCW1.setWord(pCW1);
		mInfix.setWord(pInfix);
		mCW2.setWord(pCW2);
		mPostfix.setWord(pPostfix);
	}

	public void set(Word pPrefix, Word pCW1, Word pInfix, Word pCW2,
			Word pPostfix, Word pHook, Word pTarget, Integer pHookTargetCount) {

		mPrefix = pPrefix;
		mCW1 = pCW1;
		mInfix = pInfix;
		mCW2 = pCW2;
		mPostfix = pPostfix;

		mHook = pHook;
		mTarget = pTarget;

		mHookTargetCount = pHookTargetCount;

		mPMI = 0.0;

		mType = PatternType.UNCONFIRMED;
	}

	public void set(Word pPrefix, Word pCW1, Word pInfix, Word pCW2,
			Word pPostfix, Integer pHookTargetCount) {
		set(pPrefix, pCW1, pInfix, pCW2, pPostfix, null, null, pHookTargetCount);
	}

	@Override
	public String toString() {

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(mPrefix + "\t");
		stringBuilder.append(mCW1 + "\t");
		stringBuilder.append(mInfix + "\t");
		stringBuilder.append(mCW2 + "\t");
		stringBuilder.append(mPostfix + "\n");

		stringBuilder.append(mHook + "\t");
		stringBuilder.append(mTarget + "\n");

		stringBuilder.append(mHookTargetCount + "\t");
		stringBuilder.append(mPMI + "\t");
		stringBuilder.append(mType + "\n\n");

		return stringBuilder.toString();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {

		Pattern pattern = new Pattern((Word) mPrefix.clone(),
				(Word) mCW1.clone(), (Word) mInfix.clone(),
				(Word) mCW2.clone(), (Word) mPostfix.clone(),
				(Word) mHook.clone(), (Word) mTarget.clone(), mHookTargetCount);

		pattern.setPMI(mPMI);
		pattern.setType(mType);

		return pattern;
	}
}
