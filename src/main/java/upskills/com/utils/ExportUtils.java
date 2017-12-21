package upskills.com.utils;

public class ExportUtils {
	public static void main(String[] args) {
		DBBase dbBase =  new DBBase("jdbc:oracle:thin:@//localhost:1521/xe", "reconcile", "reconcile123");
		dbBase.connect();
		if(dbBase.exportData("D:\\tkyte.csv")) {
			System.out.println("Ok");
		}
	}
}
