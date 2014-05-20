package loginlogout;

import pvt14servertest.SystemTesting;
import pvt14servertest.Values;
import pvt14servertest.resultClass;

import java.io.IOException;

public class RunLoginFail {




	/**
	 * Init login tests, mainly prints to console and web
	 * 
	 */
	public  void initloginfailtest()  {
        try {
		long startTime = System.currentTimeMillis();
            testLoginFail();
        long endTime = System.currentTimeMillis();
		System.out.println("  " + (double) ((endTime) - (startTime)) / 1000
				+ "s");

		System.out.println(resultClass.getInstance().getMap().toString());
		SystemTesting.writeWeb.createTestSection("Failures:<br>"
				+ resultClass.getInstance().getMap().toString());
		SystemTesting.writeWeb.endTest();
        } catch (InterruptedException e) {
            System.err.println("LoginFail failed");
        } catch (IOException e) {
            System.err.println("LoginFail failed");
        }
	}

	/**
	 * Start the login tests
	 * 
	 */
	private  void testLoginFail() throws InterruptedException, IOException {
		float percent;
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
		if (Values.loginfailtot != 0) {
			percent = (float) Values.loginfailfails / (float) Values.loginfailtot
					* 100;

		} else {
			percent = 0;
		}
		System.out.print("Login fail tests: " + Values.loginfailfails
                + "  tot tests : " + Values.loginfailtot + "   percent:" + percent+"%");

		SystemTesting.writeWeb.createTestSection("Test: Login Fail "
                + Values.NTHREADS
                + " Thread * "
                + Values.NLOOPS
                + " [Random Unames/Password]<br>Test info:"
                + "Testing if invalid information fails"
                + "<br> Test date: "
                + pvt14servertest.SystemTesting.dateFormat
                .format(pvt14servertest.SystemTesting.date)
                + "<br><br>");

		SystemTesting.writeWeb.writeToHTML((int) percent);
	}
}
