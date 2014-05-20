package filetransfer;

import pvt14servertest.SystemTesting;
import pvt14servertest.Values;
import pvt14servertest.resultClass;

import java.io.IOException;

public class RunTestDownload {

    /**
     * Init login tests, mainly prints to console and web
     *
     */
    public  void initdownloadtest()  {
        try {
            long startTime = System.currentTimeMillis();
            testDownload();
            long endTime = System.currentTimeMillis();

            System.out.println("  " + (double) ((endTime) - (startTime)) / 1000
                    + "s");
            System.out.println(resultClass.getInstance().getMap().toString());
            SystemTesting.writeWeb.createTestSection("Failures:<br>"
                    + resultClass.getInstance().getMap().toString());
            SystemTesting.writeWeb.endTest();
        } catch (IOException e){
            System.err.println("Downloadtest failed");
        } catch (InterruptedException e) {
            System.err.println("Downloadtest failed");
        }
    }

    /**
     * Start the login tests
     *
     */
    private  void testDownload() throws InterruptedException, IOException {
        float percent;
        TestDownload[] threads = new TestDownload[Values.NTHREADS];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new TestDownload();
        }
        for (Thread thread : threads) {
            thread.start();
            Thread.sleep(5);
        }
        for (Thread thread : threads) {
            thread.join();
        }
        if (Values.dlfiletot != 0) {
            percent = (float) Values.dlfileacc / (float) Values.dlfiletot
                    * 100;

        } else {
            percent = 0;
        }
        System.out.print("Download test: " + Values.dlfileacc
                + "  tot tests : " + Values.dlfiletot + "   percent:" + percent+"%");

        SystemTesting.writeWeb
                .createTestSection("Test: Login "
                        + Values.NTHREADS
                        + " Thread * "
                        + Values.NLOOPS
                        + " [Random Unames/Password]<br>Test info:"
                        + "TEsting if the file is downloaded<br> Test date: "
                        + pvt14servertest.SystemTesting.dateFormat
                        .format(pvt14servertest.SystemTesting.date)
                        + "<br><br>");

        SystemTesting.writeWeb.writeToHTML((int) percent);

    }

}
