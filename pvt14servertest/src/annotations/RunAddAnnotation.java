package annotations;

import pvt14servertest.SystemTesting;
import pvt14servertest.Values;
import pvt14servertest.resultClass;

import java.io.IOException;

/**
 * Project: genomizer-Server
 * Package: annotations
 * User: c08esn
 * Date: 5/19/14
 * Time: 11:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class RunAddAnnotation {


    /**
     * Init annotations test , mainly the prints to console and web
     *
     */
    public void initaddannotest() {

        long startTime = System.currentTimeMillis();
        try {
            testsendAnnotations();
        long endTime = System.currentTimeMillis();

        System.out.println("  " + (double) ((endTime) - (startTime)) / 1000
                + "s");
        System.out.println(resultClass.getInstance().getMap().toString());
        SystemTesting.writeWeb.createTestSection("Failures:<br>"
                + resultClass.getInstance().getMap().toString());
        SystemTesting.writeWeb.endTest();
        } catch (IOException e) {
            System.err.println("Add annotations failed");
        } catch (InterruptedException e) {
            System.err.println("Add annotations failed");
        }

    }

    /**
     * start the Tests: get annotations
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private  void testsendAnnotations() throws IOException,
            InterruptedException {
        float percent;

        Thread[] threads = new Thread[Values.NTHREADS];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(
                    new TestAddAnnotation());
        }
        for (Thread thread : threads) {
            thread.start();
            Thread.sleep(5);
        }

        for (Thread thread : threads) {
            thread.join();
        }

        if (Values.addannotot != 0) {
            percent = (float) Values.addannoacc / (float) Values.addannotot
                    * 100;
        } else {
            percent = 0;
        }
        System.out.print("Add Annotations: success: " + Values.addannoacc
                + "  tottests: " + Values.addannotot + " percent: " + percent+"%");

        SystemTesting.writeWeb
                .createTestSection("Test:Add Annotations: "
                        + Values.NTHREADS
                        + " Thread * "
                        + Values.NLOOPS
                        + " <br>"
                        + "Test info: Checking if annotation has been added.<br> Test date: "
                        + SystemTesting.dateFormat.format(SystemTesting.date)
                        + "<br><br>");
        SystemTesting.writeWeb.writeToHTML((int) percent);
    }

}
