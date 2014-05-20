package experiment;

import pvt14servertest.SystemTesting;
import pvt14servertest.Values;
import pvt14servertest.resultClass;

import java.io.IOException;

public class RunSendExperiment {

	public  void initsendexptest()  {
		long startTime = System.currentTimeMillis();
        try {
            testSendExp();
        long endTime = System.currentTimeMillis();
		System.out.println("  " + (double) ((endTime) - (startTime)) / 1000
				+ "s");

		System.out.println(resultClass.getInstance().getMap().toString());
		SystemTesting.writeWeb.createTestSection("Failures:<br>"
				+ resultClass.getInstance().getMap().toString());
		SystemTesting.writeWeb.endTest();
        } catch (IOException e) {
            System.err.println("Add Experiment failed");
        } catch (InterruptedException e) {
            System.err.println("Add Experiment failed");
        }
	}

	private  void testSendExp() throws IOException, InterruptedException {
        float percent;

		Thread[] threads = new Thread[Values.NTHREADS];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new TestSendExperiment();
		}
		for (Thread thread : threads) {
			thread.start();
			Thread.sleep(5);
		}

		for (Thread thread : threads) {
			thread.join();
		}
		if (Values.sendexptotTest != 0) {
			percent = (float) Values.sendexpaccTest / (float) Values.sendexptotTest
					* 100;

		} else {
			percent = 0;
		}
		System.out.print("Send experiment create: success: "
                + Values.sendexpaccTest + "  tottests: "
                + Values.sendexptotTest + " percent:" + percent+"%");

		SystemTesting.writeWeb
				.createTestSection("Test: Send experiment create: "
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
