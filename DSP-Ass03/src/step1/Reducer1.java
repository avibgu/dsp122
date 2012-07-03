package step1;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import data.Word;
import data.WordType;

public class Reducer1 extends Reducer<Text, Text, Text, Text> {

	public static final String HOOKs_LIST_HDFS_PATH_PREFIX = "/hooks/";
	public static final String HFWs_LIST_HDFS_PATH_PREFIX = "/hfws/";

	public static final String HOOKs_LIST_LOCAL_PATH_PREFIX = "/home/hadoop/hooks/";
	public static final String HFWs_LIST_LOCAL_PATH_PREFIX = "/home/hadoop/hfws/";

	protected static final int FH = 0;
	protected static final int FB = 0;
	protected static final int FC = 0;

	protected Set<Word> mHooks;
	protected Set<Word> mHFWs;

	protected void setup(Context context) throws IOException,
			InterruptedException {

		mHooks = new HashSet<Word>();
		mHFWs = new HashSet<Word>();
	}

	protected void reduce(Word word, Iterable<IntWritable> counts,
			Context context) throws IOException, InterruptedException {

		int sum = 0;

		for (IntWritable count : counts)
			sum += count.get();

		word.setCount(sum);

		if (sum > FH) {
			word.setType(WordType.HFW);
			mHFWs.add(word);
		}

		else if (sum < FC && sum > FB /* TODO && Counter... */) {
			word.setType(WordType.HOOK);
			mHooks.add(word);
		}
	}

	protected void cleanup(Context context) throws IOException,
			InterruptedException {

		Configuration conf = context.getConfiguration();

		URI hooksUri = createHooksFile(conf);
		URI hfwsUri = createHFWsFile(conf);

		DistributedCache.addCacheFile(hooksUri, conf);
		DistributedCache.addCacheFile(hfwsUri, conf);
	}

	protected URI createHooksFile(Configuration conf) throws IOException {

		String fileName = UUID.randomUUID() + ".txt";

		Path hdfsPath = new Path(HOOKs_LIST_HDFS_PATH_PREFIX + fileName);
		Path localPath = new Path(HOOKs_LIST_LOCAL_PATH_PREFIX + fileName);

		writeSetToFile(mHooks, localPath, hdfsPath, conf);

		return hdfsPath.toUri();
	}

	protected URI createHFWsFile(Configuration conf) throws IOException {

		String fileName = UUID.randomUUID() + ".txt";

		Path hdfsPath = new Path(HFWs_LIST_HDFS_PATH_PREFIX + fileName);
		Path localPath = new Path(HFWs_LIST_LOCAL_PATH_PREFIX + fileName);

		writeSetToFile(mHFWs, localPath, hdfsPath, conf);

		return hdfsPath.toUri();
	}

	private void writeSetToFile(Set<Word> wordsSet, Path localPath,
			Path hdfsPath, Configuration conf) throws IOException {

		DataOutputStream stream = FileSystem.get(localPath.toUri(), conf)
				.create(localPath, true);

		for (Word word : wordsSet)
			stream.writeBytes(word.toString() + "\n");

		// upload the file to hdfs. Overwrite any existing copy.

		FileSystem.get(conf)
				.copyFromLocalFile(false, true, localPath, hdfsPath);
	}
}
