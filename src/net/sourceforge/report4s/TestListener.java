package net.sourceforge.report4s;

import org.testng.*;

/**
 * The test listener.<br>
 * @author Harmin Parra Rueda
 */
public class TestListener extends TestListenerAdapter {

	/**
	 * Whether this listener has been registered in the testng xml file.
	 */
	protected static boolean registered;
	
	/**
	 * Whether a test method is running.
	 */
	protected static boolean running;
	
	/**
	 * Whether a log has been added to the test method report.
	 */
	protected static boolean printing;

	/**
	 * Whether an exception has been logged in the test report.
	 */
	protected static boolean exception_logged = false;
	
	/**
	 * An instantiation indicates that the listener was added in the testng xml file.
	 */
	public TestListener() {
		registered = true;
	}

	/**
	 * Invoked each time before a test method is invoked.
	 */
	@Override
	public void onTestStart(ITestResult result) {
		if( !verifyPrecondition() )
			return;

		//Force the test to skip if the necessary conditions are met.
		if( SuiteListener.test_failure && Report4s.skipSuiteAfterTestFailure ) {
			result.setEndMillis(result.getStartMillis());
			result.setStatus(ITestResult.SKIP);
			onTestSkipped(result);
			throw new SkipSuiteException();
		}
		//Otherwise, start the test
		SuiteListener.methodCount++;
		startTestReport(result);
	}

	/**
	 * Invoked each time a test method succeeds.
	 */
	@Override
	public void onTestSuccess(ITestResult result) {
		if( !verifyPrecondition() )
			return;
		endTestReport(result);
	}

	/**
	 * Invoked each time a test method fails.
	 */
	@Override
	public void onTestFailure(ITestResult result) {
		if( !verifyPrecondition() )
			return;
		if( result.getThrowable() instanceof SkipSuiteException )
			return;
		if( !(result.getThrowable() instanceof SkipTestException) ) {
			//Print the exception trace in the standard error output device. 
			result.getThrowable().printStackTrace();
			//Print the exception trace in the test report.
			if( !exception_logged )
				Report4s.logTrace(result.getThrowable());
		}
		SuiteListener.test_failure = true;
		endTestReport(result);
	}

	/**
	 * Invoked each time a test method is skipped.
	 */
	@Override
	public void onTestSkipped(ITestResult result) {
		if( !verifyPrecondition() )
			return;
		SuiteListener.methodCount++;
		endTestReport(result);
	}

	/**
	 * Invoked each time a test method fails within the success percentage.
	 */
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		if( !verifyPrecondition() )
			return;
		endTestReport(result);
	}
	
	/**
	 * Print the table header of a test method report. 
	 */
	private void startTestReport(ITestResult result) {
		HtmlWriter.printTestTitle(result);
		HtmlWriter.openTable();
		running = true;
		exception_logged = false;
	}

	/**
	 * Print the closing </table> tag and the execution time label of a test method report.
	 */
	private void endTestReport(ITestResult result) {
		Status status = Utils.getTestStatus(result);
		
		if( status != Status.SKIPPED ) {
			//Print the execution time.
			long time = result.getEndMillis() - result.getStartMillis();
			HtmlWriter.closeTable();
			HtmlWriter.printExecutionTime(Utils.getExecutionLabel(time));			
		} else
			HtmlWriter.printTestTitle(result);

		//Print the test status
		if( status == Status.FAILED || status == Status.SKIPPED )
			HtmlWriter.printTestStatus(status);
		if( status == Status.FAILED_WITHIN_PERCENTAGE )
			HtmlWriter.printTestStatus(status, Utils.getSuccessPercentage(result));
		
		HtmlWriter.printLineBreak();
		
		running = false;
		printing = false;
		exception_logged = false;
		addMetadata(result);
	}
	
	/**
	 * Add meta-data of a test method for the html and Excel reports.
	 * @param result The test under execution.
	 */
	private void addMetadata(ITestResult result) {
		String method_name = Utils.getMethod(result);
		String method_parameters = Utils.getParameters(result);
		Status status = null;
		switch(result.getStatus()) {
			case ITestResult.SUCCESS : status = Status.PASSED; break;
			case ITestResult.FAILURE : status = Status.FAILED; break;
			case ITestResult.SKIP : status = Status.SKIPPED; break;
			case ITestResult.SUCCESS_PERCENTAGE_FAILURE : status = Status.FAILED_WITHIN_PERCENTAGE; break;
		}			
		long time = result.getEndMillis() - result.getStartMillis();
		int order = SuiteListener.methodCount;
		Metadata.addMethod(method_name, method_parameters, status, time, order);
	}
	
	/**
	 * Whether the conditions are met before logging.
	 * @return
	 */
	private boolean verifyPrecondition() {
		return Report4s.extracted && !SuiteListener.multi_threaded;
	}

}
