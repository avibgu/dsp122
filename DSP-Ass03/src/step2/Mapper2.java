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

import data.Pattern;
import data.Word;
import data.WordType;

public class Mapper2 extends Mapper<LongWritable, Text, Word, Pattern> {

	public static final String HOOKs_LIST_HDFS_PATH_PREFIX = "/hooks/";
	public static final String HFWs_LIST_HDFS_PATH_PREFIX = "/hfws/";

	protected Set<Word> mHooks;
	protected Set<Word> mHFWs;
	protected Word mWord;
	protected Pattern mPattern;

	@Override
	public void setup(Context context) {

		mHooks = new HashSet<Word>();
		mHFWs = new HashSet<Word>();
		mWord = new Word();
		mPattern = new Pattern();

		String[] splitted = null;
		Word word = null;		
		
		try {

			URI[] cacheFilesURI = DistributedCache.getCacheFiles(context
					.getConfiguration());

			for (URI cacheFileURI : cacheFilesURI) {

				if (cacheFileURI.getPath()
						.contains(HOOKs_LIST_HDFS_PATH_PREFIX)) {

					BufferedReader reader = new BufferedReader(new FileReader(
							new File(cacheFileURI)));

					try {

						String line = "";

						while ((line = reader.readLine()) != null){
							
							splitted = line.split("\t");
							word = new Word(splitted[0], Integer.parseInt(splitted[1]));
							word.setType(WordType.HOOK);
							mHooks.add(word);
						}
					}

					finally {
						reader.close();
					}
				}

				else if (cacheFileURI.getPath().contains(
						HFWs_LIST_HDFS_PATH_PREFIX)) {

					BufferedReader reader = new BufferedReader(new FileReader(
							new File(cacheFileURI)));

					try {

						String line = "";

						while ((line = reader.readLine()) != null){
							
							splitted = line.split("\t");
							word = new Word(splitted[0], Integer.parseInt(splitted[1]));
							word.setType(WordType.HFW);
							mHFWs.add(word);
						}
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

	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

		String[] splitted = value.toString().split("\t");

		if (splitted.length != 5)
			return;

		splitted = splitted[0].split(" ");

		if (splitted.length != 5)
			return;

		if (mHFWs.contains(splitted[0])
				&& mHFWs.contains(splitted[2])
				&& mHFWs.contains(splitted[4])){
			
			mPattern.set(splitted[0], splitted[1], splitted[2], splitted[3], splitted[4]);
			
			if (mHooks.contains(splitted[1])){
				mWord.setWord(splitted[1]);
				context.write(mWord, mPattern);
			}
			
			if (mHooks.contains(splitted[3])){
				mWord.setWord(splitted[3]);
				context.write(mWord, mPattern);
			}
		}
				
	}
}
