package pvt14servertest;

import java.util.ArrayList;

public class AddedExperiments {

	private ArrayList<String> addedexpnames = new ArrayList<String>();
	private volatile int position = 0;

	public synchronized void addExps(String name) {
		addedexpnames.add(name);
	}

	public synchronized String getExp() {
		String ret = "";
		if (position >= 0) {
			ret = addedexpnames.get(position);
			position++;
		}
		return ret;
	}

	public synchronized int getsize() {
		return addedexpnames.size();
	}

}
