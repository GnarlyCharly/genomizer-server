package loginlogout;

import pvt14servertest.SystemTesting;
import pvt14servertest.Values;
import pvt14servertest.resultClass;

import java.io.IOException;

public class RunLogin {

	/**
	 * Init login tests, mainly prints to console and web
	 * 
	 */
	public  void initlogintest()  {
        try {
		long startTime = System.currentTimeMillis();
            testLogin();
        long endTime = System.currentTimeMillis();
		System.out.println("  " + (double) ((endTime) - (startTime)) / 1000
				+ "s");

		System.out.println(resultClass.getInstance().getMap().toString());
		SystemTesting.writeWeb.createTestSection("Failures:<br>"
				+ resultClass.getInstance().getMap().toString());
		SystemTesting.writeWeb.endTest();
        } catch (InterruptedException e) {
            System.err.println("Login failed");
        } catch (IOException e) {
            System.err.println("Login failed");
        }
	}

	/**
	 * Start the login tests
	 * 
	 */
	private  void testLogin() throws InterruptedException, IOException {
		float percent;
		TestLogin[] threads = new TestLogin[Values.NTHREADS];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new TestLogin();
		}
		for (Thread thread : threads) {
			thread.start();
			Thread.sleep(5);
		}
		for (Thread thread : threads) {
			thread.join();
		}
		if (Values.logintotTest != 0) {
			percent = (float) Values.loginaccTest / (float) Values.logintotTest
					* 100;

		} else {
			percent = 0;
		}
		System.out.print("Login success: " + Values.loginaccTest
                + "  tot tests : " + Values.logintotTest + "   percent:" + percent+"%");

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

		SystemTesting.writeWeb.writeToHTML((int) percent);
	}

}
