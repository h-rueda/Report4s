//package net.sourceforge.report4s;
//
//import java.io.File;
//import java.util.List;
//import org.testng.*;
//import org.testng.xml.*;
//
///**
// * Implementation of the following interfaces :
// * IReporter
// * ISuiteListener
// * ITestListener
// * IConfigurationListener2
// * @author Harmin Parra Rueda
// */
//class Listeners implements IReporter, ISuiteListener, ITestListener, IConfigurationListener2 {
//
//	/**
//	 * Whether this listener has been registered in the testng xml file.
//	 */
//	protected static boolean registered;
//
//	/**
//	 * An instantiation indicates that the listener was added in the testng xml file.
//	 */
//	public Listeners() {
//		registered = true;
//	}
//
//	/************************************************************
//	 *                    THE SUITE LISTENER
//	 ***********************************************************/
//	
//	/**
//	 * Start and end execution time of a suite.
//	 */
//	private long startTime, endTime;
//
//	/**
//	 * The html filename of the suite report.
//	 */
//	private String filename;
//
//	/**
//	 * Counter used in order to name the suite report files.
//	 */
//	private int suiteCount = 1;
//
//	/**
//	 * Counter used to build internal html <a> references.
//	 */
//	protected static int methodCount;
//
//	/**
//	 * Counter used in order to set trace html elements id.
//	 */
//	protected static int traceCount;
//	
//	/**
//	 * Whether a test has failed during the suite execution.
//	 */
//	protected static boolean test_failure;
//	
//	/**
//	 * Whether the suite under execution contains multi-threaded tests.
//	 */
//	protected static boolean multi_threaded;
//	
//	/**
//	 * This method is invoked before the SuiteRunner starts.
//	 * Starts the suite execution report.
//	 */
//	@Override
//	public void onStart(ISuite suite) {
//		//Create and initialize the html file
//		setFilename();
//		HtmlWriter.openFile(Report4s.report_dir + File.separator + filename, false);
//		HtmlWriter.printSuiteHead(suiteCount);
//		if( suite.getName() != null )
//			HtmlWriter.printSuiteName(suite.getName());
//		startTime = Utils.getTimeInMillisec();
//		methodCount = 0;
//		traceCount = 0;
//		test_failure = false;
//		
//		//Create an empty suite metadata for the report index.
//		Metadata.createSuite();
//		
//		//Determine whether this suite is multi-threaded.
//		multi_threaded = Utils.hasMultiThreadedTests(suite);
//		if( multi_threaded ) {
//			HtmlWriter.printMultiThreadMessage();
//			Metadata.setMultiThreaded();
//		}
//	}
//
//	/**
//	 * This method is invoked after the SuiteRunner has run all the test suites.
//	 * Finalises the suite execution report.
//	 */
//	@Override
//	public void onFinish(ISuite suite) {
//		endTime = Utils.getTimeInMillisec();
//		HtmlWriter.printSuiteTail();
//	    suiteCount++;
//	    //Update and add the suite metadata for the report index.
//		Metadata.setName(suite);
//		Metadata.setFilename(filename);
//		Metadata.setExecTime(endTime - startTime);
//	    Metadata.calculateAggregations(suite);
//		Metadata.addSuite();
//	}
//	
//	/**
//	 * Set the html filename of the suite report.
//	 */
//	private void setFilename() {
//		filename = "suite-" + suiteCount + ".html";
//	}
//	
//	/************************************************************
//	 *                    THE TEST LISTENER
//	 ***********************************************************/
//
//	/**
//	 * Whether a @test-annotated method is running.
//	 */
//	protected static boolean running_test;
//	
//	/**
//	 * Whether a log has been added to the @test-annotated method report.
//	 */
//	protected static boolean printing_test;
//
//	/**
//	 * Whether an exception has been logged in the test report.
//	 */
//	protected static boolean exception_logged = false;
//
//	/**
//	 * Invoked each time before a @test-annotated method is invoked.
//	 */
//	@Override
//	public void onTestStart(ITestResult result) {
//		if( SuiteListener.multi_threaded )
//			return;
//
//		//Force the test to skip if the necessary conditions are met.
//		if( SuiteListener.test_failure && Report4s.skipSuiteAfterTestFailure ) {
//			result.setEndMillis(result.getStartMillis());
//			result.setStatus(ITestResult.SKIP);
//			onTestSkipped(result);
//			throw new SkipSuiteException();
//		}
//		//Otherwise, start the test
//		SuiteListener.methodCount++;
//		startTestReport(result);
//	}
//
//	/**
//	 * Invoked each time a @test-annotated method succeeds.
//	 */
//	@Override
//	public void onTestSuccess(ITestResult result) {
//		if( SuiteListener.multi_threaded )
//			return;
//		endTestReport(result);
//	}
//
//	/**
//	 * Invoked each time a @test-annotated method fails.
//	 */
//	@Override
//	public void onTestFailure(ITestResult result) {
//		if( SuiteListener.multi_threaded )
//			return;
//		if( result.getThrowable() instanceof SkipSuiteException )
//			return;
//		if( !(result.getThrowable() instanceof SkipTestException) ) {
//			//Print the exception trace in the standard error output device. 
//			result.getThrowable().printStackTrace();
//			//Print the exception trace in the test report.
//			if( !exception_logged )
//				Report4s.logTrace(result.getThrowable());
//		}
//		SuiteListener.test_failure = true;
//		endTestReport(result);
//	}
//
//	/**
//	 * Invoked each time a @test-annotated method is skipped.
//	 */
//	@Override
//	public void onTestSkipped(ITestResult result) {
//		if( SuiteListener.multi_threaded )
//			return;
//		SuiteListener.methodCount++;
//		endTestReport(result);
//	}
//
//	/**
//	 * Invoked each time a @test-annotated method fails within the success percentage.
//	 */
//	@Override
//	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
//		if( SuiteListener.multi_threaded )
//			return;
//		endTestReport(result);
//	}
//	
//	/**
//	 * Print the table header of a @test-annotated method report. 
//	 */
//	private void startTestReport(ITestResult result) {
//		HtmlWriter.printTestTitle(result);
//		HtmlWriter.openTable();
//		running_test = true;
//		exception_logged = false;
//	}
//
//	/**
//	 * Print the closing </table> tag and the execution time label of a @test-annotated method report.
//	 */
//	private void endTestReport(ITestResult result) {
//		Status status = Utils.getTestStatus(result);
//		
//		if( status != Status.SKIPPED ) {
//			//Print the execution time.
//			long time = result.getEndMillis() - result.getStartMillis();
//			HtmlWriter.closeTable();
//			HtmlWriter.printExecutionTime(Utils.getExecutionLabel(time));			
//		} else
//			HtmlWriter.printTestTitle(result);
//
//		//Print the test status
//		if( status == Status.FAILED || status == Status.SKIPPED )
//			HtmlWriter.printTestStatus(status);
//		if( status == Status.FAILED_WITHIN_PERCENTAGE )
//			HtmlWriter.printTestStatus(status, Utils.getSuccessPercentage(result));
//		
//		HtmlWriter.printLineBreak();
//		
//		running_test = false;
//		printing_test = false;
//		exception_logged = false;
//		addMetadata(result);
//	}
//	
//	/**
//	 * Add meta-data of a @test-annotated method for the html and Excel reports.
//	 * @param result The test under execution.
//	 */
//	private void addMetadata(ITestResult result) {
//		String method_name = Utils.getMethod(result);
//		String method_parameters = Utils.getParameters(result);
//		Status status = null;
//		switch( result.getStatus() ) {
//			case ITestResult.SUCCESS : status = Status.PASSED; break;
//			case ITestResult.FAILURE : status = Status.FAILED; break;
//			case ITestResult.SKIP : status = Status.SKIPPED; break;
//			case ITestResult.SUCCESS_PERCENTAGE_FAILURE : status = Status.FAILED_WITHIN_PERCENTAGE; break;
//		}			
//		long time = result.getEndMillis() - result.getStartMillis();
//		int order = SuiteListener.methodCount;
//		Metadata.addMethod(method_name, method_parameters, status, time, order);
//	}
//
//	/**
//	 * This method is invoked before a test starts.<br>
//	 * Not implemented by Report4s.
//	 */
//	@Override
//	public void onStart(ITestContext context) { }
//
//	/**
//	 * This method is invoked after a test has run all @test-annotated methods.<br>
//	 * Not implemented by Report4s.
//	 */
//	@Override
//	public void onFinish(ITestContext context) { }
//
//	/************************************************************
//	 *               THE CONFIGURATION LISTENER
//	 ***********************************************************/
//
//	/**
//	 * Whether a configuration method is running.
//	 */
//	protected static boolean running_configuration;
//	
//	/**
//	 * Whether a log has been added to the configuration method.
//	 */
//	protected static boolean printing_configuration;
//
//	/**
//	 * Invoked each time before a configuration method is invoked.
//	 */
//	@Override
//	public void beforeConfiguration(ITestResult result) {
//		if( SuiteListener.multi_threaded )
//			return;
//		startConfigurationReport(result);
//	}
//
//	/**
//	 * Invoked each time a configuration method succeeds.
//	 */
//	@Override
//	public void onConfigurationSuccess(ITestResult result) {
//		if( SuiteListener.multi_threaded )
//			return;
//		endConfigurationReport(result);
//	}
//
//	/**
//	 * Invoked each time a configuration method fails.
//	 */
//	@Override
//	public void onConfigurationFailure(ITestResult result) {
//		if( SuiteListener.multi_threaded )
//			return;
//		Report4s.logTrace(result.getThrowable());
//		endConfigurationReport(result);
//		Metadata.addConfigurationFailed();
//	}
//
//	/**
//	 * Invoked each time a configuration method is skipped.
//	 */
//	@Override
//	public void onConfigurationSkip(ITestResult result) {
//		if( SuiteListener.multi_threaded )
//			return;
//		HtmlWriter.printConfigurationTitle(result);
//		endConfigurationReport(result);
//		Metadata.addConfigurationSkipped();
//	}
//
//	/**
//	 * Print the table header of a configuration method report. 
//	 */
//	private void startConfigurationReport(ITestResult result) {
//		HtmlWriter.printConfigurationTitle(result);
//		HtmlWriter.openTable();
//		running_configuration = true;
//	}
//
//	/**
//	 * Print the closing </table> and the execution status of a configuration report.
//	 */
//	private void endConfigurationReport(ITestResult result) {
//		HtmlWriter.printConfigurationStatus(Utils.getTestStatus(result));
//		running_configuration = false;
//		printing_configuration = false;
//	}
//	
//	/************************************************************
//	 *              THE REPORT HOMEPAGE BUILDER
//	 ***********************************************************/
//
//	int suite_count;
//	
//	/**
//	 * invoked after all suites have run.
//	 * Generates the report homepage (index) file.
//	 */
//	@Override
//	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> isuites, String outputDirectory) {
//		//Generate the HTML summary report
//		HtmlWriter.openFile(Report4s.report_dir + File.separator + Report4s.report_homepage, false);
//		print_head();
//		print_rows();
//		print_tail();
//		HtmlWriter.closeFile();
//
//		//Generate JavaScript files
//		print_piecharts_data();
//		if( Report4s.suite_tooltips )
//			print_tooltip_data();
//
//		//Generate the Excel report
//		Excel.generateExcelReport();
//	}
//
//	/**
//	 * Print the beginning of the report homepage file.
//	 */
//	private void print_head() {
//		String content =
//			"<html>" + "\n" +
//			"<head>" + "\n" +
//			"  <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />" + "\n" +
//			"  <title>" + Report4s.report_title + "</title>" + "\n" +
//			"  <link rel='stylesheet' type='text/css' href='resources/css/design.css' />" + "\n" +
//			"  <link rel='stylesheet' type='text/css' href='resources/css/legend.css' />" + "\n" +
//			"  <link rel='stylesheet' type='text/css' href='resources/css/jquery-ui.css' />" + "\n" +
//			"  <script src='resources/js/Chart.js'></script>" + "\n" +
//			"  <script src='resources/js/legend.js'></script>" + "\n" +
//			"  <script src='resources/js/jquery.js'></script>" + "\n" +
//			"  <script src='resources/js/jquery-ui.js'></script>" + "\n" +
//			"  <script src='resources/js/piecharts.js'></script>" + "\n" +
//			"  <script src='resources/js/events.js'></script>" + "\n";
//		if( Report4s.suite_tooltips )
//			content +=
//			"  <script src='resources/js/tooltips.js'></script>" + "\n";
//		content +=
//			"</head>" + "\n\n" +
//			"<body>" + "\n" +
//			"  <h1 align='center'>" + Report4s.report_title + "</h1>" + "\n\n" +
//
//			"  <table id='table1' style='width:100%;display:none'>" + "\n" +
//			"    <tr>" + "\n" +
//			"      <td align='center' colspan='3'>" + "\n" +
//			"        <div id='chart1'>" + "\n" +
//			"          <canvas id='canvas1' width='240' height='240'></canvas>" + "\n" +
//			"        </div>" + "\n" +
//			"      </td>" + "\n" +
//			"    </tr>" + "\n" +
//			"    <tr>" + "\n" +
//			"      <td style='width:20%'></td>" + "\n" +
//			"      <td><div id='legend1'></div></td>" + "\n" +
//			"      <td style='width:20%'></td>" + "\n" +
//			"    </tr>" +  "\n" +
//			"  </table>" + "\n\n" +
//  					
//			"  <table id='table2' style='width:100%;display:none'>" + "\n" +
//			"    <tr>" + "\n" +
//			"      <td align='center' colspan='3'>" + "\n" +
//			"      Aggregation by suite<br>" + "\n" +
//			"        <div id='chart2a'>" + "\n" +
//			"          <canvas id='canvas2a' width='240' height='240'></canvas>" + "\n" +
//			"        </div>" + "\n" +
//			"      </td>" + "\n" +
//			"      <td align='center' colspan='3'>" + "\n" +
//			"      Aggregation by test<br>" + "\n" +
//			"        <div id='chart2b'>" + "\n" +
//			"          <canvas id='canvas2b' width='240' height='240'></canvas>" + "\n" +
//			"        </div>" + "\n" +
//			"      </td>" + "\n" +
//			"    </tr>" +  "\n" +
//			"    <tr>" +  "\n" +
//			"      <td style='width:10%'></td>" +  "\n" +
//			"      <td style='width:30%'><div id='legend2a'></div></td>" + "\n" +
//			"      <td style='width:10%'></td>" +  "\n" +
//			"      <td style='width:10%'></td>" +  "\n" +
//			"      <td style='width:30%'><div id='legend2b'></div></td>" + "\n" +
//			"      <td style='width:10%'></td>" +  "\n" +
//			"    </tr>" + "\n" +
//			"  </table>" + "\n\n" + "  <br>" + "\n\n" +
//
//			"  <table id='tableStyle' class='width_index'>" + "\n" +
//			"    <tr>" + "\n" +
//			"      <th style='width:20px'>" + "\n" +
//			"          <img src='resources/img/expand.png' class='plusminus' id='expand_all_suites' onclick='expand_suites()'>" + "\n" +
//			"          <img src='resources/img/collapse.png' class='plusminus' id='collapse_all_suites' onclick='collapse_suites()' style='display:none'></th>" + "\n" +
//			"      <th>Suite</th><th width='15%'>Execution time</th><th width='8%'>Details</th><th width='8%'>Status</th>" + "\n" +
//			"    </tr>";
//		HtmlWriter.println(content);
//	}
//
//	/**
//	 * Print the table rows.
//	 */
//	private void print_rows() {
//		//Iterating over the suites metadata
//		suite_count = Metadata.size();
//		for( int i = 0; i != suite_count; i++ ) {
//			SuiteMetadata suite_md = Metadata.get(i);			
//			//print the suite result table row
//			print_suite_row(i+1, suite_md.name, suite_md.time, suite_md.filename, suite_md.status);
//			//Iterating over the @test-annotated methods metadata
//			for( int j = 0; j != suite_md.size(); j++ ) {
//				print_method_row(i+1,
//								suite_md.methods.get(j).order,
//								suite_md.methods.get(j).name,
//								suite_md.methods.get(j).parameters,
//								suite_md.methods.get(j).time,
//								suite_md.filename,
//								suite_md.methods.get(j).status);
//			}
//			if( suite_md.hasMutiThreads )
//				print_method_row(i+1, 0, "<span class='multiThread'>No support for multi-threaded tests</span>", null, "", suite_md.filename, suite_md.status);
//		}
//	}
//	
//	/**
//	 * Print a suite execution result table row.
//	 * @param order The order of execution of the suite.
//	 * @param suite The suite name.
//	 * @param time The suite execution time.
//	 * @param file The suite report filename.
//	 * @param status The suite execution status.
//	 */
//	private void print_suite_row(int order, String suite, String time, String file, Status status) {
//		HtmlWriter.println(
//			"    <tr class='row_suite'>" + "\n" +
//			"      <td align='center'>" + "\n" +
//			"          <img src='resources/img/expand.png' class='plusminus' id='expand_suite_" + order + "' onclick='expand_suite(" + order + ")'>" + "\n" +
//			"          <img src='resources/img/collapse.png' class='plusminus' id='collapse_suite_" + order + "' onclick='collapse_suite(" + order + ")' style='display:none'></td>" + "\n" +
//			"      <td id='ttp" + order + "' title=''>" + suite + "</td>" + "\n" +
//			"      <td align='right'>" + time + "</td>" + "\n" +
//			"      <td align='center'><a target='_blank' href='" + file + "'>details</a></td>");
//		String icon = "";
//		String piechart = "";
//		if( Report4s.suite_status_content.equals("icon") ) {
//			switch(status) {
//				case PASSED : icon = "pass.png"; break;
//				case FAILED : icon = "fail.png"; break;
//				case SKIPPED : icon = "skip.png"; break;
//				case INCOMPLETE : icon = "incomplete.png"; break;
//				default : break;
//			}
//			HtmlWriter.println(
//				"      <td align='center'><img src='resources/img/" + icon + "' class='icon'></td>" + "\n" +
//				"    </tr>");
//		} else { //Report4s.suite_status_content == "piechart"
//			piechart = "<div><canvas id='canvas_suite_" + order + "' width='" + Report4s.suite_status_size + "' height='" + Report4s.suite_status_size + "'></canvas></div>";
//			HtmlWriter.println(
//				"      <td align='center'>" + piechart + "</td>" + "\n" +
//				"    </tr>");
//		}
//	}
//
//	/**
//	 * Print a @test-annotated method execution result table row.
//	 * @param suite The number of the suite associated to the test.
//	 * @param order The order of execution of the test within the suite.
//	 * @param name The test name.
//	 * @param parameters The test parameters.
//	 * @param time The test execution time.
//	 * @param file The filename of the suite report.
//	 * @param status The test execution status.
//	 */
//	private void print_method_row(int suite, int order, String name, String parameteres, String time, String file, Status status) {
//		name = parameteres == null ? name : name + "(" + parameteres + ")";
//		name = name.length() > 90 ? name.substring(0, 90) + "..." : name;
//		file += "#test_" + order;
//		HtmlWriter.println(
//			"    <tr name='suite_" + suite + "' class='row_test' style='display:none'>" + "\n" +
//			"      <td></td>" + "\n" +
//			"      <td>" + name + "</td>" + "\n" +
//			"      <td align='right'>" + time + "</td>" + "\n" +
//			"      <td align='center'><a target='_blank' href='" + file + "'>details</a></td>");
//		String icon = "";
//		switch(status) {
//			case PASSED : icon = "pass.png"; break;
//			case FAILED : icon = "fail.png"; break;
//			case SKIPPED : icon = "skip.png"; break;
//			case FAILED_WITHIN_PERCENTAGE : icon = "fail_percentage.png"; break;
//			default : break;
//		}
//
//		HtmlWriter.println(
//			"      <td align='center'><img src='resources/img/" + icon + "' class='icon'></td>" + "\n" +
//			"    </tr>");
//	}
//
//	/**
//	 * Print the end of the homepage file report.
//	 */
//	private void print_tail() {
//		HtmlWriter.println(
//			"  </table>" + "\n\n" + 
//			"  <br>" + "\n\n" +
//			"  <table style='width:85%;margin:0px auto 0px'>" + "\n" +
//			"    <tr>" + "\n" +
//			"      <td style='text-align:right'><a href='" + Report4s.report_excel + "' style='vertical-align:middle;font-size:12px;color:black;text-decoration:none'>Export</a></td>" + "\n" +
//			"      <td style='width:32px'><a href='" + Report4s.report_excel + "'><img src='resources/img/excel.png' style='height:32px;width:32px'><a></td>" + "\n" +
//			"    </tr>" + "\n" +
//			"  </table>" + "\n\n" +
//			"  <br><br><br>" + "\n\n" +
//			"</body>" + "\n" +
//			"</html>" + "\n");
//	}
//
//	/**
//	 * Print the piecharts data in a separate JavaScript file.
//	 */
//	private void print_piecharts_data() {
//		String content = new String();
//
//		content = "";
//		//Print the summary piecharts data
//		content += Utils.getPiechartData("suite_piechart", Metadata.suites_passed, Metadata.suites_failed, 0, Metadata.suites_skipped);
//		content += Utils.getPiechartData("test_piechart", Metadata.tests_passed, Metadata.tests_failed, Metadata.tests_failed_pct, Metadata.tests_skipped);
//
//		//Print each individual suite piechart data
//		if( Report4s.suite_status_content.equals("piechart") ) {
//			for( int i = 0; i != suite_count; i++ ) {
//				SuiteMetadata suite_md = Metadata.get(i);
//				content += Utils.getPiechartData("piechart_suite_" + (i+1), suite_md.tests_passed, suite_md.tests_failed, suite_md.tests_failed_pct, suite_md.tests_skipped);
//			}
//		}
//		
//		//Print the onload function, which draws the piecharts
//		content +=
//			"window.onload = function() {" + "\n" +
//			"	//The summary piecharts" + "\n" +
//			"	if( getPieChartAggregation() == 'suite' ) {" + "\n" +
//			"		document.getElementById('table1').style.display = '';" + "\n" +
//			"		var ctx = document.getElementById('canvas1').getContext('2d');" + "\n" +
//			"		var chart = new Chart(ctx).Pie(suite_piechart);" + "\n" +
//			"		legend(document.getElementById('legend1'), suite_piechart, chart, \"<%=label%>: <%=value%> suite(s)\");" + "\n" +
//			"	} else if( getPieChartAggregation() == 'test' ) {" + "\n" +
//			"		document.getElementById('table1').style.display = '';" + "\n" +
//			"		var ctx = document.getElementById('canvas1').getContext('2d');" + "\n" +
//			"		var chart = new Chart(ctx).Pie(test_piechart);" + "\n" +
//			"		legend(document.getElementById('legend1'), test_piechart, chart, \"<%=label%>: <%=value%> test(s)\");" + "\n" +
//			"	} else if( getPieChartAggregation() == 'both' ) {" + "\n" +
//			"		document.getElementById('table2').style.display = '';" + "\n" +
//			"		var ctx1 = document.getElementById('canvas2a').getContext('2d');" + "\n" +
//			"		var chart1 = new Chart(ctx1).Pie(suite_piechart);" + "\n" +
//			"		legend(document.getElementById('legend2a'), suite_piechart, chart1, \"<%=label%>: <%=value%> suite(s)\");" + "\n" +
//			"		var ctx2 = document.getElementById('canvas2b').getContext('2d');" + "\n" +
//			"		var chart2 = new Chart(ctx2).Pie(test_piechart);" + "\n" +
//			"		legend(document.getElementById('legend2b'), test_piechart, chart2, \"<%=label%>: <%=value%> test(s)\");" + "\n" +
//			"	}" + "\n\n";
//				
//		if( Report4s.suite_status_content.equals("piechart") ) {
//			if( suite_count > 0 )
//				content += "	//The individual suite piecharts" + "\n";
//			for( int i = 0; i != suite_count; i++ ) {
//				content +=
//				"	var ctx_" + "suite_" + (i+1) + "= document.getElementById('canvas_suite_" + (i+1) + "').getContext('2d');" + "\n" +
//				"	var chart_" + "suite_" + (i+1) + " = new Chart(ctx_" + "suite_" + (i+1) + ").Pie(piechart_suite_" + (i+1) + ", {showTooltips: false, segmentShowStroke: false});" + "\n";
//			}
//		}
//		//Print some utility functions
//		content +=		
//			"}" + "\n\n" +
//	
//			"function getNumberOfSuites() {" + "\n" +
//			"	return " + suite_count + ";" + "\n" +
//			"}" + "\n\n" +
//			
//			"function getPieChartAggregation() {" + "\n" +
//			"	return '" + Report4s.piechart_aggregation + "';" + "\n" +
//			"}" + "\n";
//
//		HtmlWriter.openFile(Report4s.report_dir + File.separator + "resources" + File.separator + "js" + File.separator + "piecharts.js", false);
//		HtmlWriter.println(content);
//		HtmlWriter.closeFile();		
//	}
//	
//	/**
//	 * Print the tooltips data in a separate JavaScript file.
//	 */
//	private void print_tooltip_data() {
//		int tests_passed, tests_failed, tests_failed_pct, tests_skipped;
//		String content = "$(function() {" + "\n\n";
//
//		for( int i = 0; i != suite_count; i++ ) {
//			SuiteMetadata suite_md = Metadata.get(i);
//			tests_passed = suite_md.tests_passed;
//			tests_failed = suite_md.tests_failed;
//			tests_skipped = suite_md.tests_skipped;
//			tests_failed_pct = suite_md.tests_failed_pct;
//
//			if( tests_passed == 0 && tests_failed == 0 && tests_skipped == 0 && tests_failed_pct == 0 )
//				continue;
//			
//			content +=
//				"	$('#ttp" + (i+1) + "').tooltip({" + "\n" +
//				"		content: \"<table><tr>";
//			if(tests_passed > 0)
//				content += "<td><div class='ttp ttp_passed'></div></td><td class='ttp_text'>Passed: " + tests_passed + "</td>";
//			if(tests_failed > 0)
//				content += "<td><div class='ttp ttp_failed'></div></td><td class='ttp_text'>Failed: " + tests_failed + "</td>";
//			if(tests_failed_pct > 0)
//				content += "<td><div class='ttp ttp_failed_pct'></div></td><td class='ttp_text'>Failed w/n %: " + tests_failed_pct + "</td>";
//			if(tests_skipped > 0)
//				content += "<td><div class='ttp ttp_skipped'></div></td><td class='ttp_text'>Skipped: " + tests_skipped + "</td>";
//			
//			content +=
//				"</tr></table>\"," + "\n" +
//				"		track: true," + "\n" +
//				"		position: { my: 'left bottom' }" + "\n" +
//				"	});" + "\n\n";
//		}
//		content += "});" + "\n";
//		
//		HtmlWriter.openFile(Report4s.report_dir + File.separator + "resources" + File.separator + "js" + File.separator + "tooltips.js", false);
//		HtmlWriter.println(content);
//		HtmlWriter.closeFile();
//	}
//
//}
