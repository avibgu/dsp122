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

import data.Global;

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

		String jobName = "nominals";

		try {
			jobName = args[0];
		} catch (Exception e) {
		}

		AmazonElasticMapReduce mapReduce = new AmazonElasticMapReduceClient(
				credentials);

		StepConfig debugConfig = new StepConfig().withName("debug")
				.withHadoopJarStep(new StepFactory().newEnableDebuggingStep())
				.withActionOnFailure("TERMINATE_JOB_FLOW");

		HadoopJarStepConfig hadoopJarStep1 = new HadoopJarStepConfig()
				.withJar("s3n://" + Global.BUCKET_NAME + "/step1.jar")
				.withMainClass("step1.Step1")
				.withArgs(Global.CORPUS_LOCATION,
						"s3n://" + Global.BUCKET_NAME + "/output1/");

		HadoopJarStepConfig hadoopJarStep2 = new HadoopJarStepConfig()
				.withJar("s3n://" + Global.BUCKET_NAME + "/step2.jar")
				.withMainClass("step2.Step2")
				.withArgs("s3n://" + Global.BUCKET_NAME + "/output1/",
						"s3n://" + Global.BUCKET_NAME + "/output2/");

		HadoopJarStepConfig hadoopJarStep3 = new HadoopJarStepConfig()
				.withJar("s3n://" + Global.BUCKET_NAME + "/step3.jar")
				.withMainClass("step3.Step3")
				.withArgs("s3n://" + Global.BUCKET_NAME + "/output2/",
						"s3n://" + Global.BUCKET_NAME + "/output3/");

		HadoopJarStepConfig hadoopJarStep4 = new HadoopJarStepConfig()
				.withJar("s3n://" + Global.BUCKET_NAME + "/step4.jar")
				.withMainClass("step4.Step4")
				.withArgs("s3n://" + Global.BUCKET_NAME + "/output3/",
						"s3n://" + Global.BUCKET_NAME + "/output4/");

		HadoopJarStepConfig hadoopJarStep4Text = new HadoopJarStepConfig()
				.withJar("s3n://" + Global.BUCKET_NAME + "/step4text.jar")
				.withMainClass("step4.Step4")
				.withArgs("s3n://" + Global.BUCKET_NAME + "/output3/",
						"s3n://" + Global.BUCKET_NAME + "/output4text/");

		HadoopJarStepConfig hadoopJarStep51 = new HadoopJarStepConfig()
				.withJar("s3n://" + Global.BUCKET_NAME + "/step51.jar")
				.withMainClass("step51.Step51")
				.withArgs("s3n://" + Global.BUCKET_NAME + "/output4/",
						"s3n://" + Global.BUCKET_NAME + "/output51/");

		HadoopJarStepConfig hadoopJarStep51Text = new HadoopJarStepConfig()
				.withJar("s3n://" + Global.BUCKET_NAME + "/step51text.jar")
				.withMainClass("step51.Step51")
				.withArgs("s3n://" + Global.BUCKET_NAME + "/output4/",
						"s3n://" + Global.BUCKET_NAME + "/output51text/");

		HadoopJarStepConfig hadoopJarStep52 = new HadoopJarStepConfig()
				.withJar("s3n://" + Global.BUCKET_NAME + "/step52.jar")
				.withMainClass("step52.Step52")
				.withArgs("s3n://" + Global.BUCKET_NAME + "/output51/",
						"s3n://" + Global.BUCKET_NAME + "/output52/");

		HadoopJarStepConfig hadoopJarStep6 = new HadoopJarStepConfig()
				.withJar("s3n://" + Global.BUCKET_NAME + "/step6.jar")
				.withMainClass("step6.Step6")
				.withArgs("s3n://" + Global.BUCKET_NAME + "/output52/",
						"s3n://" + Global.BUCKET_NAME + "/output6/");

		// HadoopJarStepConfig hadoopJarStep7 = new HadoopJarStepConfig()
		// .withJar("s3n://" + Global.BUCKET_NAME + "/step7.jar")
		// .withMainClass("step7.Step7")
		// .withArgs("s3n://" + Global.BUCKET_NAME + "/output6/",
		// "s3n://" + Global.BUCKET_NAME + "/output7/");

		HadoopJarStepConfig hadoopJarStep9train = new HadoopJarStepConfig()
				.withJar("s3n://" + Global.BUCKET_NAME + "/step9.jar")
				.withMainClass("step9.Step9")
				.withArgs("s3n://" + Global.BUCKET_NAME + "/output51/",
						"s3n://" + Global.BUCKET_NAME + "/outputTrain/", "0");

		HadoopJarStepConfig hadoopJarStep9test = new HadoopJarStepConfig()
				.withJar("s3n://" + Global.BUCKET_NAME + "/step9.jar")
				.withMainClass("step9.Step9")
				.withArgs("s3n://" + Global.BUCKET_NAME + "/output51/",
						"s3n://" + Global.BUCKET_NAME + "/outputTest/", "2");

		HadoopJarStepConfig hadoopJarStep9train2 = new HadoopJarStepConfig()
				.withJar("s3n://" + Global.BUCKET_NAME + "/step9.jar")
				.withMainClass("step9.Step9")
				.withArgs(
						"s3n://" + Global.BUCKET_NAME + "/output52-3/",
						"s3n://" + Global.BUCKET_NAME
								+ "/outputTrainWithMerge/", "0");

		HadoopJarStepConfig hadoopJarStep9test2 = new HadoopJarStepConfig()
				.withJar("s3n://" + Global.BUCKET_NAME + "/step9.jar")
				.withMainClass("step9.Step9")
				.withArgs(
						"s3n://" + Global.BUCKET_NAME + "/output52-3/",
						"s3n://" + Global.BUCKET_NAME + "/outputTestWithMerge/",
						"2");

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

		StepConfig step4TextConfig = new StepConfig().withName("step4text")
				.withHadoopJarStep(hadoopJarStep4Text)
				.withActionOnFailure("TERMINATE_JOB_FLOW");

		StepConfig step51Config = new StepConfig().withName("step51")
				.withHadoopJarStep(hadoopJarStep51)
				.withActionOnFailure("TERMINATE_JOB_FLOW");

		StepConfig step51TextConfig = new StepConfig().withName("step51text")
				.withHadoopJarStep(hadoopJarStep51Text)
				.withActionOnFailure("TERMINATE_JOB_FLOW");

		StepConfig step52Config = new StepConfig().withName("step52")
				.withHadoopJarStep(hadoopJarStep52)
				.withActionOnFailure("TERMINATE_JOB_FLOW");

		StepConfig step6Config = new StepConfig().withName("step6")
				.withHadoopJarStep(hadoopJarStep6)
				.withActionOnFailure("TERMINATE_JOB_FLOW");

		// StepConfig step7Config = new StepConfig().withName("step7")
		// .withHadoopJarStep(hadoopJarStep7)
		// .withActionOnFailure("TERMINATE_JOB_FLOW");

		StepConfig step9trainConfig = new StepConfig().withName("step9train")
				.withHadoopJarStep(hadoopJarStep9train)
				.withActionOnFailure("TERMINATE_JOB_FLOW");

		StepConfig step9testConfig = new StepConfig().withName("step9test")
				.withHadoopJarStep(hadoopJarStep9test)
				.withActionOnFailure("TERMINATE_JOB_FLOW");

		StepConfig step9trainConfig2 = new StepConfig().withName("step9train")
				.withHadoopJarStep(hadoopJarStep9train2)
				.withActionOnFailure("TERMINATE_JOB_FLOW");

		StepConfig step9testConfig2 = new StepConfig().withName("step9test")
				.withHadoopJarStep(hadoopJarStep9test2)
				.withActionOnFailure("TERMINATE_JOB_FLOW");

		JobFlowInstancesConfig instances = new JobFlowInstancesConfig()
				.withInstanceCount(Global.NUM_OF_INSTANCES)
				.withMasterInstanceType(InstanceType.M1Small.toString())
				.withSlaveInstanceType(InstanceType.M1Small.toString())
				.withHadoopVersion(Global.HADOOP_VERSION)
				.withEc2KeyName(Global.KEY_PAIR)
				.withKeepJobFlowAliveWhenNoSteps(false)
				.withPlacement(new PlacementType());

		RunJobFlowRequest runFlowRequest = new RunJobFlowRequest()
				.withName(jobName)
				.withInstances(instances)
				.withSteps(debugConfig, step1Config, step2Config, step3Config,
						step4Config, step51Config, step9trainConfig,
						step9testConfig, step4TextConfig, step51TextConfig
				// , step52Config, step6Config,
				// ,step7Config,
				// step9trainConfig2, step9testConfig2
				).withLogUri("s3n://" + Global.BUCKET_NAME + "/logs/");

		RunJobFlowResult runJobFlowResult = mapReduce
				.runJobFlow(runFlowRequest);

		String jobFlowId = runJobFlowResult.getJobFlowId();
		System.out.println("Ran job flow with id: " + jobFlowId);
	}
}
