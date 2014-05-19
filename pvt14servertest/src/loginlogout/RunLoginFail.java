package loginlogout;

import java.io.IOException;

import pvt14servertest.SystemTesting;
import pvt14servertest.Values;
import pvt14servertest.resultClass;

public class RunLoginFail {

	private static String teststring;

	public RunLoginFail(String teststring) {
		this.teststring = teststring;
	}

	/**
	 * Init login tests, mainly prints to console and web
	 * 
	 * @throws Exception
	 * @throws IOException
	 */
	public static void initloginfailtest() throws Exception, IOException {
		long startTime = System.currentTimeMillis();
		testLoginFail();
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
	private static void testLoginFail() throws Exception {
		TestLoginFail[] objs = new TestLoginFail[Values.NTHREADS];
		for (int i = 0; i < objs.length; i++) {
			objs[i] = new TestLoginFail();
		}
		for (Thread thread : objs) {
			thread.start();
			Thread.sleep(5);
		}
		for (Thread thread : objs) {
			thread.join();
		}
		float per;
		if (Values.loginfailtot != 0) {
			per = (float) Values.loginfailfails / (float) Values.loginfailtot
					* 100;

		} else {
			per = 0;
		}
		System.out.print("Login fail tests: " + Values.loginfailfails
				+ "  tot tests : " + Values.loginfailtot + "   percent:" + per);

		SystemTesting.writeWeb.createTestSection("Test: Login Fail "
				+ Values.NTHREADS
				+ " Thread * "
				+ Values.NLOOPS
				+ " [Random Unames/Password]<br>Test info:"
				+ teststring
				+ "<br> Test date: "
				+ pvt14servertest.SystemTesting.dateFormat
						.format(pvt14servertest.SystemTesting.date)
				+ "<br><br>");

		SystemTesting.writeWeb.writeToHTML((int) per);

	}
}
