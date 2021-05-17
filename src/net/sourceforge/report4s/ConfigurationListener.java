package net.sourceforge.report4s;

import org.testng.*;

/**
 * The configuration listener.<br>
 * @author Harmin Parra Rueda
 */
public class ConfigurationListener implements IConfigurationListener2 {

	/**
	 * Whether this listener has been registered in the testng xml file.
	 */
	protected static boolean registered;
	
	/**
	 * Whether a configuration method is running.
	 */
	protected static boolean running;
	
	/**
	 * Whether a log has been added to the configuration method.
	 */
	protected static boolean printing;

	/**
	 * An instantiation indicates that the listener was added in the testng xml file.
	 */
	public ConfigurationListener() {
		registered = true;
	}

	/**
	 * Invoked each time before a configuration method is invoked.
	 */
	@Override
	public void beforeConfiguration(ITestResult result) {
		if( !verifyPrecondition() )
			return;
		startConfigurationReport(result);
	}

	/**
	 * Invoked each time a configuration method succeeds.
	 */
	@Override
	public void onConfigurationSuccess(ITestResult result) {
		if( !verifyPrecondition() )
			return;
		endConfigurationReport(result);
	}

	/**
	 * Invoked each time a configuration method fails.
	 */
	@Override
	public void onConfigurationFailure(ITestResult result) {
		if( !verifyPrecondition() )
			return;
		Report4s.logTrace(result.getThrowable());
		endConfigurationReport(result);
		Metadata.addConfigurationFailed();
	}

	/**
	 * Invoked each time a configuration method is skipped.
	 */
	@Override
	public void onConfigurationSkip(ITestResult result) {
		if( !verifyPrecondition() )
			return;
		HtmlWriter.printConfigurationTitle(result);
		endConfigurationReport(result);
		Metadata.addConfigurationSkipped();
	}

	/**
	 * Print the table header of a configuration method report. 
	 */
	private void startConfigurationReport(ITestResult result) {
		HtmlWriter.printConfigurationTitle(result);
		HtmlWriter.openTable();
		running = true;
	}

	/**
	 * Print the closing </table> and the execution status of a configuration report.
	 */
	private void endConfigurationReport(ITestResult result) {
		HtmlWriter.printConfigurationStatus(Utils.getTestStatus(result));
		running = false;
		printing = false;
	}

	/**
	 * Whether the conditions are met before logging.
	 * @return
	 */
	private boolean verifyPrecondition() {
		return Report4s.extracted && !SuiteListener.multi_threaded;
	}
	
}
