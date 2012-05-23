package recordreader;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

public class MyKeyValueTextInputFormat extends FileInputFormat<Text, Text> {

	@Override
	public RecordReader<Text, Text> createRecordReader(InputSplit pArg0,
			TaskAttemptContext pArg1) throws IOException, InterruptedException {
		
		// TODO Auto-generated method stub
		
		return null;
	}
}
