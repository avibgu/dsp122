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
		
		int size = in.readInt(); 
		
		for(int i = 0; i < size; i++){
			
			Pattern pattern = new Pattern();
			pattern.readFields(in);
			mCorePatterns.add(pattern);
		}
	}

	@Override
	public void write(DataOutput out) throws IOException {
		
		out.writeInt(mCorePatterns.size());
		
		for(Pattern pattern : mCorePatterns)
			pattern.write(out);
	}
	
	//TODO: should be equals or contains??..
	@Override
	public int compareTo(CorePatternsList pOther) {

		int ans = this.mCorePatterns.size() - pOther.getCorePatterns().size();
		
		if (0 != ans)
			return ans;
	
		for(Pattern pattern : pOther.getCorePatterns())
			if (!mCorePatterns.contains(pattern))
				return -1;
		
		return 0;
	}
	
	@Override
	public boolean equals(Object pOther) {

		if (!(pOther instanceof CorePatternsList))
			return false;

		return this.compareTo((CorePatternsList) pOther) == 0;
	}

	@Override
	public int hashCode() {
		
		int hashCode = 0;
		int size = mCorePatterns.size();
		
		for(Pattern pattern : mCorePatterns)
			hashCode += pattern.hashCode() / size;
		
		return hashCode; 
	}
	
	public Vector<Pattern> getCorePatterns() {
		return mCorePatterns;
	}

	public void setCorePatterns(Vector<Pattern> pCorePatterns) {
		this.mCorePatterns = pCorePatterns;
	}
}
