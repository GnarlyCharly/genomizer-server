package experiment;

import pvt14servertest.SystemTesting;
import pvt14servertest.Values;
import pvt14servertest.resultClass;

import java.io.IOException;

public class RunDeleteExperiment {

	public  void initdeleteexptest()  {
        try {
		long startTime = System.currentTimeMillis();
            testDeleteAnnotations();
        long endTime = System.currentTimeMillis();

		System.out.println("  " + (double) ((endTime) - (startTime)) / 1000
				+ "s");
		System.out.println(resultClass.getInstance().getMap().toString());
		SystemTesting.writeWeb.createTestSection("Failures:<br>"
				+ resultClass.getInstance().getMap().toString());
		SystemTesting.writeWeb.endTest();
        } catch (IOException e) {
            System.err.println("Delete experiment failed");
        } catch (InterruptedException e) {
            System.err.println("Delete experiment failed");
        }
	}

	private  void testDeleteAnnotations() throws IOException,
			InterruptedException {
		float percent;
		Thread[] threads = new Thread[Values.NTHREADS];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new TestDeleteExperiment());
		}
		for (Thread thread : threads) {
			thread.start();
			Thread.sleep(5);
		}

		for (Thread thread : threads) {
			thread.join();
		}
		if (Values.incdeleteexptot != 0) {
			percent = (float) Values.incsendexpaccTest
					/ (float) Values.incdeleteexptot * 100;

		} else {
			percent = 0;
		}
		System.out.print("Delete experiments: success: "
                + Values.incsendexpaccTest + "  tottests: "
                + Values.incdeleteexptot + " percent:" + percent+"%");

		SystemTesting.writeWeb
				.createTestSection("Test: Delete experiments: "
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
