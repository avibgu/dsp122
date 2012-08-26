package step56;

import java.io.IOException;

import javax.xml.soap.Text;

import org.apache.hadoop.mapreduce.Mapper;

import data.Cluster;

public class Mapper56 extends Mapper<Text, Cluster, Text, Cluster> {

	protected void map(Text hookWord, Cluster cluster, Context context)
			throws IOException, InterruptedException {
		context.write(hookWord, cluster);
	}
}
