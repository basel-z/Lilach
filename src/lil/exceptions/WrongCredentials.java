package src.lil.exceptions;

public class WrongCredentials extends Exception {
	public WrongCredentials() {
		super("Wrong user ID or password!");
	}
}
