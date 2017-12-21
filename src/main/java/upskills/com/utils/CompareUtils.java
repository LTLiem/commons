package upskills.com.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import upskills.com.model.DSMetaData;
import upskills.com.model.DataRow;
import upskills.com.model.CompareRequest;

/**
 * This class contains util methods for comparison
 * @author Thinh Vo
 *
 */
public final class CompareUtils {

    //private static final String RESULT_HEADER = "SourceName\tLineNumber\tKey Values\tOriginal Value";
    private static final Integer PAGE_SIZE = 100;
    
    public static void compareData(CompareRequest compareRequest) throws Exception {
    	DSMetaData dsMetaData = new DSMetaData();
        HashMap<String, DataRow> dataMap1 = new HashMap<String, DataRow>();
        HashMap<String, DataRow> dataMap2 = new HashMap<String, DataRow>();
        
        if (compareRequest.getIsFixedLengthData()) {
            dsMetaData = ReaderUtils.readMetaDataByFullStructure(compareRequest.getMetaDataFileName());
        } else {
            dsMetaData = ReaderUtils.readCSVMetaData(compareRequest.getMetaDataFileName());
        }

        List<String> resultData = new ArrayList<String>();
        resultData.add(buildHeader(null, dsMetaData, compareRequest));
        
        List<DataRow> dataRows1;
        List<DataRow> dataRows2;
        int dataCount1 = 0, dataCount2 = 0;
        BufferedReader reader1 = new BufferedReader(new FileReader(new File(compareRequest.getFileName1())));
        BufferedReader reader2 = new BufferedReader(new FileReader(new File(compareRequest.getFileName2())));
        do {
        	if (compareRequest.getIsFixedLengthData()) {
        		System.out.println("Read data for " + compareRequest.getDsName1());
                dataRows1 = ReaderUtils.readFixLengthDataSource(reader1, dsMetaData, compareRequest, PAGE_SIZE, dataCount1);
                System.out.println("Read data for " + compareRequest.getDsName2());
                dataRows2 = ReaderUtils.readFixLengthDataSource(reader2, dsMetaData, compareRequest, PAGE_SIZE, dataCount2);
            } else {
                dataRows1 = ReaderUtils.readCSVDataSource(reader1, dsMetaData, compareRequest, PAGE_SIZE, dataCount1);
                dataRows2 = ReaderUtils.readCSVDataSource(reader2, dsMetaData, compareRequest, PAGE_SIZE, dataCount2);
            }
        	dataCount1+=dataRows1.size();
        	dataCount2+=dataRows2.size();
        	dataMap1.putAll(DataRow.toMap(dataRows1));
        	dataMap2.putAll(DataRow.toMap(dataRows2));
            resultData.addAll(findAndRemoveDifferentRows(dataMap1, dataMap2, compareRequest));
            resultData.addAll(findAndRemoveDifferentRows(dataMap2, dataMap1, compareRequest));
        } while (dataRows1.size() > 0 || dataRows2.size() > 0);
        resultData.addAll(addMissingRow(dataMap1, compareRequest, true));
        resultData.addAll(addMissingRow(dataMap2, compareRequest, false));
        
        ReaderUtils.writeFile(compareRequest.getResultFileName(), resultData);
        
        /**
         * lochp
        */ 
        try{
        	JExcelUtils.writeFile(resultData, compareRequest);
        }catch(Exception e){
        	e.printStackTrace();
        	System.out.println("Error to Write Excel File! ERROR: " + e.getMessage());
        }
        
        
        reader1.close();
        reader2.close();
    }
    
    /**
     * Compare CVS data driven by header of each data files.
     * TODO exclude different columns from comparison
     * @param compareRequest
     * @throws Exception
     */
    public static void compareCSV(CompareRequest compareRequest) throws Exception {
    	DSMetaData dsMetaData1 = new DSMetaData();
    	DSMetaData dsMetaData2 = new DSMetaData();
    	BufferedReader reader1 = new BufferedReader(new FileReader(new File(compareRequest.getFileName1())));
        BufferedReader reader2 = new BufferedReader(new FileReader(new File(compareRequest.getFileName2())));
        
        HashMap<String, DataRow> dataMap1 = new HashMap<String, DataRow>();
        HashMap<String, DataRow> dataMap2 = new HashMap<String, DataRow>();
        System.out.println("Parse metadata for data file 1:" + compareRequest.getFileName1());
        dsMetaData1 = parseHeaderRow(reader1.readLine(), compareRequest.getKeyFieldsStr1(), compareRequest);
        System.out.println("Parse metadata for data file 2:" + compareRequest.getFileName2());
        dsMetaData2 = parseHeaderRow(reader2.readLine(), compareRequest.getKeyFieldsStr2(), compareRequest);

        
        List<DataRow> dataRows1;
        List<DataRow> dataRows2;
        Set<String> mutualColumns = getMutualColumns(dsMetaData1, dsMetaData2);
        
        List<String> resultData = new ArrayList<String>();
        resultData.add(buildHeader(mutualColumns, dsMetaData1, compareRequest));
        
        int dataCount1 = 0, dataCount2 = 0;
        //int index = 0;
        do {
            dataRows1 = ReaderUtils.readCSVDataSource(reader1, mutualColumns, dsMetaData1, null, compareRequest, PAGE_SIZE, dataCount1);
            dataRows2 = ReaderUtils.readCSVDataSource(reader2, mutualColumns, dsMetaData2, dsMetaData1, compareRequest, PAGE_SIZE, dataCount2);
        	dataCount1+=dataRows1.size();
        	dataCount2+=dataRows2.size();
        	dataMap1.putAll(DataRow.toMap(dataRows1));
        	dataMap2.putAll(DataRow.toMap(dataRows2));
            resultData.addAll(findAndRemoveDifferentRows(dataMap1, dataMap2, compareRequest));
            resultData.addAll(findAndRemoveDifferentRows(dataMap2, dataMap1, compareRequest));
            //index++;
            //System.out.println(index);
        } while (dataRows1.size() > 0 || dataRows2.size() > 0);
        resultData.addAll(addMissingRow(dataMap1, compareRequest, true));
        resultData.addAll(addMissingRow(dataMap2, compareRequest, false));
        
        ReaderUtils.writeFile(compareRequest.getResultFileName(), resultData);
        
        /**
         * lochp
         */
        try{
        	JExcelUtils.writeFile(resultData, compareRequest);
        }catch(Exception e){
        	System.out.println("Error to Write Excel File! ERROR: " + e.getMessage());
        }
        
        reader1.close();
        reader2.close();
    }

    private static Set<String> getMutualColumns(DSMetaData dsMetaData1, DSMetaData dsMetaData2) {
		
    	Set<String> allCols = new HashSet<String>();
		allCols.addAll(dsMetaData1.getFieldListInOrder());
		allCols.addAll(dsMetaData2.getFieldListInOrder());
		Set<String> mutualCols = new HashSet<String>();
		for (String col : allCols ) {
			if (dsMetaData1.getFieldIndexMap().containsKey(col) 
					&& dsMetaData2.getFieldIndexMap().containsKey(col)) {
				mutualCols.add(col);
			}
		}
		return mutualCols;
	}

	/**
     * Parse the header to build the meta data.
     * @param headerRow
     * @param compareRequest 
     * @return
     */
    private static DSMetaData parseHeaderRow(String headerRow, String keyFields, CompareRequest compareRequest) {
    	List<String> fieldListInOrder = Arrays.asList(headerRow.split(compareRequest.getDataSeparator()));
		Map<String, Integer> indexFieldMap = new HashMap<String, Integer>(); 
		for (int i = 0; i < fieldListInOrder.size(); i++) {
			String field = fieldListInOrder.get(i);
			if (indexFieldMap.containsKey(field)){
				System.out.println(String.format(
						"Header of column %d is duplicated with column %d with the same name [%s]. "
						+ "Please update header to make them unique", i, indexFieldMap.get(field), field));
			} else {
				indexFieldMap.put(field, i);
			}
		}
		DSMetaData metadata = new DSMetaData(fieldListInOrder, indexFieldMap);
		metadata.setKeysField(keyFields);
		return metadata;
	}

	private static String buildHeader(Set<String> mutualColumns, DSMetaData dsMetaData, CompareRequest compareRequest) {
		String header = String.format("SourceName%1$sLineNumber%1$sKey Values%1$s", compareRequest.getResultColumnSeparator());
		
		List<String> comparedCols = new ArrayList<String>();
		for (String col : dsMetaData.getFieldListInOrder()) {
			if (mutualColumns == null || mutualColumns.contains(col)) {
				comparedCols.add(col);
			}
		}
		header += StringUtils.join(comparedCols, compareRequest.getDataJoinDelimiter());
		
		return header;
	}

	private static List<String> addMissingRow(HashMap<String, DataRow> dataMap,
			CompareRequest compareRequest, boolean isDataSource1) {
    	List<String> resultData = new ArrayList<String>();
    	String delim = compareRequest.getResultColumnSeparator();
    	for (String missingKey : dataMap.keySet()) {
    		DataRow dataRow = dataMap.get(missingKey);
    		String compareResultRow;
			if (isDataSource1) {
	    		compareResultRow = compareRequest.getDsName1() + delim + dataRow.getLineNumber() +
	    				delim + missingKey + delim + dataRow.getRowValue();
	            resultData.add(compareResultRow);
	    		compareResultRow = compareRequest.getDsName2() + delim + delim + "MISSING key: " + missingKey;
	            resultData.add(compareResultRow);
    		} else {
    			compareResultRow = compareRequest.getDsName1() + delim + delim + "MISSING key: " + missingKey;
	            resultData.add(compareResultRow);
	            compareResultRow = compareRequest.getDsName2() + delim + dataRow.getLineNumber() +
	            		delim + missingKey + delim + dataRow.getRowValue();
	            resultData.add(compareResultRow);
    		}
    	}
    	
		return resultData;
	}

	private static List<String> findAndRemoveDifferentRows(HashMap<String, DataRow> sourceDataMap,
			HashMap<String, DataRow> destDataMap, CompareRequest compareRequest) {
    	String resSeparator = compareRequest.getResultColumnSeparator();
		List<String> resultData = new ArrayList<String>();
    	List<String> matchedKeys = new ArrayList<String>();
//    	String compareResultRow = "";
    	StringBuilder compareResultRow = null;
    	for (String key1 : sourceDataMap.keySet()) {
            String value1, value2;
            if (destDataMap.keySet().contains(key1)) {
            	matchedKeys.add(key1);
                DataRow dataRow1 = sourceDataMap.get(key1);
                DataRow dataRow2 = destDataMap.get(key1);
                value1 = dataRow1.getRowValue();
                value2 = dataRow2.getRowValue();
                if (!value1.equals(value2)) {
                	compareResultRow = new StringBuilder();
                    compareResultRow.append(compareRequest.getDsName1())
                    	.append(resSeparator)
                    	.append(dataRow1.getLineNumber())
                    	.append(resSeparator)
                    	.append(key1)
                    	.append(resSeparator)
                    	.append(value1)
                    	.append(compareRequest.getDataJoinDelimiter()) //TODO check to remove this last join delimiter
                    	.append(" ")
                    	;
                    
                    resultData.add(compareResultRow.toString());
                    
                    compareResultRow = new StringBuilder();
                    compareResultRow.append(compareRequest.getDsName2())
                	.append(resSeparator)
                	.append(dataRow2.getLineNumber())
                	.append(resSeparator)
                	.append(key1)
                	.append(resSeparator)
                	.append(value2)
                	.append(compareRequest.getDataJoinDelimiter())
                	.append(" ")
                	;
                    
                    /*compareResultRow = compareRequest.getDsName2() + resSeparator + dataRow2.getLineNumber() + 
                    		resSeparator + key1 + resSeparator + value2;*/
                    resultData.add(compareResultRow.toString());
                }
            }
        }
    	for (String matchedKey : matchedKeys) {
    		sourceDataMap.remove(matchedKey);
    		destDataMap.remove(matchedKey);
    	}
		return resultData;
	}

	public static boolean verify(String filePath1, String filePath2) {
        List<String> actualRows = ReaderUtils.readFile(filePath1);
        List<String> expectedRows = ReaderUtils.readFile(filePath2);
        return actualRows.equals(expectedRows);
    }
}
