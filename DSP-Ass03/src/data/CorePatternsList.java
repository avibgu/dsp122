package data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Vector;

import org.apache.hadoop.io.WritableComparable;


public class CorePatternsList implements WritableComparable<CorePatternsList>{
	
	protected Vector<Pattern> mCorePatterns;
	
	public CorePatternsList(){
		mCorePatterns = new Vector<Pattern>();
	}

	public CorePatternsList(Vector<Pattern> pCorePatterns){
		this.mCorePatterns = pCorePatterns;
	} 

	@Override
	public void readFields(DataInput in) throws IOException {
		for(Pattern pattern : mCorePatterns)
			pattern.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		for(Pattern pattern : mCorePatterns)
			pattern.write(out);
	}

	@Override
	public int compareTo(CorePatternsList pOther) {

		int ans = 0;
		Vector<Pattern> other =  pOther.getCorePatterns();
		
		for(int i = 0; i < mCorePatterns.size() && ans == 0; i++)
			ans = other.get(i).compareTo(mCorePatterns.get(i));
		
		return ans;
	}
	
	@Override
	public boolean equals(Object pOther) {

		if (!(pOther instanceof CorePatternsList))
			return false;

		return this.compareTo((CorePatternsList) pOther) == 0;
	}

	@Override
	public int hashCode() {
		//TODO
		return 0;
	}
	
	public Vector<Pattern> getCorePatterns() {
		return mCorePatterns;
	}

	public void setCorePatterns(Vector<Pattern> pCorePatterns) {
		this.mCorePatterns = pCorePatterns;
	}

}
