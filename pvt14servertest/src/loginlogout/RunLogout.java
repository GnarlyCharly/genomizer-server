package loginlogout;

import pvt14servertest.SystemTesting;
import pvt14servertest.Values;
import pvt14servertest.resultClass;

import java.io.IOException;

public class RunLogout {

	/**
	 * Init login tests, mainly prints to console and web
	 * 
	 */
	public  void initlogouttest()  {

        try {
		long startTime = System.currentTimeMillis();
            testLogout();
        long endTime = System.currentTimeMillis();
		System.out.println("  " + (double) ((endTime) - (startTime)) / 1000
				+ "s");

		System.out.println(resultClass.getInstance().getMap().toString());
		SystemTesting.writeWeb.createTestSection("Failures:<br>"
				+ resultClass.getInstance().getMap().toString());
		SystemTesting.writeWeb.endTest();
        } catch (InterruptedException e) {
            System.err.println("Logout failed");
        } catch (IOException e) {
            System.err.println("Logout failed");
        }
	}

	/**
	 * Start the login tests
	 * 
	 */

	private  void testLogout() throws InterruptedException, IOException {
		float percent;
		TestLogout[] threads = new TestLogout[Values.NTHREADS];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new TestLogout();
		}
		for (Thread thread : threads) {
			thread.start();
			Thread.sleep(20);
		}
		for (Thread thread : threads) {
			thread.join();
		}
		if (Values.logouttot != 0) {
			percent = (float) Values.logoutacc / (float) Values.logouttot * 100;

		} else {
			percent = 0;
		}
		System.out.print("logout success: " + Values.logoutacc
                + "  tot tests : " + Values.logouttot + "   percent:" + percent+"%");

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

		SystemTesting.writeWeb.writeToHTML((int) percent);

	}

}
