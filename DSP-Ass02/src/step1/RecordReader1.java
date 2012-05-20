package step1;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.util.LineReader;

public class RecordReader1 extends RecordReader<Text, Text> {
	// private static final Logger logger =
	// Logger.getLogger((LineRecordReader.class.getCanonicalName()));

	@Override
	public void initialize(InputSplit genericSplit, TaskAttemptContext context)
			throws IOException {
		
		FileSplit split = (FileSplit) genericSplit;
		Configuration job = context.getConfiguration();
		
		this.maxLineLength = job.getInt("mapred.linerecordreader.maxlength",
				Integer.MAX_VALUE);
		
		start = split.getStart();
		end = start + split.getLength();
		final Path file = split.getPath();
		compressionCodecs = new CompressionCodecFactory(job);
		final CompressionCodec codec = compressionCodecs.getCodec(file);

		// open the file and seek to the start of the split
		FileSystem fs = file.getFileSystem(job);
		FSDataInputStream fileIn = fs.open(split.getPath());
		boolean skipFirstLine = false;
		
		if (codec != null) {
			in = new LineReader(codec.createInputStream(fileIn), job);
			end = Long.MAX_VALUE;
		} else {
			if (start != 0) {
				skipFirstLine = true;
				--start;
				fileIn.seek(start);
			}
			in = new LineReader(fileIn, job);
		}
		if (skipFirstLine) { // skip first line and re-establish "start".
			start += in.readLine(new Text(), 0,
					(int) Math.min((long) Integer.MAX_VALUE, end - start));
		}
		this.pos = start;
	}

	@Override
	public boolean nextKeyValue() throws IOException {
		
		if (key == null) {
			key = new LongWritable();
		}
		
		key.set(pos);
		
		if (value == null) {
			value = new Text();
		}
	
		int newSize = 0;
		
		while (pos < end) {
			newSize = in.readLine(value, maxLineLength,
					Math.max((int) Math.min(Integer.MAX_VALUE, end - pos),
							maxLineLength));
			if (newSize == 0) {
				break;
			}
			pos += newSize;
			if (newSize < maxLineLength) {
				break;
			}
			// line too long. try again
			// logger.info("Skipped line of size " + newSize + " at pos " + (pos
			// - newSize));
		}
		if (newSize == 0) {
			key = null;
			value = null;
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Text getCurrentKey() {
		try {
			tempValue = value.toString();
			return new Text(value.toString().split("\t")[2]);
		} catch (Exception ex) {
			return value;
		}
	}

	@Override
	public Text getCurrentValue() {
		try {
			String[] tmp = value.toString().split("\t");
			
			String tmp2 = tmp[0] + "\t" + tmp[1] + "\t" + tmp[3] + "\t" + tmp[4];
			
			return new Text(tmp2);
		} catch (Exception ex) {
			return nullText;
		}
	}

	/**
	 * Get the progress within the split
	 */
	@Override
	public float getProgress() {
		if (start == end) {
			return 0.0f;
		} else {
			return Math.min(1.0f, (pos - start) / (float) (end - start));
		}
	}

	@Override
	public synchronized void close() throws IOException {
		if (in != null) {
			in.close();
		}
	}

	private CompressionCodecFactory compressionCodecs = null;
	private long start;
	private long pos;
	private String tempValue;
	private long end;
	private LineReader in;
	private int maxLineLength;
	private LongWritable key = null;
	private Text value = null;
	private final Text nullText = new Text("");

}