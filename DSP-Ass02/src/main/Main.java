package main;

import java.io.IOException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;

import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduce;
import com.amazonaws.services.elasticmapreduce.AmazonElasticMapReduceClient;
import com.amazonaws.services.elasticmapreduce.model.HadoopJarStepConfig;
import com.amazonaws.services.elasticmapreduce.model.JobFlowInstancesConfig;
import com.amazonaws.services.elasticmapreduce.model.PlacementType;
import com.amazonaws.services.elasticmapreduce.model.RunJobFlowRequest;
import com.amazonaws.services.elasticmapreduce.model.RunJobFlowResult;
import com.amazonaws.services.elasticmapreduce.model.StepConfig;
import com.amazonaws.services.elasticmapreduce.util.StepFactory;

public class Main {

	private static final String KEY_PAIR = "AviKeyPair";

	// TODO: init it if not exist..
	private static final String BUCKET_NAME = "dsp122-avi-batel-ass02";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		AWSCredentials credentials = null;

		try {

			credentials = new PropertiesCredentials(
					Main.class.getResourceAsStream("AwsCredentials.properties"));
		}

		catch (IOException e) {
			e.printStackTrace();
		}

		AmazonElasticMapReduce mapReduce = new AmazonElasticMapReduceClient(
				credentials);

		StepConfig debugConfig = new StepConfig().withName("debug")
				.withHadoopJarStep(new StepFactory().newEnableDebuggingStep())
				.withActionOnFailure("TERMINATE_JOB_FLOW");

		HadoopJarStepConfig hadoopJarStep1 = new HadoopJarStepConfig()
				.withJar("s3n://" + BUCKET_NAME + "/step1.jar")
				.withMainClass("step1.Step1")
				.withArgs(
						"s3://datasets.elasticmapreduce/ngrams/books/20090715/heb-all/5gram/data",
						// TODO
						// "s3n://dsp122/heb.corpus.10K",
						"s3n://" + BUCKET_NAME + "/output1/");

		HadoopJarStepConfig hadoopJarStep2 = new HadoopJarStepConfig()
				.withJar("s3n://" + BUCKET_NAME + "/step2.jar")
				.withMainClass("step2.Step2")
				.withArgs("s3n://" + BUCKET_NAME + "/output1/",
						"s3n://" + BUCKET_NAME + "/output2/");

		HadoopJarStepConfig hadoopJarStep3 = new HadoopJarStepConfig()
				.withJar("s3n://" + BUCKET_NAME + "/step3.jar")
				.withMainClass("step3.Step3")
				.withArgs("s3n://" + BUCKET_NAME + "/output2/",
						"s3n://" + BUCKET_NAME + "/output3/");

		HadoopJarStepConfig hadoopJarStep4 = new HadoopJarStepConfig()
				.withJar("s3n://" + BUCKET_NAME + "/step4.jar")
				.withMainClass("step4.Step4")
				.withArgs("s3n://" + BUCKET_NAME + "/output3/",
						"s3n://" + BUCKET_NAME + "/output4/", args[1]);

		StepConfig step1Config = new StepConfig().withName("step1")
				.withHadoopJarStep(hadoopJarStep1)
				.withActionOnFailure("TERMINATE_JOB_FLOW");

		StepConfig step2Config = new StepConfig().withName("step2")
				.withHadoopJarStep(hadoopJarStep2)
				.withActionOnFailure("TERMINATE_JOB_FLOW");

		StepConfig step3Config = new StepConfig().withName("step3")
				.withHadoopJarStep(hadoopJarStep3)
				.withActionOnFailure("TERMINATE_JOB_FLOW");

		StepConfig step4Config = new StepConfig().withName("step4")
				.withHadoopJarStep(hadoopJarStep4)
				.withActionOnFailure("TERMINATE_JOB_FLOW");

		JobFlowInstancesConfig instances = new JobFlowInstancesConfig()
				.withInstanceCount(20)
				.withMasterInstanceType(InstanceType.M1Small.toString())
				.withSlaveInstanceType(InstanceType.M1Small.toString())
				.withHadoopVersion("0.20").withEc2KeyName(KEY_PAIR)
				.withKeepJobFlowAliveWhenNoSteps(false)
				.withPlacement(new PlacementType());

		RunJobFlowRequest runFlowRequest = new RunJobFlowRequest()
				.withName(args[0])
				.withInstances(instances)
				.withSteps(debugConfig, step1Config, step2Config, step3Config,
						step4Config)
				.withLogUri("s3n://" + BUCKET_NAME + "/logs/");

		RunJobFlowResult runJobFlowResult = mapReduce
				.runJobFlow(runFlowRequest);

		String jobFlowId = runJobFlowResult.getJobFlowId();
		System.out.println("Ran job flow with id: " + jobFlowId);
	}
}
