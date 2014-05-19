package loginlogout;

import java.io.IOException;

import pvt14servertest.SystemTesting;
import pvt14servertest.Values;
import pvt14servertest.resultClass;

public class RunLogin {

	/**
	 * Init login tests, mainly prints to console and web
	 * 
	 * @throws Exception
	 * @throws IOException
	 */
	public static void initlogintest() throws Exception, IOException {
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
		TestLogin[] objs = new TestLogin[Values.NTHREADS];
		for (int i = 0; i < objs.length; i++) {
			objs[i] = new TestLogin();
		}
		for (Thread thread : objs) {
			thread.start();
			Thread.sleep(5);
		}
		for (Thread thread : objs) {
			thread.join();
		}
		float per;
		if (Values.logintotTest != 0) {
			per = (float) Values.loginaccTest / (float) Values.logintotTest
					* 100;

		} else {
			per = 0;
		}
		System.out.print("Login succ tests: " + Values.loginaccTest
				+ "  tot tests : " + Values.logintotTest + "   percent:" + per);

		SystemTesting.writeWeb
				.createTestSection("Test: Login "
						+ Values.NTHREADS
						+ " Thread * "
						+ Values.NLOOPS
						+ " [Random Unames/Password]<br>Test info:"
						+ "Trying to login with random usernames and passwords, checking if server response with token<br> Test date: "
						+ pvt14servertest.SystemTesting.dateFormat
								.format(pvt14servertest.SystemTesting.date)
						+ "<br><br>");

		SystemTesting.writeWeb.writeToHTML((int) per);

	}

}
