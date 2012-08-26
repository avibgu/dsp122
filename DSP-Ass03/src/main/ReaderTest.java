package main;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Vector;

import org.junit.Test;

import data.WordsPair;

import utilities.Reader;

public class ReaderTest {

	@Test
	public void test() {

		Reader reader = new Reader(
				"https://s3.amazonaws.com/dsp122-batel-avi-ass03/train/relation-1-train.txt");

		// try {
		// // Create a URL for the desired page
		// URL url = new URL(
		// "https://s3.amazonaws.com/dsp122-batel-avi-ass03/train/relation-1-train.txt");
		//
		// BufferedReader in = new BufferedReader(new InputStreamReader(
		// url.openStream()));
		//
		// String str = "";
		//
		// while ((str = in.readLine()) != null)
		// System.out.println(str);
		//
		// in.close();
		// } catch (Exception e) {
		// }

		Vector<WordsPair> mTrainingPairs = null;

		try {
			mTrainingPairs = reader
					.readWordPairs(new URL(
							"https://s3.amazonaws.com/dsp122-batel-avi-ass03/train/relation-1-train.txt"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (WordsPair wp : mTrainingPairs)
			System.out.println(wp.getW1() + "\t" + wp.getW2());
	}
}
