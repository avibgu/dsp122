package data;

public class Global {

	public static final int FH = 15;

	public static final int FB = 5;

	public static final int FC = 10;

	public static final long N = 100;

	public static final int L = 10;

	public static final int S = 10;

	public final static double alfa = 0.5;

	public static final int CONTEXT_LENGTH = 5;

	// public static final String KEY_PAIR = "AviKeyPair";
	public static final String KEY_PAIR = "batelKey";

	// Avi
	// public static final String BUCKET_NAME = "dsp122-avi-batel-ass03";

	// Batel
	public static final String BUCKET_NAME = "dsp122-batel-avi-ass03";

	public static final Integer NUM_OF_INSTANCES = 2;

	// private static final String CORPUS_LOCATION =
	// "s3://datasets.elasticmapreduce/ngrams/books/20090715/eng-1M/5gram/data";
	// private static final String CORPUS_LOCATION =
	// "s3://datasets.elasticmapreduce/ngrams/books/20090715/eng-us-all/5gram/data";
	// public static final String CORPUS_LOCATION =
	// "s3://datasets.elasticmapreduce/ngrams/books/20090715/eng-gb-all/5gram/data";

	public static final String CORPUS_LOCATION = "http://commondatastorage.googleapis.com/books/ngrams/books/googlebooks-eng-1M-5gram-20090715-0.csv.zip";

	public static final String HADOOP_VERSION = "0.20";
}
