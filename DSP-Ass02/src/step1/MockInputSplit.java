package step1;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;

final class MockInputSplit extends InputSplit implements
		Writable {
	
	private String location;

	public MockInputSplit() {
	}

	public MockInputSplit(String location) {
		this.location = location;
	}

	@Override
	public String[] getLocations() throws IOException, InterruptedException {
		return new String[] { location };
	}

	@Override
	public long getLength() throws IOException, InterruptedException {
		return 10000000;
	}

	@Override
	public boolean equals(Object arg0) {
		return arg0 == this;
	}

	@Override
	public int hashCode() {
		return location.hashCode();
	}

	@Override
	public void readFields(DataInput arg0) throws IOException {
		location = arg0.readUTF();
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		arg0.writeUTF(location);
	}
}