package ua.nure.tarasenko.summary4.exception;

/**
 * Throws if specified object was locked.
 * 
 * @author Taasenko
 *
 */
public class WasLockedException extends Exception {

	private static final long serialVersionUID = 1L;

	public WasLockedException() {
		super();
	}

	public WasLockedException(String arg0) {
		super(arg0);
	}

}
