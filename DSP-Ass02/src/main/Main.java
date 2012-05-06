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
				.withJar("s3n://yourbucket/yourfile.jar")
				// This should be a full map reduce application.
				.withMainClass("some.pack.MainClass")
				.withArgs("s3n://yourbucket/input/", "s3n://yourbucket/output/");

		StepConfig stepConfig = new StepConfig().withName("stepname")
				.withHadoopJarStep(hadoopJarStep)
				.withActionOnFailure("TERMINATE_JOB_FLOW");

		JobFlowInstancesConfig instances = new JobFlowInstancesConfig()
				.withInstanceCount(2)
				.withMasterInstanceType(InstanceType.M1Small.toString())
				.withSlaveInstanceType(InstanceType.M1Small.toString())
				.withHadoopVersion("0.20").withEc2KeyName("yourkey")
				.withKeepJobFlowAliveWhenNoSteps(false)
				.withPlacement(new PlacementType());

		RunJobFlowRequest runFlowRequest = new RunJobFlowRequest()
				.withName("jobname").withInstances(instances)
				.withSteps(stepConfig).withLogUri("s3n://yourbucket/logs/");

		RunJobFlowResult runJobFlowResult = mapReduce
				.runJobFlow(runFlowRequest);
		String jobFlowId = runJobFlowResult.getJobFlowId();
		System.out.println("Ran job flow with id: " + jobFlowId);
	}
}
