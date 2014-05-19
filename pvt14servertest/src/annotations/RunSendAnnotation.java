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
public class RunSendAnnotation {


    /**
     * Init annotations test , mainly the prints to console and web
     *
     * @throws java.io.IOException
     * @throws InterruptedException
     */
    public static void initaddannotest() throws IOException, InterruptedException {
        long startTime;
        long endTime;
        startTime = System.currentTimeMillis();
        testsendAnnotations();
        endTime = System.currentTimeMillis();
        System.out.println("  " + (double) ((endTime) - (startTime)) / 1000
                + "s");

        System.out.println(resultClass.getInstance().getMap().toString());
        SystemTesting.writeWeb.createTestSection("Failures:<br>"
                + resultClass.getInstance().getMap().toString());
        SystemTesting.writeWeb.endTest();

    }

    /**
     * start the Tests: get annotations
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private static void testsendAnnotations() throws IOException,
            InterruptedException {
        Thread[] annotationthreads = new Thread[Values.NTHREADS];
        for (int i = 0; i < annotationthreads.length; i++) {
            annotationthreads[i] = new Thread(
                    new TestAnnotations(Values.NLOOPS));
        }
        for (Thread thread : annotationthreads) {
            thread.start();
            Thread.sleep(5);
        }

        for (Thread thread : annotationthreads) {
            thread.join();
        }
        float per;
        if (Values.getanntotTest != 0) {
            per = (float) Values.getannaccTest / (float) Values.getanntotTest
                    * 100;

        } else {
            per = 0;
        }
        System.out.print("Annotations: succ test: " + Values.getannaccTest
                + "  tottests: " + Values.getanntotTest + " percent:" + per);

        SystemTesting.writeWeb
                .createTestSection("Test:Get Annotations: "
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
