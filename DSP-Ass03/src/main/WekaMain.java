package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Vector;

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

		Vector<String> trainLines = readFilesFromFolder(trainFolder);
		Vector<String> testLines = readFilesFromFolder(testFolder);

		int clusterSize = trainLines.get(0).split(",").length - 1;
		
		Instances isTrainingSet = createFeatureVector(trainLines, clusterSize-1);
		Instances isTestingSet = createFeatureVector(testLines, clusterSize-1);
		
		 // Create a naive bayes classifier 
		 Classifier cModel = (Classifier)new NaiveBayes();
		 cModel.buildClassifier(isTrainingSet);
		 
		// Test the model
		 Evaluation eTest = new Evaluation(isTrainingSet);
		 eTest.evaluateModel(cModel, isTestingSet);
		 
		// Print the result a la Weka explorer:
		 String strSummary = eTest.toSummaryString();
		 System.out.println(strSummary);
		 
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
		Instances data = new Instances("HITS Vecotrs", attributes, attributes.size());

		data.setClassIndex(attributes.size() - 1);

		// 3. fill with data

		for (String line : lines) {

			Instance featureVector = new Instance(size);
			String[] splitted = line.split(",");

			for (int i = 0; i < splitted.length - 1; i++)
				featureVector.setValue((Attribute) attributes.elementAt(i),
						Double.parseDouble(splitted[i]));

			featureVector.setValue((Attribute) attributes.elementAt(splitted.length - 1),
					splitted[splitted.length - 1]);

			data.add(featureVector);

		}

		return data;
	}
	
	private static Vector<String> readFilesFromFolder(File folder)
			throws FileNotFoundException {

		Vector<String> trainFileNames = listFilesForFolder(folder);

		Vector<String> result = new Vector<String>();

		for (String fileName : trainFileNames) {

			if(!fileName.equals(".svn")){
				
				File gf = new File(folder + "/" + fileName);
				InputStream fis = new FileInputStream(gf);
	
				Vector<String> lines = FileManipulator.readFromInputStream(fis, false);
	
				result.addAll(lines);
			}
		}

		return result;

	}

	public static Vector<String> listFilesForFolder(final File folder) {

		Vector<String> fileNames = new Vector<String>();

		for (final File fileEntry : folder.listFiles())
			fileNames.add(fileEntry.getName());

		return fileNames;
	}

}
