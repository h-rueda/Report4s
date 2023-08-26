package com.github.report4s;

import java.io.File;
import java.util.List;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

/**
 * Generates the report home page (index) file.
 */
public class Reporter implements IReporter {

    /**
     * invoked after all suites have run.
     * Generates the report homepage (index) file.
     */
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> isuites, String outputDirectory) {
        if (!verifyPrecondition())
            return;
        //Generate the HTML summary report
        HtmlWriter.openFile(Report4s.report_dir + File.separator + Report4s.report_homepage, false);
        print_head();
        print_rows();
        print_tail();
        HtmlWriter.closeFile();

        //Generate JavaScript files
        print_auxiliary_function();
        if (Report4s.suite_tooltips)
            print_tooltip_data();
    }

    /**
     * Print the beginning of the report homepage file.
     */
    private void print_head() {
        String content =
            "<html>" + "\n" +
            "    <head>" + "\n" +
            "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />" + "\n" +
            "        <title>" + Report4s.report_title + "</title>" + "\n" +
            "        <link rel=\"stylesheet\" type=\"text/css\" href=\"assets/css/design.css\" />" + "\n";
        if (Report4s.report_css != null)
            content +=
            "        <link rel=\"stylesheet\" type=\"text/css\" href=\"assets/css/" + Report4s.report_css + "\" />" + "\n";
        content +=
            "        <link rel=\"stylesheet\" type=\"text/css\" href=\"assets/css/jquery-ui.css\" />" + "\n" +
            "        <link rel=\"stylesheet\" type=\"text/css\" href=\"assets/css/jquery-ui-override.css\" />" + "\n" +
            "        <script src=\"assets/js/jquery.js\"></script>" + "\n" +
            "        <script src=\"assets/js/jquery-ui.js\"></script>" + "\n" +
            "        <script src=\"assets/js/events.js\"></script>" + "\n";
        if (Report4s.suite_tooltips)
            content +=
            "        <script src=\"assets/js/tooltips.js\"></script>" + "\n";
        content +=
            "    </head>" + "\n\n" +
            "    <body>" + "\n" +
            "        <h1 style=\"text-align:center\">" + Report4s.report_title + "</h1>" + "\n\n" +

            "        <div style=\"margin-left: 7%\">\n" +
            "            <p>" + "\n" +
            "                <strong>Summary</strong>" + "\n" +
            "                <br/>" + "\n" +
            "                " + Metadata.size() + " suites ran in " + Utils.getExecutionLabel(Metadata.exec_time) + "\n" +
            "            </p>\n\n" +

            "            <input type=\"checkbox\" checked=\"true\" status=\"passed\" name=\"filter_checkbox\" onChange=\"filter_table(this)\">\n" +
            "            <label>" + (Metadata.suites_passed + Metadata.suites_passed_warning) + " passed</label>\n" +
            "            <input type=\"checkbox\" checked=\"true\" status=\"skipped\" name=\"filter_checkbox\" onChange=\"filter_table(this)\">\n" +
            "            <label>" + Metadata.suites_skipped + " skipped</label>\n" +
            "            <input type=\"checkbox\" checked=\"true\" status=\"failed\" name=\"filter_checkbox\" onChange=\"filter_table(this)\">\n" +
            "            <label>" + Metadata.suites_failed + " failed</label>\n\n" +

            "            <table>\n" +
            "                <tr>\n" +
            "                    <td><a href=\"javascript:expand_suites();\">Show all details</a></td>\n" +
            "                    <td> / </td>\n" +
            "                    <td><a href=\"javascript:collapse_suites();\">Hide all details</a></td>\n" +
            "                </tr>\n" +
            "            </table>\n" +
            "        </div>\n\n" +

            "        <br/>\n\n" +

            "        <table id=\"tableStyle\" class=\"width_index\">" + "\n" +
            "            <tr>" + "\n" +
            "                <th style=\"width:20px\">" + "\n" +
            "                    <img src=\"assets/img/expand.png\" class=\"plusminus\" id=\"expand_all_suites\" onclick=\"expand_suites()\">" + "\n" +
            "                    <img src=\"assets/img/collapse.png\" class=\"plusminus\" id=\"collapse_all_suites\" onclick=\"collapse_suites()\" style=\"display:none\"></th>" + "\n" +
            "                <th>Suite</th>" + "\n" +
            "                <th style=\"width:12%\">Execution time</th>" + "\n" +
            "                <th style=\"width:8%\">Details</th>" + "\n" +
            "                <th style=\"width:8%\">Status</th>" + "\n" +
            "            </tr>";
        HtmlWriter.println(content);
    }

    /**
     * Print the table rows.
     */
    private void print_rows() {
        //Iterating over the suites metadata
        for (int i = 0; i != Metadata.size(); i++) {
            SuiteMetadata suite_md = Metadata.get(i);            
            //print the suite result table row
            print_suite_row(i+1, suite_md.name, suite_md.time, suite_md.filename, suite_md.status);
            //Iterating over the test methods metadata
            for (int j = 0; j != suite_md.size(); j++) {
                print_method_row(i+1,
                                suite_md.methods.get(j).order,
                                suite_md.methods.get(j).name,
                                suite_md.methods.get(j).parameters,
                                suite_md.methods.get(j).time,
                                suite_md.filename,
                                suite_md.methods.get(j).status,
                                suite_md.status);
            }
            if (suite_md.hasMutiThreads)
                print_method_row(i+1, 0, "<span class=\"multiThread\">No support for multi-threaded tests</span>", null, "", suite_md.filename, Status.EMPTY, suite_md.status);
        }
    }

    /**
     * Print a suite execution result table row.
     * @param order The order of execution of the suite.
     * @param suite The suite name.
     * @param time The suite execution time.
     * @param file The suite report filename.
     * @param status The suite execution status.
     */
    private void print_suite_row(
    		int order,
    		String suite,
    		String time,
    		String file,
    		Status status) {
        //Parse status to determine row icon and CSS class name.
        String icon = Utils.getIconFilename(status);
        String class_name = Utils.appendStatusClassName("row_suite", status);
        HtmlWriter.println(
            "            <tr class=\"" + class_name + "\">" + "\n" +
            "                <td style=\"text-align:center\">" + "\n" +
            "                    <img src=\"assets/img/expand.png\" class=\"plusminus\" id=\"expand_suite_" + order + "\" onclick=\"expand_suite(" + order + ")\">" + "\n" +
            "                    <img src=\"assets/img/collapse.png\" class=\"plusminus\" id=\"collapse_suite_" + order + "\" onclick=\"collapse_suite(" + order + ")\" style=\"display:none\"></td>" + "\n" +
            "                <td id=\"ttp" + order + "\" title=\"\">" + suite + "</td>" + "\n" +
            "                <td style=\"text-align:right\">" + time + "</td>" + "\n" +
            "                <td style=\"text-align:center\"><a target=\"_blank\" href=\"" + file + "\">details</a></td>" + "\n" +
            "                <td style=\"text-align:center\"><img src=\"assets/img/" + icon + "\" class=\"icon\"></td>" + "\n" +
            "            </tr>");
    }

    /**
     * Print a test method execution result table row.
     * @param suite The number of the suite associated to the test.
     * @param order The order of execution of the test within the suite.
     * @param name The test name.
     * @param parameters The test parameters.
     * @param time The test execution time.
     * @param file The filename of the suite report.
     * @param status The test execution status.
     * @param suite_status The suite execution status.
     */
    private void print_method_row(
            int suite,
            int order,
            String name,
            String parameteres,
            String time,
            String file,
            Status status,
            Status suite_status) {
        String class_name = Utils.appendStatusClassName("row_test", suite_status);
        String icon = Utils.getIconFilename(status);
        name = (parameteres == null) ? name : name + "(" + parameteres + ")";
        name = (name.length() > 90) ? name.substring(0, 90) + "..." : name;
        file += "#test_" + order;
        HtmlWriter.println(
            "            <tr name=\"suite_" + suite + "\" class=\"" + class_name + "\" style=\"display:none\">" + "\n" +
            "                <td></td>" + "\n" +
            "                <td>" + name + "</td>" + "\n" +
            "                <td style=\"text-align:right\">" + time + "</td>" + "\n" +
            "                <td style=\"text-align:center\"><a target=\"_blank\" href=\"" + file + "\">details</a></td>" + "\n");
        if (status == Status.EMPTY)
            HtmlWriter.println(
            "                <td align=\"center\"></td>" + "\n");
        else
            HtmlWriter.println(
            "                <td align=\"center\"><img src=\"assets/img/" + icon + "\" class=\"icon\"></td>" + "\n");
            HtmlWriter.println(
            "            </tr>");
    }

    /**
     * Print the end of the homepage file report.
     */
    private void print_tail() {
        HtmlWriter.println(
            "        </table>" + "\n\n" + 
            "        <br><br><br>" + "\n\n" +
            "    </body>" + "\n" +
            "</html>" + "\n");
    }

    /**
     * Print auxiliary JavaScript function in events.js file.
     */
    private void print_auxiliary_function() {
        String content =
            "function getNumberOfSuites() {" + "\n" +
            "    return " + Metadata.size() + ";" + "\n" +
            "}" + "\n";
        HtmlWriter.openFile(Report4s.report_dir + File.separator + "assets" + File.separator + "js" + File.separator + "events.js", true);
        HtmlWriter.println(content);
        HtmlWriter.closeFile();
    }
    
    /**
     * Print the tooltips data in a separate JavaScript file.
     */
    private void print_tooltip_data() {
        int tests_passed, tests_failed, tests_failed_pct, tests_skipped;
        String content = "$(function() {" + "\n\n";

        for (int i = 0; i != Metadata.size(); i++) {
            SuiteMetadata suite_md = Metadata.get(i);
            tests_passed = suite_md.tests_passed;
            tests_failed = suite_md.tests_failed;
            tests_skipped = suite_md.tests_skipped;
            tests_failed_pct = suite_md.tests_failed_pct;

            if (tests_passed == 0 && tests_failed == 0 && tests_skipped == 0 && tests_failed_pct == 0)
                continue;

            content +=
                "    $(\"#ttp" + (i+1) + "\").tooltip({" + "\n" +
                "        content: \"<table><tr>";
            if (tests_passed > 0)
                content += "<td><div class=\\\"ttp ttp_passed\\\"></div></td><td class=\\\"ttp_text\\\">Passed: " + tests_passed + "</td>";
            if (tests_failed > 0)
                content += "<td><div class=\\\"ttp ttp_failed\\\"></div></td><td class=\\\"ttp_text\\\">Failed: " + tests_failed + "</td>";
            if (tests_failed_pct > 0)
                content += "<td><div class=\\\"ttp ttp_failed_pct\\\"></div></td><td class=\\\"ttp_text\\\">Failed w/n %: " + tests_failed_pct + "</td>";
            if (tests_skipped > 0)
                content += "<td><div class=\\\"ttp ttp_skipped\\\"></div></td><td class=\\\"ttp_text\\\">Skipped: " + tests_skipped + "</td>";

            content +=
                "</tr></table>\"," + "\n" +
                "        track: true," + "\n" +
                "        position: { my: \"left bottom\" }" + "\n" +
                "    });" + "\n\n";
        }
        content += "});" + "\n";

        HtmlWriter.openFile(Report4s.report_dir + File.separator + "assets" + File.separator + "js" + File.separator + "tooltips.js", false);
        HtmlWriter.println(content);
        HtmlWriter.closeFile();
    }

    /**
     * Whether the conditions are met before logging.
     * @return
     */
    private boolean verifyPrecondition() {
        return Report4s.extracted && SuiteListener.registered && TestListener.registered;
    }

}
