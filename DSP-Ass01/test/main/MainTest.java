package main;

import java.io.File;

import common.utilities.ImageManipulator;

public class MainTest {

	public static void main(String[] args) {
		
		ImageManipulator.cropFacesFromImage(new File("halici.jpg"));
		ImageManipulator.cropFacesFromImage(new File("nedStarck.jpg"));
		
	}
}
