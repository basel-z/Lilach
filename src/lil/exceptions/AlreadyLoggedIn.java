package src.lil.exceptions;

public class AlreadyLoggedIn extends Exception {
	public AlreadyLoggedIn() {
		super("user already logged in!");
	}
}
