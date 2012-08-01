package main;

import java.io.FileReader;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;

public class WEKATest {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

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
	}
}