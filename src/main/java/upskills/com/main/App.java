package upskills.com.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/*import ups.mongo.MongoConnectionBootApplication;
import ups.mongo.service.ReconInputServiceMx2;*/
import upskills.com.utils.ReaderUtils;
import upskills.sqlcon.model.User;
import upskills.sqlcon.service.UserService;

/**
 * Hello world!
 *
 */
@SpringBootApplication
//@ComponentScan(basePackages = { "ups.mongo", "upskills.sqlcon" })
public class App  //implements ApplicationRunner 
{

	//@Autowired
	//ReconInputServiceMx2 reconInputServiceMx2;
	

	//@Autowired
	//private UserService userService;

	public static void main(String[] args) {

		// ReaderUtils.compareData("MxG2K.txt", "MY3.txt", "DataStructure.csv",
		// "D:\\ResultData.txt", false);
		// ReaderUtils.compareData("MxG2KFieldAndKey.txt", "MY3FieldAndKey.txt",
		// "DataStructutr_FieldAndKey.csv", "D:\\ResultDataFieldAndKey.txt", true);
		//SpringApplication.run(App.class, args);
		System.out.println("Hello");
		
		test t= new test();
		t.foo();

	}

/*	@Override
	public void run(ApplicationArguments args) throws Exception {

		//System.out.println("Begin");
		//System.out.println("Result: " + reconInputServiceMx2.getByReportId("R315").size());

		System.out.println("Begin testing phase...");

		User user = userService.getUserByName("LLE");

		System.out.println(user.getName());
	}*/

}
