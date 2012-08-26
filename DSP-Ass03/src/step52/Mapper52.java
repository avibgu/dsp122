package step52;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import data.Cluster;

public class Mapper52 extends Mapper<Text, Cluster, Text, Cluster> {

	protected void map(Text hookWord, Cluster cluster, Context context)
			throws IOException, InterruptedException {
		context.write(hookWord, cluster);
	}
}
