package pvt14servertest;

import java.util.HashMap;

public class resultClass {

	private static HashMap<String, Integer> errors = new HashMap<String, Integer>();
	private static resultClass instance = null;

	protected resultClass() {
		// Exists only to defeat instantiation.
	}

	public static resultClass getInstance() {
		if (instance == null) {
			instance = new resultClass();
		}
		return instance;
	}

	public HashMap<String, Integer> getMap() {
		return errors;
	}

	public synchronized void addError(String errorname) {
		if (errors.get(errorname) == null) {
			errors.put(errorname, 1);
		} else {
			errors.put(errorname, (errors.get(errorname) + 1));
		}
	}

}
