package experiment;

import java.io.IOException;

import pvt14servertest.SystemTesting;
import pvt14servertest.Values;
import pvt14servertest.resultClass;

public class RunSendExperiment {

	public static void initsendexptest() throws Exception, IOException {
		long startTime = System.currentTimeMillis();
		testSendExp();
		long endTime = System.currentTimeMillis();
		System.out.println("  " + (double) ((endTime) - (startTime)) / 1000
				+ "s");

		System.out.println(resultClass.getInstance().getMap().toString());
		SystemTesting.writeWeb.createTestSection("Failures:<br>"
				+ resultClass.getInstance().getMap().toString());
		SystemTesting.writeWeb.endTest();
	}

	private static void testSendExp() throws IOException, InterruptedException {
		Thread[] sendexp = new Thread[Values.NTHREADS];
		for (int i = 0; i < sendexp.length; i++) {
			sendexp[i] = new Thread(new TestSendExperiment(Values.NLOOPS,
					SystemTesting.addedexps));
		}
		for (Thread thread : sendexp) {
			thread.start();
			Thread.sleep(5);
		}

		for (Thread thread : sendexp) {
			thread.join();
		}
		float per;
		if (Values.sendexptotTest != 0) {
			per = (float) Values.sendexpaccTest / (float) Values.sendexptotTest
					* 100;

		} else {
			per = 0;
		}
		System.out.print("Send experiment create: succ test: "
				+ Values.sendexpaccTest + "  tottests: "
				+ Values.sendexptotTest + " percent:" + per);

		SystemTesting.writeWeb
				.createTestSection("Test: Send experiment create: "
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
