package upskills.com.model;

/**
 * This class contain request info to compare two text files.
 * @author Thinh Vo
 *
 */
public class CompareRequest {

    /**
     * This field used to indicate whether datasource file is in fixed length (true) or CSV format (false).
     */
    private Boolean isFixedLengthData;
    
    /**
     * The absolute file name of metadata (data structure) file of fixed length data sources need to compare
     */
    private String metaDataFileName;
    
    private String dsName1;
    private String fileName1;
    private String keyFieldsStr1;
    
    private String dsName2;
    private String fileName2;
    private String keyFieldsStr2;
    
    private String showCSV = "TRUE";
    
    private String dataSeparator = "_;_";
    
    private String resultColumnSeparator = "_;_";
    
    /**
     * This delimiter used to join all column values of each row into a string for comparison
     */
    private String dataJoinDelimiter = "_;_";
    
    /**
     * This delimiter used to join key column values of each row into a string as a composite key
     */
    private String keyJoinDelimiter = ".";
    
    private boolean hasDataHeader = true;
    
    /**
     * the absolute file name to store comparison result
     */
    private String resultFileName;

    public CompareRequest() {
        
    }
    
    public String getShowCSV() {
		return showCSV;
	}

	public void setShowCSV(String showCSV) {
		this.showCSV = showCSV;
	}

	public CompareRequest(String metaDataFileName, String dsName1, String filePath1, 
            String dsName2, String filePath2, String resultFilePath) {
        this.metaDataFileName = metaDataFileName;
        this.dsName1 = dsName1;
        this.fileName1 = filePath1;
        this.dsName2 = dsName2;
        this.fileName2 = filePath2;
        this.resultFileName = resultFilePath;
    }
    
    public CompareRequest(boolean isFixedLengthData) {
        this.isFixedLengthData = isFixedLengthData;
    }

    public Boolean getIsFixedLengthData() {
        return isFixedLengthData;
    }

    public void setIsFixedLengthData(Boolean isFixedLengthData) {
        this.isFixedLengthData = isFixedLengthData;
    }

    public String getMetaDataFileName() {
        return metaDataFileName;
    }

    public void setMetaDataFileName(String metaDataFileName) {
        this.metaDataFileName = metaDataFileName;
    }

    public String getDsName1() {
        return dsName1;
    }

    public void setDsName1(String dsName1) {
        this.dsName1 = dsName1;
    }

    public String getFileName1() {
        return fileName1;
    }

    public void setFileName1(String fileName1) {
        this.fileName1 = fileName1;
    }

    public String getDsName2() {
        return dsName2;
    }

    public void setDsName2(String dsName2) {
        this.dsName2 = dsName2;
    }

    public String getDataSeparator() {
		return dataSeparator;
	}

	public void setDataSeparator(String dataSeparator) {
		this.dataSeparator = dataSeparator;
	}

	public String getResultColumnSeparator() {
		return resultColumnSeparator;
	}

	public void setResultColumnSeparator(String resultColumnSeparator) {
		this.resultColumnSeparator = resultColumnSeparator;
	}

	public String getDataJoinDelimiter() {
		return dataJoinDelimiter;
	}

	public void setDataJoinDelimiter(String dataJoinDelimiter) {
		this.dataJoinDelimiter = dataJoinDelimiter;
	}

	public String getKeyJoinDelimiter() {
		return keyJoinDelimiter;
	}

	public void setKeyJoinDelimiter(String keyJoinDelimiter) {
		this.keyJoinDelimiter = keyJoinDelimiter;
	}

	public boolean isHasDataHeader() {
		return hasDataHeader;
	}

	public void setHasDataHeader(boolean hasDataHeader) {
		this.hasDataHeader = hasDataHeader;
	}

	public String getFileName2() {
        return fileName2;
    }

    public void setFileName2(String fileName2) {
        this.fileName2 = fileName2;
    }

    public String getResultFileName() {
        return resultFileName;
    }

    public void setResultFileName(String resultFileName) {
        this.resultFileName = resultFileName;
    }

	public String getKeyFieldsStr1() {
		return keyFieldsStr1;
	}

	public void setKeyFieldsStr1(String keyFieldsStr1) {
		this.keyFieldsStr1 = keyFieldsStr1;
	}

	public String getKeyFieldsStr2() {
		return keyFieldsStr2;
	}

	public void setKeyFieldsStr2(String keyFieldsStr2) {
		this.keyFieldsStr2 = keyFieldsStr2;
	}
}
