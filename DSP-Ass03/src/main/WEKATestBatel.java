package main;

import java.io.FileReader;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;

public class WEKATestBatel {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		FastVector fvNominalVal = new FastVector(3);
		fvNominalVal.addElement("1");
		fvNominalVal.addElement("2");
		fvNominalVal.addElement("3");
		Attribute Attribute1 = new Attribute("nominal", fvNominalVal);
		
		FastVector fvClassVal = new FastVector(2);
		 fvClassVal.addElement("positive");
		 fvClassVal.addElement("negative");
		Attribute ClassAttribute = new Attribute("the calass", fvClassVal);
		
		 FastVector fvWekaAttributes = new FastVector(2);
		 fvWekaAttributes.addElement(Attribute1);
		 fvWekaAttributes.addElement(ClassAttribute); 

		 // Create an empty training set
		 Instances isTrainingSet = new Instances("Rel", fvWekaAttributes, 1);           
		 // Set class index
		 isTrainingSet.setClassIndex(1);
		 
		 Instance iExample = new Instance(2);
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(0), 1.0);
		 iExample.setValue((Attribute)fvWekaAttributes.elementAt(1), "positive");
		// add the instance
		 isTrainingSet.add(iExample);
		 
		 // Create a naive bayes classifier 
		 Classifier cModel = (Classifier)new NaiveBayes();
		 cModel.buildClassifier(isTrainingSet);
		 
		// Test the model
		 Evaluation eTest = new Evaluation(isTrainingSet);
		 eTest.evaluateModel(cModel, isTrainingSet);
		 
		// Print the result a la Weka explorer:
		 String strSummary = eTest.toSummaryString();
		 System.out.println(strSummary);
		
		 /*
//		Instances train = new Instances(new FileReader("train/relation-1-train.txt")); // from somewhere
//		Instances test = new Instances(new FileReader("test/relation-1-test.txt")); // from somewhere
//		
//		// train classifier
//		Classifier cls = new J48();
//		cls.buildClassifier(train);
//		
//		// evaluate classifier and print some statistics
//		Evaluation eval = new Evaluation(train);
//		eval.evaluateModel(cls, test);
//		System.out.println(eval.toSummaryString("\nResults\n======\n", false));

		
		 FastVector      atts;
	     Instances       data;
	     double[]        vals;
	 
	     // 1. set up attributes
	     atts = new FastVector();

	     atts.addElement(new Attribute("att3", (FastVector) null));

	     // 2. create Instances object
	     data = new Instances("MyRelation", atts, 0);
	 
	     // 3. fill with data
	     
	     // first instance
	     vals = new double[data.numAttributes()];
	     vals[0] = data.attribute(0).addStringValue("This is a string!");
	     data.add(new Instance(1.0, vals));
	 
	     // second instance
	     vals = new double[data.numAttributes()];  // important: needs NEW array!
	     vals[0] = data.attribute(0).addStringValue("And another one!");
	     data.add(new Instance(1.0, vals));
	 
	     // 4. output data
	     System.out.println(data);
	     
	     */
	}
}