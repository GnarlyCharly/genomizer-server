package pvt14servertest;

import java.util.ArrayList;

public class AddedFields {

    private ArrayList<String> addedexpnames = new ArrayList<String>();
    private ArrayList<String> addedannnames = new ArrayList<String>();
    private volatile int expposition = 0, annposition = 0;

    public synchronized void addExps(String name) {
        addedexpnames.add(name);
    }

    public synchronized void addanno(String name) {
        addedannnames.add(name);
    }

    public synchronized String getExp() {
        String ret = "";
        if (expposition >= 0) {
            ret = addedexpnames.get(expposition);
            expposition++;
        }
        return ret;
    }

    public synchronized String getann() {
        String ret = "";
        if (annposition >= 0) {
            ret = addedannnames.get(annposition);
            annposition++;
        }
        return ret;
    }

    public synchronized int getsize() {
        return addedexpnames.size();
    }

}
