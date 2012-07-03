package data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class Word implements WritableComparable<Word> {

	protected Text mWord;
	protected IntWritable mCount;
	
	public Word() {
		mWord = new Text();
		mCount = new IntWritable(-1);
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
	public int compareTo(Word o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Text getWord() {
		return mWord;
	}

	public void setWord(Text pWord) {
		this.mWord = pWord;
	}
	
	public void setWord(String pWord) {
		this.mWord.set(pWord);
	}

	public IntWritable getCount() {
		return mCount;
	}

	public void setCount(IntWritable pCount) {
		this.mCount = pCount;
	}

}