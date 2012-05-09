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

public class Main {

	private static final String KEY_PAIR = "AviKeyPair";
	
	//TODO: init it if not exist..
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

		HadoopJarStepConfig hadoopJarStep = new HadoopJarStepConfig()
				.withJar("s3n://" + BUCKET_NAME + "/yourfile.jar")
				// This should be a full map reduce application.
				.withMainClass("some.pack.MainClass")
				.withArgs("s3n://" + BUCKET_NAME + "/input/",
						"s3n://" + BUCKET_NAME + "/output/");

		StepConfig stepConfig = new StepConfig().withName("stepname")
				.withHadoopJarStep(hadoopJarStep)
				.withActionOnFailure("TERMINATE_JOB_FLOW");

		JobFlowInstancesConfig instances = new JobFlowInstancesConfig()
				.withInstanceCount(2)
				.withMasterInstanceType(InstanceType.T1Micro.toString())
				.withSlaveInstanceType(InstanceType.T1Micro.toString())
				.withHadoopVersion("0.20").withEc2KeyName(KEY_PAIR)
				.withKeepJobFlowAliveWhenNoSteps(false)
				.withPlacement(new PlacementType());

		RunJobFlowRequest runFlowRequest = new RunJobFlowRequest()
				.withName("jobname").withInstances(instances)
				.withSteps(stepConfig)
				.withLogUri("s3n://" + BUCKET_NAME + "/logs/");

		RunJobFlowResult runJobFlowResult = mapReduce
				.runJobFlow(runFlowRequest);
		
		String jobFlowId = runJobFlowResult.getJobFlowId();
		System.out.println("Ran job flow with id: " + jobFlowId);
	}
}
