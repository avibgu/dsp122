package data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.WritableComparable;

public class HookCorpus implements WritableComparable<HookCorpus> {

	protected ArrayList<Pattern> mCorpus;
	
	@Override
	public void readFields(DataInput arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int compareTo(HookCorpus o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
