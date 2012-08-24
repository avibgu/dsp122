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
	public void readFields(DataInput arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int compareTo(CorePatternsList arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public Vector<Pattern> getCorePatterns() {
		return mCorePatterns;
	}

	public void setCorePatterns(Vector<Pattern> pCorePatterns) {
		this.mCorePatterns = pCorePatterns;
	}

}
