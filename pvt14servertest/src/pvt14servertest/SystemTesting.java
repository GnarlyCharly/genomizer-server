package pvt14servertest;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import loginlogout.RunLogin;
import loginlogout.RunLoginFail;
import loginlogout.RunLogout;
import search.RunSearch;
import annotations.RunGetAnnotations;
import experiment.RunDeleteExperiment;
import experiment.RunSendExperiment;

public class SystemTesting {
	public static Token token;
	public static Token tokporten = null;

	public static final int PORT = 7000;

	public static ToWeb writeWeb;

	public static DateFormat dateFormat;
	public static Date date;

	private static loginlogout.RunLogin runlogin = new RunLogin();
	private static loginlogout.RunLoginFail runloginfail = new RunLoginFail(
			"Testing if invalid information fails");
	private static loginlogout.RunLogout runlogout = new RunLogout();

	private static annotations.RunGetAnnotations rungetannotations = new RunGetAnnotations();

	private static experiment.RunSendExperiment runsendexp = new RunSendExperiment();
	private static experiment.RunDeleteExperiment rundelexp = new RunDeleteExperiment();

	private static search.RunSearch runsearch = new RunSearch();

	public static AddedExperiments addedexps = new AddedExperiments();

	public static void main(String args[]) throws Exception {
		dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		date = new Date(System.currentTimeMillis());
		writeWeb = new ToWeb();
		writeWeb.starthtml();
		runtests();
		writeWeb.endhtml();

	}

	private static void runtests() throws IOException, Exception {
		// LOGINTEST
		runlogin.initlogintest();

		betweenTest();

		runloginfail.initloginfailtest();

		betweenTest();

		// LOGOUTTEST
		runlogout.initlogouttest();

		betweenTest();

		// ANNOTEST
		rungetannotations.initannotest();

		betweenTest();

		// SEND EXP TEST
		runsendexp.initsendexptest();

		// BETWEEN TESTS
		betweenTest();
		// BETWEEN TESTS

		rundelexp.initdeleteexptest();

		betweenTest();

		runsearch.initsearchtest();
	}

	private static void betweenTest() {
		System.out.flush();
		resultClass.getInstance().getMap().clear();
	}

}