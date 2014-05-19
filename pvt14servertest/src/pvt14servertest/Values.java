package pvt14servertest;

public class Values {

	public static volatile int logintotTest, loginaccTest, getanntotTest,
			getannaccTest, sendexpaccTest, sendexptotTest, incdeleteexptot,
			incsendexpaccTest, logoutacc, logouttot, searchtot, searchacc,
			loginfailfails, loginfailtot;
	public final static int NTHREADS = 1;
	public final static int NLOOPS = 1;

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
