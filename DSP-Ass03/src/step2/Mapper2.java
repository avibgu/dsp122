package step2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Mapper2 extends Mapper<Text, Text, Text, Text> {

	@Override
	public void setup(Context context) {
		try {
			Path[] cacheFiles = DistributedCache.getLocalCacheFiles(context
					.getConfiguration());

			if (cacheFiles != null && cacheFiles.length > 0) {

				String line;
				String[] tokens;

				BufferedReader joinReader = new BufferedReader(new FileReader(
						cacheFiles[0].toString()));

				try {

					while ((line = joinReader.readLine()) != null) {

						tokens = line.split(",", 2);
						// joinData.put(tokens[0], tokens[1]);
					}
				}

				finally {
					joinReader.close();
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
