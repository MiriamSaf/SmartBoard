package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.Authenticator;
import model.DefaultUser;
import model.User;

//test authenticator class 
public class AuthenticatorTest {

	List<User> listOfUsers = new ArrayList<User>();
	Authenticator auth = new Authenticator(listOfUsers);

	@Before
	public void setUp() {

		DefaultUser deUser = new DefaultUser("hello", "user", "ruth", "naomi", null);

		listOfUsers.add(deUser);

		DefaultUser deUser1 = new DefaultUser("password", "user2", "orpah", "queen", null);

		listOfUsers.add(deUser1);

	}

	@After
	public void tearDown() {
		listOfUsers = null;
		auth = null;
	}

	// test trying to add a duplicate user with the saved list
	@Test
	public void testDuplicateSignUp() {
		assertTrue(auth.checkDuplicateUserName("user", listOfUsers));
	}

	// test trying to add non duplicate user with the saved list
	@Test
	public void testNotDuplicateSignUp() {
		assertFalse(auth.checkDuplicateUserName("nondups", listOfUsers));
	}

	// password matches with stored user password
	@Test
	public void testPasswordSuccess() {
		assertTrue(auth.validateChosenPassword("user", "hello"));
	}

	// password does not match username
	@Test
	public void testPasswordUnsuccessful() {
		assertFalse(auth.validateChosenPassword("user", "hellothere"));
	}

	// password does not match username
	@Test
	public void testPasswordWrongUser() {
		assertFalse(auth.validateChosenPassword("user", "password"));
	}
}
