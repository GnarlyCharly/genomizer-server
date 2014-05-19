package pvt14servertest;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import annotations.RunAddAnnotation;
import annotations.RunDelAnnotation;
import loginlogout.RunLogin;
import loginlogout.RunLoginFail;
import loginlogout.RunLogout;
import search.RunSearch;
import annotations.RunGetAnnotations;
import experiment.RunDeleteExperiment;
import experiment.RunSendExperiment;

public class SystemTesting {
    public static Token token;
    public static final int PORT = 7000;
    public static final String server = "http://scratchy.cs.umu.se:";
    public static final String htmlfile = "/home/c08/c08esn/testres.html";

    public static ToWeb writeWeb;

    public static DateFormat dateFormat;
    public static Date date;

    private static RunLogin runlogin = new RunLogin();
    private static RunLoginFail runloginfail = new RunLoginFail(
            "Testing if invalid information fails");
    private static loginlogout.RunLogout runlogout = new RunLogout();

    private static RunGetAnnotations rungetannotations = new RunGetAnnotations();
    private static RunAddAnnotation runsendannotations = new RunAddAnnotation();
    private static RunDelAnnotation rundelannotations = new RunDelAnnotation();

    private static RunSendExperiment runsendexp = new RunSendExperiment();
    private static RunDeleteExperiment rundelexp = new RunDeleteExperiment();

    private static RunSearch runsearch = new RunSearch();

    public static AddedFields addedfields = new AddedFields();

    public static void main(String args[]) throws Exception {
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        date = new Date(System.currentTimeMillis());
        writeWeb = new ToWeb();
        writeWeb.starthtml();
        runtests();
        writeWeb.endhtml();

    }

    private static void runtests() throws IOException, Exception {
        runlogin.initlogintest();
        betweenTest();
        runloginfail.initloginfailtest();
        betweenTest();
        runlogout.initlogouttest();
        betweenTest();
        rungetannotations.initannotest();
        betweenTest();
        runsendexp.initsendexptest();
        betweenTest();
        rundelexp.initdeleteexptest();
        betweenTest();
        runsearch.initsearchtest();
        betweenTest();
        runsendannotations.initaddannotest();
        betweenTest();
        rundelannotations.initdelannotest();

    }

    private static void betweenTest() {
        System.out.flush();
        resultClass.getInstance().getMap().clear();
    }

}