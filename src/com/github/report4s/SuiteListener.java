package com.github.report4s;

import java.io.File;
import org.testng.ISuite;
import org.testng.ISuiteListener;

/**
 * Report4s suite listener.
 * Creates an individual html file report for each suite.
 */
public class SuiteListener implements ISuiteListener {

    /**
     * Whether this listener has been registered in the testng xml file.
     */
    protected static boolean registered;

    /**
     * Start and end execution time of a suite.
     */
    private long startTime, endTime;

    /**
     * The html filename of the suite report.
     */
    private String filename;

    /**
     * Counter used in order to name the suite report files.
     */
    private int suiteCount = 1;

    /**
     * Counter used to build internal html <a> references.
     */
    protected static int methodCount;

    /**
     * Counter used in order to set trace HTML elements id.
     */
    protected static int traceCount;

    /**
     * Whether a test has failed during the suite execution.
     */
    protected static boolean test_failure;

    /**
     * Whether a configuration has failed during the suite execution.
     */
    protected static boolean conf_failure;

    /**
     * Whether the suite under execution contains multi-threaded tests.
     */
    protected static boolean multi_threaded;

    /**
     * An instantiation indicates that the listener was added in the testng xml file.
     */
    public SuiteListener() {
        registered = true;
    }

    /**
     * This method is invoked before the SuiteRunner starts.
     * Starts the suite execution report.
     */
    @Override
    public void onStart(ISuite suite) {
        if(!verifyPrecondition())
            return;
        //Create and initialize the html file
        setFilename();
        HtmlWriter.openFile(Report4s.report_dir + File.separator + filename, false);
        HtmlWriter.printSuiteHead(suiteCount);
        if (suite.getName() != null)
            HtmlWriter.printSuiteName(suite.getName());
        this.startTime = Utils.getTimeInMillisec();
        methodCount = 0;
        traceCount = 0;
        test_failure = false;
        conf_failure = false;

        //Create an empty suite metadata for the report index.
        Metadata.createSuite();

        //Determine whether this suite is multi-threaded.
        multi_threaded = Utils.hasMultiThreadedTests(suite);
        if (multi_threaded) {
            HtmlWriter.printMultiThreadMessage();
            Metadata.setMultiThreaded();
        }
        TestListener.driver = null;
    }

    /**
     * This method is invoked after the SuiteRunner has run all the test suites.
     * Finalizes the suite execution report.
     */
    @Override
    public void onFinish(ISuite suite) {
        if (!verifyPrecondition())
            return;
        this.endTime = Utils.getTimeInMillisec();
        HtmlWriter.printSuiteTail();
        this.suiteCount++;
        //Update and add the suite metadata for the report index.
        Metadata.setName(suite);
        Metadata.setFilename(filename);
        Metadata.setExecTime(endTime - startTime);
        Metadata.calculateAggregations(suite);
        Metadata.addSuite();
        TestListener.driver = null;
    }

    /**
     * Set the html filename of the suite report.
     */
    private void setFilename() {
        filename = "suite-" + suiteCount + ".html";
    }

    /**
     * Whether the conditions are met before logging.
     * @return
     */
    private boolean verifyPrecondition() {
        return Report4s.extracted;
    }

}
