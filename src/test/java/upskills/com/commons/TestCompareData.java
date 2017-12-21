package upskills.com.commons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestCompareData {

	@Test
	public void test() {
//		ReaderUtils.compareData("MxG2K.txt", "MY3.txt", "DataStructure.csv", "D:\\ResultDataActual.txt", false);
//		
//		List<String> actualRows = readFile("D:\\ResultDataActual.txt");
//		List<String> expectedRows = readFile("D:\\ResultDataExpected.txt");
//		junit.framework.Assert.assertEquals(expectedRows, actualRows);
		
//		ReaderUtils.compareData("MxG2KFieldAndKey.txt", "MY3FieldAndKey.txt", "DataStructutr_FieldAndKey.csv", "D:\\ResultDataFieldAndKeyActual.txt", true);
//		
//		List<String> actualRows = readFile("D:\\ResultDataFieldAndKeyActual.txt");
//		List<String> expectedRows = readFile("D:\\ResultDataFieldAndKeyExpected.txt");
//		junit.framework.Assert.assertEquals(expectedRows, actualRows);
	}
	
	private List<String> readFile(String fileName) {
		List<String> resultList = new ArrayList<String>();
		BufferedReader reader = null;
        try {
        	reader = new BufferedReader(new FileReader(new File(fileName)));
            String line;
            while ((line = reader.readLine()) != null ) {
            	resultList.add(line);
            }
        }
        catch (Exception x) {
            x.printStackTrace();
        }
        finally {
        	if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
        }
		return resultList;
	}

}
