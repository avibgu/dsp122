package recordreader;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.KeyValueLineRecordReader;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class MyKeyValueLineRecordReader extends RecordReader<Text, Text> {

	private KeyValueLineRecordReader lineReader;

	private Text lineKey;
	private Text lineValue;

	private Text key;
	private Text value;

	private MyKeyValueLineRecordReader() {
	}

	public static RecordReader<Text, Text> create(InputSplit split,
			TaskAttemptContext context) throws IOException,
			InterruptedException {

		RecordReader<Text, Text> ret = new MyKeyValueLineRecordReader();

		ret.initialize(split, context);

		return ret;
	}

	@Override
	public void initialize(InputSplit split, TaskAttemptContext context)
			throws IOException, InterruptedException {

		org.apache.hadoop.mapreduce.lib.input.FileSplit fs = (org.apache.hadoop.mapreduce.lib.input.FileSplit) split;

		lineReader = new KeyValueLineRecordReader(context.getConfiguration(),
				new org.apache.hadoop.mapred.FileSplit(fs.getPath(),
						fs.getStart(), fs.getLength(), fs.getLocations()));

		lineKey = lineReader.createKey();
		lineValue = lineReader.createValue();

		key = new Text();
		value = new Text();
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public Text getCurrentKey() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Text getCurrentValue() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getProgress() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

}
