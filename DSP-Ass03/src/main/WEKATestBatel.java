package main;

import java.util.Vector;

import data.WordsPair;
import utilities.Reader;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;

public class WEKATestBatel {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		Reader reader = new Reader("try");
		
		Vector<WordsPair> wordPairs = reader.readWordPairs();
		
		for(WordsPair wp : wordPairs)
			System.out.println("e1= " + wp.getW1() + " e2 = " + wp.getW2() + " positivity= " + wp.getPositivity());
		
	
		FastVector fvNominalVal = new FastVector(3);
		fvNominalVal.addElement("1");
		fvNominalVal.addElement("2");
		fvNominalVal.addElement("3");
		Attribute Attribute1 = new Attribute("Boolean", fvNominalVal);
		
		FastVector fvNominalVal2 = new FastVector(3);
		fvNominalVal2.addElement("1");
		fvNominalVal2.addElement("2");
		fvNominalVal2.addElement("3");
		Attribute Attribute2 = new Attribute("Boolean", fvNominalVal2);
		
		FastVector fvClassVal = new FastVector(2);
		 fvClassVal.addElement("true");
		 fvClassVal.addElement("false");
		Attribute ClassAttribute = new Attribute("the class", fvClassVal);
		
		 FastVector fvWekaAttributes = new FastVector(3);
		 fvWekaAttributes.addElement(Attribute1);
		 fvWekaAttributes.addElement(Attribute2);
		 fvWekaAttributes.addElement(ClassAttribute); 

		 // Create an empty training set
		 Instances isTrainingSet = new Instances("Rel", fvWekaAttributes, 1);           
		 // Set class index
		 isTrainingSet.setClassIndex(2);
		 
		 Instance iExample = new Instance(2);
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(0), 1.0);
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(1), "true");
		// add the instance
		 isTrainingSet.add(iExample);
		 
		 // Create a naive bayes classifier 
		 Classifier cModel = (Classifier)new NaiveBayes();
		 cModel.buildClassifier(isTrainingSet);
		 
		// Create an empty training set
		 Instances isTestingSet = new Instances("Rel", fvWekaAttributes, 1);           
		 // Set class index
		 isTestingSet.setClassIndex(1);
		 
		 Instance iTestExample = new Instance(2);
		 iTestExample.setValue((Attribute)fvWekaAttributes.elementAt(0), 0.0);
		 iTestExample.setValue((Attribute)fvWekaAttributes.elementAt(1), "flase");
		// add the instance
		 isTestingSet.add(iTestExample);
		 
		// Test the model
		 Evaluation eTest = new Evaluation(isTrainingSet);
		 eTest.evaluateModel(cModel, isTestingSet);
		 
		// Print the result a la Weka explorer:
		 String strSummary = eTest.toSummaryString();
		 System.out.println(strSummary);
		 
	}
}