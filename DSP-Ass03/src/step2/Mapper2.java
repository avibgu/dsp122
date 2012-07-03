package step2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import data.Word;

public class Mapper2 extends Mapper<LongWritable, Text, Text, Text> {

	public static final String HOOKs_LIST_HDFS_PATH_PREFIX = "/hooks/";

	protected Set<Word> mHooks;

	@Override
	public void setup(Context context) {

		mHooks = new HashSet<Word>();

		try {

			URI[] cacheFilesURI = DistributedCache.getCacheFiles(context
					.getConfiguration());

			for (URI cacheFileURI : cacheFilesURI) {

				if (cacheFileURI.getPath()
						.contains(HOOKs_LIST_HDFS_PATH_PREFIX)) {

					BufferedReader reader = new BufferedReader(
							new FileReader(new File(cacheFileURI)));

					try {

						String line = "";
						
						while ((line = reader.readLine()) != null)
							mHooks.add(new Word(line));
					}

					finally {
						reader.close();
					}
				}
			}
		}

		catch (IOException e) {
			System.err.println("Exception reading DistributedCache: " + e);
		}
	}

	protected void map(Text key, Text value, Context context)
			throws IOException, InterruptedException {

	}
}
