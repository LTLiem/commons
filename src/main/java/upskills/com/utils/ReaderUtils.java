package upskills.com.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import upskills.com.model.CompareRequest;
import upskills.com.model.DSMetaData;
import upskills.com.model.DataRow;
import upskills.com.model.FieldsObject;

public final class ReaderUtils {
    
    private static DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("YYYYMMdd_HHmmss");
    
    private static DataRow processLine(String rawData, DSMetaData dsMetaData, CompareRequest compareRequest, int lineNumber) {
    	Exception lastError = null;
    	DataRow rowData = new DataRow();
        //StringBuffer stringBufferRowValue = new StringBuffer();
        List<String> values = new ArrayList<String>();
        Map<String, FieldsObject> fieldsMap = dsMetaData.getFieldsMap();
        List<String> keysField = dsMetaData.getKeysField();
        List<String> keysValue = new ArrayList<String>();
        for (String fieldName : fieldsMap.keySet()) {
            FieldsObject fieldsObject = fieldsMap.get(fieldName);
            String value = null;
            try{
            	value = rawData.substring(fieldsObject.getStart() - 1, fieldsObject.getEnd());
            } catch (Exception e) {
            	lastError = e;
            	System.out.println(String.format("Error at line [%d] - field [%s]: %s ", lineNumber, fieldName, e.getMessage()));
            }
            if (keysField.contains(fieldName)) {
                keysValue.add(value);
            }
            //stringBufferRowValue.append(value);
            values.add(value);
        }

        StringBuffer stringBufferKeys = new StringBuffer();
        for (int i = 0; i < keysValue.size(); i++) {
            if (i != 0) {
                stringBufferKeys.append("." + keysValue.get(i)/*.replaceAll("\\s","")*/ );
            } else {
                stringBufferKeys.append(keysValue.get(i)/*.replaceAll("\\s","")*/ );
            }
        }
        rowData.setKeys(stringBufferKeys.toString());
        rowData.setRowValue(StringUtils.join(values, compareRequest.getDataJoinDelimiter()));
        rowData.setLineNumber(lineNumber);
        if (lastError != null) {
        	lastError.printStackTrace();
        }
        return rowData;
    }

    private static DataRow processCSVLine(String rawData, Set<String> mutualColumns, DSMetaData dsMetaData, DSMetaData dsMainMetaData, 
    		CompareRequest compareRequest, int lineNumber) {
        DataRow rowData = new DataRow();
        //StringBuffer rowValueSB = new StringBuffer();
        List<String> fieldListInOrder = dsMetaData.getFieldListInOrder();
        Map<String, String> valueMap = new HashMap<String, String>();
//        String keyValue = "";
        StringBuilder keyValue = new StringBuilder();
        String values[] = rawData.split(compareRequest.getDataSeparator());
        
        for (int i = 0; i < values.length; i++) {
        	String value = values[i];
        	try {
        		valueMap.put(fieldListInOrder.get(i), value);
        	} catch (Exception e) {
        		if (values.length > fieldListInOrder.size()) {
        			System.out.println((dsMainMetaData == null)? ">>> 1st data source:" : ">>> 2nd data source:");
        			System.out.println(String.format(">>> Raw data\r\n\tLine number: %d\r\n\tCol count: %d\r\n\tData: %s", 
        					lineNumber, values.length, rawData));                	
                }		
        	}
        	
        }
        // Build key value
        List<String> keysField = (dsMetaData != null)? 
        		dsMetaData.getKeysField(): dsMainMetaData.getKeysField();
        if (keysField == null) {
        	throw new IllegalArgumentException("Key fields are not specified!");
        }
        for (String key : keysField) {
        	if (!valueMap.containsKey(key)) {
        		throw new IllegalArgumentException("Missing column key: " + key);
        	}
//        	keyValue += valueMap.get(key).trim() + compareRequest.getKeyJoinDelimiter();
        	keyValue.append(valueMap.get(key).trim()).append(compareRequest.getKeyJoinDelimiter());
        }
        // Build row value
        List<String> comparedValues = new ArrayList<String>();
        
        List<String> mainFieldListInOrder = (dsMainMetaData != null)? dsMainMetaData.getFieldListInOrder() : fieldListInOrder;
        for (String field : mainFieldListInOrder) {
        	//Integer fieldIndex = dsMetaData.getFieldIndexMap().get(field);
        	if (mutualColumns.contains(field)) {
        		comparedValues.add(valueMap.get(field));
        	}
        }
        // Build row data
        rowData.setKeys(keyValue.toString());
        rowData.setRowValue(StringUtils.join(comparedValues, compareRequest.getDataJoinDelimiter()));
        rowData.setLineNumber(lineNumber);
        return rowData;
    }

    public static List<DataRow> readFixLengthDataSource(BufferedReader reader, DSMetaData dsMetaData, CompareRequest compareRequest, int pageSize, int fromLine) {
        List<DataRow> result = new ArrayList<DataRow>();

        try {
            String line;
            int lineNo = fromLine;
            int count = 0;
            while ((line = reader.readLine()) != null && count < pageSize) {
                DataRow rowData = processLine(line, dsMetaData, compareRequest, lineNo++);
                count++;
                result.add(rowData);
            }
        } catch (IOException e) {
        	e.printStackTrace();
        }
        return result;
    }

    public static List<DataRow> readCSVDataSource(
    		BufferedReader reader, DSMetaData dsMetaData, CompareRequest compareRequest, int pageSize, int fromLine) throws Exception {
        return readCSVDataSource(reader, null, dsMetaData, null, compareRequest, pageSize, fromLine);
    }

    public static List<DataRow> readCSVDataSource(BufferedReader reader, Set<String> mutualColumns, DSMetaData dsMetaData, DSMetaData dsMainMetaData,
			CompareRequest compareRequest, Integer pageSize, int fromLine) throws IOException {
    	List<DataRow> result = new ArrayList<DataRow>();

        String line;
        int lineNo = fromLine;
        int count = 0;
        do {
        	line = reader.readLine();
        	if (line != null) {
        		DataRow rowData = processCSVLine(line, mutualColumns, dsMetaData, dsMainMetaData, compareRequest, lineNo++);
        		count++;
        		result.add(rowData);
        	}
        } while (count < pageSize && line != null);
   
        return result;
	}

	public static DSMetaData readCSVMetaData(String pathName) {
        DSMetaData dsMetaData = new DSMetaData();
        List<String> fieldListInOrder = new ArrayList<String>();
        LinkedHashMap<String, FieldsObject> fieldsMap = new LinkedHashMap<String, FieldsObject>();
        List<String> keyFields = new ArrayList<String>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(pathName)));
            String next, line = reader.readLine();

            for (boolean first = true, last = (line == null); !last; first = false, line = next) {
                last = ((next = reader.readLine()) == null);

                if (first) {
                    continue;
                } else {
                    String[] arrTemp = line.split(",");

                    FieldsObject object = new FieldsObject();
                    String field = arrTemp[0];
                    object.setName(field);
                    if (arrTemp.length >= 2) {
                        if (arrTemp[1] != null && Integer.parseInt(arrTemp[1]) > 0) {
                            keyFields.add(field);
                        }
                    }
                    fieldsMap.put(field, object);
                    object.setOrderNumber(fieldListInOrder.size());
                    fieldListInOrder.add(field);
                    
                }
            }
            dsMetaData.setFieldsMap(fieldsMap);
            dsMetaData.setKeysField(keyFields);
            dsMetaData.setFieldListInOrder(fieldListInOrder);
        } catch (Exception e) {
            System.err.println("readMetaDataByFieldAndKey: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException logOrIgnore) {
                }
            }
        }
        System.out.println(dsMetaData.getFieldsMap().keySet());
        return dsMetaData;
    }

    public static DSMetaData readMetaDataByFullStructure(String pathName) {
        DSMetaData dsMetaData = new DSMetaData();
        LinkedHashMap<String, FieldsObject> fieldsMap = new LinkedHashMap<String, FieldsObject>();
        List<String> keyFields = new ArrayList<String>();
        List<String> fieldListInOrder = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(pathName)));
            String next, line = reader.readLine();

            for (boolean first = true, last = (line == null); !last; first = false, line = next) {
                last = ((next = reader.readLine()) == null);

                if (first) {
                    continue;
                } else if (last) {
                    String[] keys = line.split(",");
                    for (String key : keys) {
                        if (key != null && key.length() > 0) {
                            keyFields.add(key);
                        }
                    }

                } else {
                    String[] arrTemp = line.split(",");

                    FieldsObject object = new FieldsObject();
	                fieldListInOrder.add(arrTemp[0]);
                    object.setName(arrTemp[0]);
                    object.setLength(Integer.parseInt(arrTemp[1]));
                    object.setStart(Integer.parseInt(arrTemp[2]));
                    object.setEnd(Integer.parseInt(arrTemp[3]));

                    fieldsMap.put(object.getName(), object);
                }
            }
            dsMetaData.setFieldListInOrder(fieldListInOrder);
            dsMetaData.setFieldsMap(fieldsMap);
            dsMetaData.setKeysField(keyFields);
        } catch (Exception e) {
            System.out.println("readMetaDataByFullStructure: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException logOrIgnore) {
                }
            }
        }
        return dsMetaData;
    }

    public static List<String> readFile(String fileName) {
        List<String> resultList = new ArrayList<String>();
        BufferedReader reader = null;
        try {
        	
            File sourceFile = new File(fileName);
            if (sourceFile.getParentFile().isDirectory() == false) {
            	FileUtils.forceMkdir(sourceFile.getParentFile());
            	sourceFile.createNewFile();
            }
			reader = new BufferedReader(new FileReader(sourceFile));
            
            String line;
            while ((line = reader.readLine()) != null) {
                resultList.add(line);
            }
        } catch (Exception x) {
            x.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        return resultList;
    }

    public static void writeFile(String fileName, List<String> data) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(fileName);
            
        } catch (FileNotFoundException e) {
        	File sourceFile = new File(fileName);
            if (sourceFile.getParentFile().isDirectory() == false) {
            	try {
					FileUtils.forceMkdir(sourceFile.getParentFile());
					sourceFile.createNewFile();
					writer = new PrintWriter(fileName);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
            	
            }
        }
        for (int i = 0; i < data.size(); i++) {
            writer.println(data.get(i));
        }
        writer.close();
    }

    /**
     * Return string of current time in format YYYYMMDD_HHmmss
     */
    public static String getDefaultTimeStr() {
        return DEFAULT_DATE_FORMAT.format(new Date());
    }
}
