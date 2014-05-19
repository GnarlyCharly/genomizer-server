package experiment;

import java.io.IOException;

import pvt14servertest.SystemTesting;
import pvt14servertest.Values;
import pvt14servertest.resultClass;

public class RunDeleteExperiment {

	public static void initdeleteexptest() throws Exception, IOException {
		long startTime = System.currentTimeMillis();
		testDeleteAnnotations();
		long endTime = System.currentTimeMillis();
		System.out.println("  " + (double) ((endTime) - (startTime)) / 1000
				+ "s");

		System.out.println(resultClass.getInstance().getMap().toString());
		SystemTesting.writeWeb.createTestSection("Failures:<br>"
				+ resultClass.getInstance().getMap().toString());
		SystemTesting.writeWeb.endTest();
	}

	private static void testDeleteAnnotations() throws IOException,
			InterruptedException {
		Thread[] delannotationthreads = new Thread[Values.NTHREADS];
		for (int i = 0; i < delannotationthreads.length; i++) {
			delannotationthreads[i] = new Thread(new TestDeleteExperiment(
					Values.NLOOPS, SystemTesting.addedfields));
		}
		for (Thread thread : delannotationthreads) {
			thread.start();
			Thread.sleep(5);
		}

		for (Thread thread : delannotationthreads) {
			thread.join();
		}
		float per;
		if (Values.incdeleteexptot != 0) {
			per = (float) Values.incsendexpaccTest
					/ (float) Values.incdeleteexptot * 100;

		} else {
			per = 0;
		}
		System.out.print("Delete experiments: succ test: "
				+ Values.incsendexpaccTest + "  tottests: "
				+ Values.incdeleteexptot + " percent:" + per);

		SystemTesting.writeWeb
				.createTestSection("Test: Delete experiments: "
						+ Values.NTHREADS
						+ " Thread * "
						+ Values.NLOOPS
						+ " <br>"
						+ "Test info: Checking if the server return response code \"200\".<br> Test date: "
						+ SystemTesting.dateFormat.format(SystemTesting.date)
						+ "<br><br>");
		SystemTesting.writeWeb.writeToHTML((int) per);

	}

}
