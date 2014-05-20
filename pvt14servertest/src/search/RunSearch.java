package search;

import pvt14servertest.SystemTesting;
import pvt14servertest.Values;
import pvt14servertest.resultClass;

import java.io.IOException;

public class RunSearch {
	/**
	 * Init login tests, mainly prints to console and web
	 * 
	 */
	public  void initsearchtest()  {
        try {
		long startTime = System.currentTimeMillis();
            testSearch();
        long endTime = System.currentTimeMillis();
		System.out.println("  " + (double) ((endTime) - (startTime)) / 1000
				+ "s");

		System.out.println(resultClass.getInstance().getMap().toString());
		SystemTesting.writeWeb.createTestSection("Failures:<br>"
				+ resultClass.getInstance().getMap().toString());
		SystemTesting.writeWeb.endTest();
        } catch (InterruptedException e) {
            System.err.println("Search failed");
        } catch (IOException e) {
            System.err.println("Search failed");
        }
	}

	/**
	 * Start the login tests
	 * 
	 */
	private  void testSearch() throws InterruptedException, IOException {
		float percent;
		TestSearch[] threads = new TestSearch[Values.NTHREADS];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new TestSearch();
		}
		for (Thread thread : threads) {
			thread.start();
			Thread.sleep(5);
		}
		for (Thread thread : threads) {
			thread.join();
		}
		if (Values.searchtot != 0) {
			percent = (float) Values.searchacc / (float) Values.searchtot * 100;

		} else {
			percent = 0;
		}
		System.out.print("Search success: " + Values.searchacc
                + "  tot tests : " + Values.searchtot + "   percent:" + percent+"%");

		SystemTesting.writeWeb.createTestSection("Test: search "
                + Values.NTHREADS
                + " Thread * "
                + Values.NLOOPS
                + " [Random Unames/Password]<br>Test info:"
                + "Testing response code 200<br> Test date: "
                + pvt14servertest.SystemTesting.dateFormat
                .format(pvt14servertest.SystemTesting.date)
                + "<br><br>");

		SystemTesting.writeWeb.writeToHTML((int) percent);

	}

}
