package main;

import java.io.DataInput;
import java.io.FileReader;

import data.WordType;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;

import weka.core.converters.ConverterUtils.DataSource;

public class WEKATestAvi {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

//		 test();

		fileCreator();
	}

	protected static void test() throws Exception {
		Instances train = new DataSource("train/relation-1-train.txt")
				.getDataSet();

		Instances test = new DataSource("test/relation-1-test.txt")
				.getDataSet();

		if (train.classIndex() == -1)
			train.setClassIndex(train.numAttributes() - 1);

		if (test.classIndex() == -1)
			test.setClassIndex(test.numAttributes() - 1);

		// train classifier
		Classifier cls = new J48();
		String[] options = new String[] { "-U" };
		cls.setOptions(options); // set the options
		cls.buildClassifier(train);

		// evaluate classifier and print some statistics
		Evaluation eval = new Evaluation(train);
		eval.evaluateModel(cls, test);
		System.out.println(eval.toSummaryString("\nResults\n======\n", false));
	}

	public static void fileCreator() {

		// 1. set up attributes
		FastVector atts = new FastVector();

		atts.addElement(new Attribute("hits1"));
		atts.addElement(new Attribute("hits2"));
		atts.addElement(new Attribute("hits3"));
		atts.addElement(new Attribute("hits4"));

		FastVector fvClassVal = new FastVector(2);
		fvClassVal.addElement("positive");
		fvClassVal.addElement("negative");
		Attribute classAttribute = new Attribute("theClass", fvClassVal);

		atts.addElement(classAttribute);

		// 2. create Instances object
		Instances data = new Instances("HITS Vecotrs", atts, 10);

		data.setClassIndex(4);

		// 3. fill with data

		// Create the instance
		Instance iExample = new Instance(5);
		iExample.setValue((Attribute) atts.elementAt(0), 1.0);
		iExample.setValue((Attribute) atts.elementAt(1), 0.5);
		iExample.setValue((Attribute) atts.elementAt(2), 17);
		iExample.setValue((Attribute) atts.elementAt(3), 7);
		iExample.setValue((Attribute) atts.elementAt(4), "positive");

		// add the instance
		data.add(iExample);

		// 4. output data
		System.out.println(data);
	}
}