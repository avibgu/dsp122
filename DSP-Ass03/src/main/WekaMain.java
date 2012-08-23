package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import data.Global;

import step2.Step2;
import step6.Step6;
import utilities.FileManipulator;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class WekaMain {

	protected static FastVector attributes;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		final File trainFolder = new File("output-train");
		final File testFolder = new File("output-test");

		AmazonS3 mAmazonS3 = new AmazonS3Client(
				new PropertiesCredentials(WekaMain.class
						.getResourceAsStream("AwsCredentials.properties")));

		Set<String> trainSet = new HashSet<String>();
		Set<String> testSet = new HashSet<String>();

		for (S3ObjectSummary objectSummary : mAmazonS3.listObjects(
				Global.BUCKET_NAME).getObjectSummaries()) {

			String key = objectSummary.getKey();

			if (key.startsWith("output-train"))
				trainSet.add(key);

			else if (key.startsWith("output-test"))
				testSet.add(key);
		}

		Vector<String> trainLines = readFilesFromFolder(mAmazonS3, trainSet);
		Vector<String> testLines = readFilesFromFolder(mAmazonS3, trainSet);

		int clusterSize = trainLines.get(0).split(",").length - 1;

		Instances isTrainingSet = createFeatureVector(trainLines, clusterSize);
		Instances isTestingSet = createFeatureVector(testLines, clusterSize);

		// Create a naive bayes classifier
		Classifier cModel = (Classifier) new NaiveBayes();
		cModel.buildClassifier(isTrainingSet);

		// Test the model
		Evaluation eTest = new Evaluation(isTrainingSet);
		eTest.evaluateModel(cModel, isTestingSet);

		// Print the result a la Weka explorer:
		double accuracy = eTest.pctCorrect();
		String statistics = eTest.toClassDetailsString("Statistics")
				+ "\nAccuracy: " + accuracy;
		System.out.println(statistics);

		// String strSummary = eTest.toSummaryString();
		// System.out.println(strSummary);

	}

	private static Vector<String> readFilesFromFolder(AmazonS3 mAmazonS3,
			Set<String> fileNames) {

		Vector<String> result = new Vector<String>();

		for (String fileName : fileNames) {

			if (!fileName.equals(".svn")) {

				Vector<String> lines = FileManipulator.readFromInputStream(
						mAmazonS3.getObject(Global.BUCKET_NAME, "totalCounter")
								.getObjectContent(), false);

				result.addAll(lines);
			}
		}

		return result;
	}

	private static Instances createFeatureVector(Vector<String> lines, int size) {

		// 1. set up attributes
		attributes = new FastVector();

		for (int i = 0; i < size; i++)
			attributes.addElement(new Attribute("Hits" + i));

		FastVector fvClassVal = new FastVector(2);
		fvClassVal.addElement("true");
		fvClassVal.addElement("false");
		Attribute classAttribute = new Attribute("Nominals", fvClassVal);

		attributes.addElement(classAttribute);

		// 2. create Instances object
		Instances data = new Instances("HITS Vecotrs", attributes, lines.size());

		data.setClassIndex(attributes.size() - 1);

		// 3. fill with data

		for (String line : lines) {

			Instance featureVector = new Instance(size + 1);
			String[] splitted = line.split(",");

			for (int i = 0; i < splitted.length - 1; i++)
				featureVector.setValue((Attribute) attributes.elementAt(i),
						Double.parseDouble(splitted[i]));

			featureVector.setValue((Attribute) attributes.elementAt(size),
					(String) splitted[splitted.length - 1]);

			data.add(featureVector);

		}

		return data;
	}

	public static Vector<String> listFilesForFolder(final File folder) {

		Vector<String> fileNames = new Vector<String>();

		for (final File fileEntry : folder.listFiles())
			fileNames.add(fileEntry.getName());

		return fileNames;
	}

}
