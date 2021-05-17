package net.sourceforge.report4s;

import java.io.*;
import org.testng.ITestResult;

/**
 * The html file writer.
 * @author Harmin Parra Rueda
 */
class HtmlWriter {

	/**
	 * File writer for the creation of the html files.
	 */
	private static PrintWriter writer;

	/**
	 * Store the table header of a {test|configuration} method report.
	 */
	private static String table_header;
		
	/**
	 * Create/open a file.
	 * @param filename The name of the file to be created/opened.
	 * @param append If true, then write to the end of the file rather than at the beginning.
	 */
	protected static void openFile(String filename, boolean append) {
		try {
			File file = new File(filename);
			file.createNewFile();
			writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, append), "UTF-8"));
		} catch (IOException e) {
			System.err.println("Failed to create/open file " + filename);
		}
	}
 
	/**
	 * Close the current opened file.
	 */
	protected static void closeFile() {
		if( writer != null ) {
			writer.flush();
			writer.close();
		}
		writer = null;
	}

	/**
	 * Print a string and terminates the line.
	 * @param string The string to print.
	 */
	protected static void println(String string) {
		if( writer == null )
			return;
		writer.println(string);
	}

	/**
	 * Print two <br> tags.
	 */
	protected static void printLineBreak() {
		println("\n" + "  <br><br>" + "\n");
	}

	/**
	 * Print the html <head> tag of a suite html report file.
	 * @param count The counter of the suite.
	 */
	protected static void printSuiteHead(int count) {
	    println(
    		"<html>" + "\n" +
    		"<head>" + "\n" +
    		"  <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />" + "\n" +
    		"  <title>Suite-" + count + "</title>" + "\n" +
    		"  <link rel='stylesheet' type='text/css' href='resources/css/design.css' />" + "\n" +
    		"  <script src='resources/js/events.js'></script>" + "\n" +
    		"</head>" + "\n\n" +
    		"<body>");
	}
	
	/**
	 * Print the name of a suite.
	 * @param name The name of the suite.
	 */
	protected static void printSuiteName(String name) {
		println("  <h2>" + name + "</h2>" + "\n");
	}
	
	/**
	 * Print the end of a suite html report file.
	 */
	protected static void printSuiteTail() {
		println("</body>");
		println("</html>");
	    closeFile();
	}
	
	/**
	 * Print the description, name and parameters of a test method.
	 * @param result The test under execution.
	 */
	protected static void printTestTitle(ITestResult result) {
		String description = Utils.getDescription(result);
		String clazz = Utils.getClazz(result);
		String method = Utils.getMethod(result);
		String parameters = Utils.getParameters(result);

		println("  <a name='test_" + SuiteListener.methodCount + "'></a>");
		if( description != null && !description.isEmpty() )
			println("  <span class='description'>" + "Description: " + description + "</span><br>");
		println("  <span class='method'>Method: " + clazz + "." + "<span class='method_name'>" + method + "</span></span><br>");
		if( parameters != null )
			println("  <span class='parameters'>Parameters: " + parameters + "</span><br>");
	}

	/**
	 * Print the status of a test method execution.
	 * @param status The execution status.
	 */
	protected static void printTestStatus(Status status) {
		switch(status) {
			case SKIPPED : println("  <span class='test_skipped'>Test skipped</span><br>"); break;
			case FAILED : println("  <span class='test_failed'>Test failed</span><br>"); break;
			default: break;
		}
	}

	/**
	 * Print the status of a test method execution.
	 * @param status The execution status.
	 * @param percentage The success percentage of the failed test.
	 */
	protected static void printTestStatus(Status status, int percentage) {
		if( status == Status.FAILED_WITHIN_PERCENTAGE )
			println("  <span class='test_failed_pct'>Test failed but within success percentage of " + percentage + "%</span><br>");
	}

	/**
	 * Print the execution time of a test method execution.
	 * @param time The execution time.
	 */
	protected static void printExecutionTime(String time) {
		println("  <span class='execution_time'>Execution time: " + time + "</span><br>");
	}
	
	/**
	 * Create the table header of a {test|configuration} execution report
	 * and store it in a temporary variable.
	 * This is to avoid printing the table header if the final-user
	 * doesn't include any log in the current test script being executed.
	 */
	protected static void openTable() {
		table_header = (table_header == null) ? "" : table_header;
		table_header +=
			"  <table id='tableStyle' class='width_suite row_test'>" + "\n" +
			"    <tr><th style='width:100px'>Status</th><th>Description</th><th style='width:100px' name='screenshot_test_" + SuiteListener.methodCount + "'>Screenshot</th></tr>";		
	}

	/**
	 * Print the table header of a {test|configuration} execution report.
	 */
	private static void printTableHead() {
		if( table_header != null )
			println(table_header);
		table_header = null;
	}

	/**
	 * Print the end of the table of a {test|configuration} execution report.
	 */
	protected static void closeTable() {
		if( TestListener.printing || ConfigurationListener.printing )
			println("  </table>");
		table_header = null;
	}
	
	/**
	 * Print the annotation and name of a configuration method.
	 * @param result The configuration under execution.
	 */
	protected static void printConfigurationTitle(ITestResult result) {
		String clazz = Utils.getClazz(result);
		String method = Utils.getMethod(result);
		String annotation = Utils.getConfigurationAnnotation(result);

		table_header =
			"  <span class='annotation'>" + annotation + "</span><br>" + "\n" +
			"  <span class='method'>Configuration: " + clazz + "." + "</span><span class='method_name'>" + method + "</span><br>" + "\n";
	}
	
	/**
	 * Print the execution result of configuration method.
	 * @param result The configuration under execution.
	 */
	protected static void printConfigurationStatus(Status status) {
		switch(status) {
			case SKIPPED :
				HtmlWriter.printTableHead();
				println("  <span class='test_skipped'>Configuration skipped</span><br>");
				printLineBreak();
				break;
			case FAILED :
				closeTable();
				println("  <span class='test_failed'>Configuration failed</span><br>");
				printLineBreak();
				break;
			case PASSED :
				if( ConfigurationListener.printing ) {
					closeTable();
					printLineBreak();
				}
				break;
			default :
				break;
		}
		table_header = null;
	}
	
	/**
	 * Append a log to the {test|configuration} execution report.
	 * @param icon The content of the html <td> tag of the icon cell.
	 * @param message The message to log.
	 * @param link The content of the html <td> tag of the screenshot cell.
	 */
	protected static void printLog(String icon, String message, String link) {
		if( link == null )
			link = "";
		
		String log = 
			"    <tr>" + "\n" +
			"      <td>" + icon + "</td>" + "\n" +
			"      <td>" + message + "</td>" + "\n" +
			"      <td align='center'>" + link + "</td>" + "\n" +
			"    </tr>";
		printTableRow(log);
	}

	/**
	 * Append a log to the {test|configuration} execution report.
	 * @param log The html <tr> tag to append.
	 */
	protected static void printTableRow(String log) {
		if( TestListener.running )
			TestListener.printing = true;
		else if( ConfigurationListener.running )
			ConfigurationListener.printing = true;
		HtmlWriter.printTableHead();
		HtmlWriter.println(log);
	}
	
	/**
	 * Print a message in the presence of multi-threaded tests.
	 */
	protected static void printMultiThreadMessage() {
		println("  <span class='multithread'>No support for multi-threaded tests</span>");
	}

}
