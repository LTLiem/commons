package upskills.com.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBBase {
	private final String strDriverForOracle = "oracle.jdbc.OracleDriver";
	private static final String RESULT_HEADER = "Source_Table,Test_Case,M_REF_ID,HASH_KEY,HASH_DATA,STATE,TIME_STAMP";
	private String strDbConnection;
	private String dbUser;
	private String dbPassword;
	private Statement stmt;
	Connection connection;
	public DBBase() {
		connection = null;
		strDbConnection = "";
		dbUser = "";
		dbPassword = "";
		stmt = null;
	}
	
	public DBBase(String strDbConnection, String dbUser, String dbPassword) {
		this.strDbConnection = strDbConnection;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
		stmt = null;
	}
	
	public String getStrDbConnection() {
		return strDbConnection;
	}

	public void setStrDbConnection(String strDbConnection) {
		this.strDbConnection = strDbConnection;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public Statement getStmt() {
		return stmt;
	}

	public void setStmt(Statement stmt) {
		this.stmt = stmt;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public String getStrDriverForOracle() {
		return strDriverForOracle;
	}

	public void connect() {
		if(connection == null) {
			try{
				Class.forName(strDriverForOracle);
				connection = DriverManager.getConnection(strDbConnection, dbUser, dbPassword);
			}catch(Exception e ) {
				e.printStackTrace();
				connection = null;
				System.err.print("Connection failed!");
			}
		}
	}
	
	public boolean exportData(String fileName) {
		boolean isOk = true;
        String query;
        List<String> data = new ArrayList<String>();
        try {
            stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
             
            //For comma separated file
            query = "SELECT  "  +   RESULT_HEADER + " FROM RESULT"  ;   
            ResultSet set = stmt.executeQuery(query);
            StringBuffer sb; 
            // header file
            sb = new StringBuffer();
            sb.append(DBBase.RESULT_HEADER + "\n");
            data.add(sb.toString());
            String[] columnNames = RESULT_HEADER.split(",");
           while(set.next()) {
        	   sb = new StringBuffer();
        	   for(int i = 0; i < columnNames.length; i++) {
        		   if(i == columnNames.length - 1) {
        			   sb.append(set.getString(i+1)).append("\n");
        		   } else {
        			   sb.append(set.getString(i+1)).append(",");
        		   }
        	   }
        	   data.add(sb.toString());
           }
           ReaderUtils.writeFile(fileName, data);
        } catch(Exception e) {
            e.printStackTrace();
            stmt = null;
            isOk = false;
        }
		return isOk;
	}
}
