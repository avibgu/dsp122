package common.utilities;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import org.junit.*;

import common.utilities.FileManipulator;

import junit.framework.TestCase;


public class FileManipulatotTest {

	
	@Test
	public void convertSummaryFileToOutputFileTest() throws Exception{
		File summaryFile = new File("summary-test.txt");
		File outputFile = new File("test-output.txt");
		
		FileManipulator.convertSummaryFileToOutputFile(summaryFile, outputFile);
	}

}
