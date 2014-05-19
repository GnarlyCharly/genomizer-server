package pvt14servertest;

public class Values {

    public static volatile int logintotTest;
    public static volatile int loginaccTest;
    public static volatile int getanntotTest;
    public static volatile int getannaccTest;
    public static volatile int sendexpaccTest;
    public static volatile int sendexptotTest;
    public static volatile int incdeleteexptot;
    public static volatile int incsendexpaccTest;
    public static volatile int logoutacc;
    public static volatile int logouttot;
    public static volatile int searchtot;
    public static volatile int searchacc;
    public static volatile int loginfailfails;
    public static volatile int loginfailtot;
    public static volatile int addannotot;
    public static volatile int addannoacc;
    public static volatile int delannotot;
    public static volatile int delannoacc;
    public final static int NTHREADS = 1;
    public final static int NLOOPS = 1;

    public static synchronized void incaddannotot() {
        addannotot++;
    }

    public static synchronized void incaddannoacc() {
        addannoacc++;
    }

    public static synchronized void incdelannoacc() {
        delannoacc++;
    }

    public static synchronized void incdelannotot() {
        delannotot++;
    }

    public static synchronized void incloginfailtot() {
        loginfailtot++;
    }

    public static synchronized void incloginfailfails() {
        loginfailfails++;
    }

    public static synchronized void inclogintot() {
        logintotTest++;
    }

    public static synchronized void incloginacc() {
        loginaccTest++;
    }

    public static synchronized void incanntot() {
        getanntotTest++;
    }

    public static synchronized void incannacc() {
        getannaccTest++;
    }

    public static synchronized void incsendexptot() {
        sendexptotTest++;
    }

    public static synchronized void incsendexpacc() {
        sendexpaccTest++;
    }

    public static synchronized void incdeleteexpacc() {
        incsendexpaccTest++;
    }

    public static synchronized void incdeleteexptot() {
        incdeleteexptot++;
    }

    public static synchronized void inclogouttot() {
        logouttot++;
    }

    public static synchronized void inclogoutacc() {
        logoutacc++;
    }

    public static synchronized void incsearchacc() {
        searchacc++;
    }

    public static synchronized void incsearchtot() {
        searchtot++;
    }

}
