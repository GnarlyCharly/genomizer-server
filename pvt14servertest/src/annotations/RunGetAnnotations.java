package annotations;

import pvt14servertest.SystemTesting;
import pvt14servertest.Values;
import pvt14servertest.resultClass;

import java.io.IOException;

public class RunGetAnnotations {

	/**
	 * Init annotations test , mainly the prints to console and web
	 * 
	 */
	public  void initannotest()  {

        long startTime = System.currentTimeMillis();
        try {
            testgetAnnotations();
        long endTime = System.currentTimeMillis();
		System.out.println("  " + (double) ((endTime) - (startTime)) / 1000
				+ "s");
		System.out.println(resultClass.getInstance().getMap().toString());
		SystemTesting.writeWeb.createTestSection("Failures:<br>"
				+ resultClass.getInstance().getMap().toString());
		SystemTesting.writeWeb.endTest();
        } catch (IOException e) {
            System.err.println("Get annotations failed");
        } catch (InterruptedException e) {
            System.err.println("Get annotations failed");
        }

	}

	/**
	 * start the Tests: get annotations
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private  void testgetAnnotations() throws IOException,
			InterruptedException {
		float percent;
		Thread[] threads = new Thread[Values.NTHREADS];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(
					new TestAnnotations());
		}
		for (Thread thread : threads) {
			thread.start();
			Thread.sleep(5);
		}

		for (Thread thread : threads) {
			thread.join();
		}
		if (Values.getanntotTest != 0) {
			percent = (float) Values.getannaccTest / (float) Values.getanntotTest
					* 100;

		} else {
			percent = 0;
		}
		System.out.print("Annotations: succ test: " + Values.getannaccTest
                + "  tottests: " + Values.getanntotTest + " percent:" + percent+"%");

		SystemTesting.writeWeb
				.createTestSection("Test:Get Annotations: "
                        + Values.NTHREADS
                        + " Thread * "
                        + Values.NLOOPS
                        + " <br>"
                        + "Test info: Checking if the server return response code \"200\".<br> Test date: "
                        + SystemTesting.dateFormat.format(SystemTesting.date)
                        + "<br><br>");
		SystemTesting.writeWeb.writeToHTML((int) percent);

	}

}
