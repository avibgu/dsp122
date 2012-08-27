package data;

public class Global {

	public static final int FH = 50;					//	10 - 100

	public static final int FB = 25;					//	1 - 50

	public static final int FC = 1000;				//	100 - 5000

	public static final long N = 500;					//	100 - 1000

	public static final int L = 25;					//	1/3 - 1/5

	public static final int S = 66;					//	2/3

	public final static double alfa = 0.05;			//	0.1 - 0.01

	public static final int CONTEXT_LENGTH = 5;		//	5

	public static final String KEY_PAIR = "batelKey";

	public static final String BUCKET_NAME = "dsp122-batel-avi-ass03";

	public static final String QUEUE_NAME = "TOTAL_COUNTER";
	
	public static final Integer NUM_OF_INSTANCES = 10;

	public static final String CORPUS_LOCATION = "s3n://" + Global.BUCKET_NAME + "/corpus";

	public static final String HADOOP_VERSION = "0.20";
}
