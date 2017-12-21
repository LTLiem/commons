package upskills.com.main;

import org.springframework.beans.factory.annotation.Autowired;

import upskills.sqlcon.model.User;
import upskills.sqlcon.service.UserService;

public class test {
	
	@Autowired
	private UserService userService;

	public void foo() {
		System.out.println("Begin testing phase...");

		User user = userService.getUserByName("LLE");

		System.out.println(user.getName());
	}
}
