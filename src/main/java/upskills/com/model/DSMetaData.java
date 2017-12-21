package upskills.com.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class DSMetaData {
	private Map<String, FieldsObject> fieldsMap;
	private List<String> keysField;
	private List<String> fieldListInOrder;
	private Map<String, Integer> fieldIndexMap;
	
	public DSMetaData(Map<String, FieldsObject> map, List<String> keysField) {
		this.fieldsMap = map;
		this.keysField = keysField;
	}
	
	public DSMetaData(List<String> fieldListInOrder, Map<String, Integer> indexFieldMap) {
		this.fieldListInOrder = fieldListInOrder;
		this.fieldIndexMap = indexFieldMap;
	}
	
	public DSMetaData(Map<String, FieldsObject> fieldsMap, List<String> keysField,
			List<String> fieldListInOrder) {
		this.fieldListInOrder = fieldListInOrder;
		this.fieldsMap = fieldsMap;
		this.keysField = keysField;
	}

	public DSMetaData() {
		this.fieldsMap = new LinkedHashMap<String, FieldsObject>();
		this.keysField = new ArrayList<String>();
		this.fieldListInOrder = new ArrayList<String>();		
	}
	
	public Map<String, FieldsObject> getFieldsMap() {
		return fieldsMap;
	}

	public void setFieldsMap(LinkedHashMap<String, FieldsObject> fieldsMap) {
		this.fieldsMap = fieldsMap;
	}

	public List<String> getKeysField() {
		return keysField;
	}

	public void setKeysField(List<String> keysField) {
		this.keysField = keysField;
	}
	
	public void setKeysField(String keysFieldStr) {
		if (StringUtils.isBlank(keysFieldStr)) {
			return;
		}
		List<String> keys = new ArrayList<String>();
		String[] keyFieldsOrIndice = keysFieldStr.split(",");
		for (String keyFieldOrIndex : keyFieldsOrIndice) {
			try {
				String key = fieldListInOrder.get(Integer.valueOf(keyFieldOrIndex));
				keys.add(key);
			} catch (NumberFormatException e) {
				keys.add(keyFieldOrIndex);
			}
		}
		this.keysField = keys;
	}
	
	public List<String> getFieldListInOrder() {
		return fieldListInOrder;
	}


	public void setFieldListInOrder(List<String> fieldListInOrder) {
		this.fieldListInOrder = fieldListInOrder;
	}

	public Map<String, Integer> getFieldIndexMap() {
		return fieldIndexMap;
	}


	public void setFieldIndexMap(Map<String, Integer> indexFieldMap) {
		this.fieldIndexMap = indexFieldMap;
	}


	public void addKey(String key) {
		if(key != null && key.length() > 0) {
			keysField.add(key);
		}
	}
	
	public void addValueToMap(String key, FieldsObject value) {
		if(key != null && key.length() > 0) {
			this.fieldsMap.put(key, value);
		}
	}
}
