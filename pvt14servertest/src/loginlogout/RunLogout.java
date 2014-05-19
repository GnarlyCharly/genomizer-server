package loginlogout;

import java.io.IOException;

import pvt14servertest.SystemTesting;
import pvt14servertest.Values;
import pvt14servertest.resultClass;

public class RunLogout {

	/**
	 * Init login tests, mainly prints to console and web
	 * 
	 * @throws Exception
	 * @throws IOException
	 */
	public static void initlogouttest() throws Exception, IOException {
		long startTime = System.currentTimeMillis();
		testLogin();
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

	private static void testLogin() throws Exception {
		TestLogout[] objs = new TestLogout[Values.NTHREADS];
		for (int i = 0; i < objs.length; i++) {
			objs[i] = new TestLogout();
		}
		for (Thread thread : objs) {
			thread.start();
			Thread.sleep(20);
		}
		for (Thread thread : objs) {
			thread.join();
		}
		float per;
		if (Values.logouttot != 0) {
			per = (float) Values.logoutacc / (float) Values.logouttot * 100;

		} else {
			per = 0;
		}
		System.out.print("logout succ tests: " + Values.logoutacc
				+ "  tot tests : " + Values.logouttot + "   percent:" + per);

		SystemTesting.writeWeb
				.createTestSection("Test: Logout "
						+ Values.NTHREADS
						+ " Thread * "
						+ Values.NLOOPS
						+ " [Random Unames/Password]<br>Test info:"
						+ "Trying to Logout, checking if server response with response code: 200<br> Test date: "
						+ pvt14servertest.SystemTesting.dateFormat
								.format(pvt14servertest.SystemTesting.date)
						+ "<br><br>");

		SystemTesting.writeWeb.writeToHTML((int) per);

	}

}
