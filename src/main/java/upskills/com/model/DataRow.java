package upskills.com.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataRow {
	private String rowValue;
	private String keysValue;
	private Map<String, String> valueMap;
	private int lineNumber;
	
	public DataRow() {
		
	}

	public String getRowValue() {
		return rowValue;
	}

	public void setRowValue(String rowValue) {
		this.rowValue = rowValue;
	}

	public String getKeys() {
		return keysValue;
	}

	public void setKeys(String keys) {
		this.keysValue = keys;
	}

	public Map<String, String> getValueMap() {
		return valueMap;
	}

	public void setValueMap(Map<String, String> valueMap) {
		this.valueMap = valueMap;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	public static Map<String, DataRow> toMap(List<DataRow> rows) {
		Map<String, DataRow> dataMap = new HashMap<String, DataRow>();
		for (DataRow row : rows) {
			dataMap.put(row.getKeys(), row);
		}
		return dataMap;
	}
	
}
