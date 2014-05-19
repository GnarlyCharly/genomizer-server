package search;

import java.io.IOException;

import pvt14servertest.SystemTesting;
import pvt14servertest.Values;
import pvt14servertest.resultClass;

public class RunSearch {
	/**
	 * Init login tests, mainly prints to console and web
	 * 
	 * @throws Exception
	 * @throws IOException
	 */
	public static void initsearchtest() throws Exception, IOException {
		long startTime = System.currentTimeMillis();
		testSearch();
		long endTime = System.currentTimeMillis();
		System.out.println("  " + (double) ((endTime) - (startTime)) / 1000
				+ "s");

		System.out.println(resultClass.getInstance().getMap().toString());
		SystemTesting.writeWeb.createTestSection("Failures:<br>"
				+ resultClass.getInstance().getMap().toString());
		SystemTesting.writeWeb.endTest();
	}

	/**
	 * Start the login tests
	 * 
	 * @throws Exception
	 */
	private static void testSearch() throws Exception {
		TestSearch[] objs = new TestSearch[Values.NTHREADS];
		for (int i = 0; i < objs.length; i++) {
			objs[i] = new TestSearch();
		}
		for (Thread thread : objs) {
			thread.start();
			Thread.sleep(5);
		}
		for (Thread thread : objs) {
			thread.join();
		}
		float per;
		if (Values.searchtot != 0) {
			per = (float) Values.searchacc / (float) Values.searchtot * 100;

		} else {
			per = 0;
		}
		System.out.print("Search succ tests: " + Values.searchacc
				+ "  tot tests : " + Values.searchtot + "   percent:" + per);

		SystemTesting.writeWeb.createTestSection("Test: search "
				+ Values.NTHREADS
				+ " Thread * "
				+ Values.NLOOPS
				+ " [Random Unames/Password]<br>Test info:"
				+ "Testing response code 200<br> Test date: "
				+ pvt14servertest.SystemTesting.dateFormat
						.format(pvt14servertest.SystemTesting.date)
				+ "<br><br>");

		SystemTesting.writeWeb.writeToHTML((int) per);

	}

}
