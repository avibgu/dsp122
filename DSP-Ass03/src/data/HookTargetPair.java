package data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class HookTargetPair implements WritableComparable<HookTargetPair> {

	protected String mHook;
	protected String mTarget;

	public HookTargetPair() {
		this("", "");
	}

	public HookTargetPair(String pHook, String pTarget) {
		set(pHook, pTarget);
	}
	
	public void set(String pHook, String pTarget) {
		mHook = pHook;
		mTarget = pTarget;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		mHook = in.readUTF();
		mTarget = in.readUTF();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(mHook);
		out.writeUTF(mTarget);
	}

	@Override
	public int compareTo(HookTargetPair pOther) {

		int result = this.mHook.compareTo(pOther.mHook);

		if (result != 0)
			return result;

		return this.mTarget.compareTo(pOther.mTarget);
	}

	@Override
	public boolean equals(Object pObj) {

		if (!(pObj instanceof HookTargetPair))
			return false;

		return this.compareTo((HookTargetPair) pObj) == 0;
	}

	@Override
	public int hashCode() {
		return mHook.hashCode() / 2 + mTarget.hashCode() / 2;
	}

	public String getHook() {
		return mHook;
	}

	public void setHook(String pHook) {
		mHook = pHook;
	}

	public String getTarget() {
		return mTarget;
	}

	public void setTarget(String pTarget) {
		mTarget = pTarget;
	}
	
	@Override
	public String toString() {
		return "<" + mHook + "," + mTarget + ">";
	}
}
