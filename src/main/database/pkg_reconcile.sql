CREATE OR REPLACE 
PACKAGE PKG_RECONCILE AS 
  type array_t is table of varchar2(1000) index by PLS_INTEGER;
  
  function hashcode_KeyColums(value_For_Hash VARCHAR2) return varchar2;
  
  procedure  feedData(data_Source VARCHAR2, table_Name_Destination VARCHAR2,
                    key_Columns array_t,
                    time_Stamp VARCHAR2);
  
END PKG_RECONCILE;

create or replace PACKAGE BODY PKG_RECONCILE AS
    
    function hashcode_KeyColums(value_For_Hash VARCHAR2) return VARCHAR2 as
    
      v_checksum VARCHAR2(32);
      BEGIN
          v_checksum := LOWER(RAWTOHEX( UTL_RAW.CAST_TO_RAW( sys.dbms_obfuscation_toolkit.md5(input_string => value_For_Hash))));
          RETURN v_checksum;
          EXCEPTION
              WHEN NO_DATA_FOUND THEN
              NULL;
          WHEN OTHERS THEN
            RAISE;
    end hashcode_KeyColums;
    
		procedure feedData(data_Source VARCHAR2, 
												table_Name_Destination VARCHAR2,
												key_Columns array_t,
												time_Stamp VARCHAR2, m_Ref_Id INTEGER) as 
				nCount int;
        idx PLS_INTEGER := key_Columns.FIRST();
        sql_Query VARCHAR2(500) := table_Name_Destination;
				begin
					select count(*) into nCount from dba_tables  where UPPER(table_name) = UPPER(table_Name_Destination);
          -- if table is existed -> create table
					if (nCount <= 0) then
						 sql_Query := 'create table ' || sql_Query || ' as'; 
					else -- if not existed -> insert
            sql_Query := 'insert into ' || sql_Query ;
					end if;
          
          sql_Query := sql_Query || ' select ' || data_Source || '.*, pkg_reconcile.hashcode_KeyColums(''' || time_Stamp || '''||';
             
          WHILE Idx IS NOT NULL LOOP
            if (Idx = key_Columns.LAST) then
                sql_Query := sql_Query || data_Source || '.'  || key_Columns(Idx) || ')';
            else 
                sql_Query := sql_Query || data_Source || '.'  || key_Columns(Idx) || '||';
            end if;
                Idx := key_Columns.NEXT(Idx);
          END LOOP;
          sql_Query := sql_Query || ' as hashcode,' || 'TO_TIMESTAMP(  '''  || time_Stamp  || ''', ''YYYY-MM-DD:HH24:MI:SS'') as ts' || ' from ' || data_Source
           || ' where ' || data_Source || '.m_Ref_Id = ';
           
           if m_Ref_Id is not null then
              sql_Query := sql_Query || m_Ref_Id; 
           else 
              sql_Query := sql_Query || '(select max(m_Ref_Id) from ' || data_Source || ')';
           end if;
          
          DBMS_OUTPUT.PUT_line(sql_Query);
          execute immediate sql_Query;
          
				end;
        
END PKG_RECONCILE;