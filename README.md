# Upskills Common Utils

This project will contain common Utils for Upskills development.

In the 1st project, this project will focus on data reconciliation between report data of MxG2K and MX.3.

Supported data format are:
- Text file
- Excel
- DB

## A. Common Approaching
### 1. Input
- the 1st data source
- the 2nd data source
- metadata: used to 
    - define data layout of two input data sources
    - declare how to compare data between two input data sources

### 2. Output
- CSV
- Excel
- JSON

## B. Specification
### 1 Text file reconciliation
Support two types: fixed length or CSV

	1.1 Fixed length
- Input
    - Metadata is specified by comma separated CSV in below example format
		- Data structure definition
	
		| Field name| length   | Start | End  |
		|-----------|----------|-------|------|
		|  Field_1| 9        | 1     |9     |

		- Key columns is specified by last row

		|  Field_1| Field_2| Field_5     |Field_10     |

	- Data sources could be indicated containing header or not
		- Names and data files for 1st and 2nd data sources
- Output: is coma separated. Each row data differences will be represented by two rows for two data sources corresponding with |Source Name| keys concatenated by "."|<original data row>|. E.g.

	| Source Name| Key values| Original Value  |
	|------------|-----------|-----------------|
	|  MxG2k| key1.key2.key3.key4 | <original_row>  |
	|  MX.3| key1.key2.key3.key4 | <original_row>     |
	|  MxG2k| a.b.c.d | <original_row>  |
	|  **MX.3**| **MISSING** |      |
	|  **MxG2k**| **MISSING** |      |
	|  MX.3| A.S.D.F | <original_row>  |

