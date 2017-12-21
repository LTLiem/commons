BEGIN
DECLARE
  table_source VARCHAR2(25) := 'Student';
  table_destination VARCHAR2(25) := 'Student5';
 
  key_Columns PKG_reconcile.array_t;
  time_Stamp VARCHAR2(20);
  
  begin
  key_Columns(1) := 'ID';
  key_Columns(2) := 'FullName';
  
  time_Stamp := to_char(sysdate, 'YYYY-MM-DD:HH24:MI:SS');
  PKG_RECONCILE.FEEDDATA(table_source, table_destination, key_Columns, time_Stamp, 1);
  end;
END;