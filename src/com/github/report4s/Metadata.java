package com.github.report4s;

import java.util.ArrayList;
import java.util.List;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;

/**
 * A holder of suites meta-data.
 * Meta-data is information needed in order to build both the HTML and the Excel summary reports.
 */
class Metadata {

    protected static int suites_passed,
                         suites_failed,
                         suites_skipped,
                         suites_passed_warning,
                         exec_time;

    /**
     * The list of suites meta-data.
     */
    protected static List<SuiteMetadata> suites = new ArrayList<SuiteMetadata>();

    /**
     * Meta-data of the current suite being executed.
     */
    private static SuiteMetadata currentSuite;

    /**
     * Create a new suite meta-data without adding it to the list of suites.
     */
    protected static void createSuite() {
        currentSuite = new SuiteMetadata();
    }

    /**
     * Set the name of the current suite being executed.
     * @param suite The suite under execution.
     */
    protected static void setName(ISuite suite) {
        currentSuite.name = suite.getName();
    }

    /**
     * Set the report filename of the current suite being executed.
     * @param filename The filename.
     */
    protected static void setFilename(String filename) {
        currentSuite.filename = filename;
    }

    /**
     * Set the execution time of the current suite being executed.
     * @param time The execution time.
     */
    protected static void setExecTime(long time) {
        exec_time += time;
        currentSuite.time = Utils.getExecutionLabel(time);
    }

    /**
     * Set the current suite being executed as having multi-threaded tests.
     */
    protected static void setMultiThreaded() {
        currentSuite.hasMutiThreads = true;
        currentSuite.status = Status.UNKNOWN;
    }

    /**
     * Add a method meta-data to the current suite being executed.
     * @param name The method's name.
     * @param parameters The method's parameters.
     * @param status The method's execution status.
     * @param time The method's execution time.
     * @param order The order of execution of the method within the suite.
     */
    protected static void addMethod(String name, String parameters, Status status, long time, int order) {
        currentSuite.addMethod(name, parameters, status, time, order);
        switch (status) {
            case PASSED:
                currentSuite.tests_passed++;
                break;
            case FAILED:
                currentSuite.tests_failed++;
                break;
            case SKIPPED:
                currentSuite.tests_skipped++;
                break;
            case FAILED_WITHIN_PERCENTAGE:
                currentSuite.tests_failed_pct++;
                break;
            default:
                break;
        }
    }

    /**
     * Add the current suite meta-data to the list of suites.
     */
    protected static void addSuite() {
        if (currentSuite.methods.size() > 0 || currentSuite.hasMutiThreads)
            suites.add(currentSuite);
        currentSuite = null;
    }

    /**
     * Return a given suite meta-data.
     * @param index The index to return.
     * @return The suite meta-data.
     */
    protected static SuiteMetadata get(int index) {
        return suites.get(index);
    }

    /**
     * Return the number of suites.
     * @return The number of suites.
     */
    protected static int size() {
        return suites.size();
    }

    /**
     * Calculate the status and test aggregations of the suite based on the testng results.
     * @param suite The suite under execution.
     */
    protected static void calculateAggregations(ISuite suite) {
        currentSuite.calculateAggregations(suite);
    }

    /**
     * Increment the counter of failed configurations.
     */
    protected static void addConfigurationFailed() {
        currentSuite.conf_failed++;
    }

    /**
     * Increment the counter of skipped configurations.
     */
    protected static void addConfigurationSkipped() {
        currentSuite.conf_skipped++;
    }

}


/**
 * A suite meta-data.
 * Meta-data is information needed in order to build the report homepage (index) html file.
 * @author Harmin Parra Rueda
 */
class SuiteMetadata {
    protected String name;      //The suite name
    protected String filename;  //The filename of the suite's report
    protected String time;      //The suite execution time
    protected Status status;    //The suite execution status

    /**
     * Whether this suite has multi-threaded tests.
     */
    protected boolean hasMutiThreads;

    /**
     * Counters of passed, failed and skipped tests.
     */
    protected int tests_passed, tests_failed, tests_skipped, tests_failed_pct;

    /**
     * Counter of failed and skipped configurations.
     */
    protected int conf_failed, conf_skipped;

    /**
     * The list of the test methods meta-data of the suite.
     */
    protected List<MethodMetadata> methods = new ArrayList<MethodMetadata>();

    /**
     * Add a new test method meta-data.
     * @param name The method name.
     * @param parameters The method parameters.
     * @param status The method execution status.
     * @param time The method execution time.
     * @param order In order of execution of the method within the suite.
     */
    protected void addMethod(String name, String parameters, Status status, long time, int order) {
        this.methods.add(new MethodMetadata(name, parameters, status, time, order));
    }

    /**
     * Return a given test method meta-data.
     * @param index The index to return.
     * @return The method meta-data at the corresponding index.
     */
    protected MethodMetadata getMethod(int index) {
        return this.methods.get(index);
    }

    /**
     * Calculate the status of the suite.
     */
    private void calculateStatus() {
        if (this.tests_passed + this.tests_failed + this.tests_skipped + this.tests_failed_pct == 0)
            return;
        if (this.tests_failed > 0 ) {
            this.status = Status.FAILED;
            Metadata.suites_failed++;
        }
        else if (this.tests_passed > 0 && this.tests_skipped == 0 && this.tests_failed_pct == 0) {
            if (this.conf_failed == 0) {
                this.status = Status.PASSED;
                Metadata.suites_passed++;
            } else {
                this.status = Status.PASSED_WITH_WARNING;
                Metadata.suites_passed_warning++;
            }
        }
        else if (this.tests_skipped > 0 && this.tests_passed == 0 && this.tests_failed_pct == 0) {
            this.status = Status.SKIPPED;
            Metadata.suites_skipped++;
        }
        else {
            this.status = Status.PASSED_WITH_WARNING;
            Metadata.suites_passed_warning++;
        }
    }

    /**
     * Calculate the status and test aggregations of the suite based on testng results.
     * @param suite The suite to be aggregated.
     */
    protected void calculateAggregations(ISuite suite) {
        if (this.hasMutiThreads)
            for (ISuiteResult result : suite.getResults().values()) {
                ITestContext tc = result.getTestContext();
                this.tests_passed += tc.getPassedTests().size();
                this.tests_failed += tc.getFailedTests().size();
                this.tests_failed_pct += tc.getFailedButWithinSuccessPercentageTests().size();
                this.tests_skipped += tc.getSkippedTests().size();
                this.conf_failed += tc.getFailedConfigurations().size();
            }
        calculateStatus();
    }

    /**
     * Return the number of test methods of the suite.
     * Not to be used if the suite has multi-threaded tests.
     * @return The number of methods of the suite.
     */
    protected int size() {
        return this.methods.size();
    }

}


/**
 * A test method meta-data.
 * Meta-data is information needed in order to build the report homepage (index) html file.
 */
class MethodMetadata {

    protected String name;        //The test name
    protected String parameters;  //The test parameters
    protected Status status;      //The test execution status
    protected String time;        //The test execution time
    protected int order;          //The test order of execution within the suite

    public MethodMetadata(String name, String parameters, Status status, long time, int order) {
        this.name = name;
        this.parameters = parameters;
        this.time = Utils.getExecutionLabel(time);
        this.status = status;
        this.order = order;
    }

}
