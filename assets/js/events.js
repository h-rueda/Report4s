function expand_trace(i) {
    document.getElementById("expand_trace_" + i).style.display = "none";
    document.getElementById("collapse_trace_" + i).style.display = "";
    document.getElementById("trace_" + i).style.display = "";
}

function collapse_trace(i) {
    document.getElementById("expand_trace_" + i).style.display = "";
    document.getElementById("collapse_trace_" + i).style.display = "none";
    document.getElementById("trace_" + i).style.display = "none";
}

function expand_suite(i) {
    document.getElementById("expand_suite_" + i).style.display = "none";
    document.getElementById("collapse_suite_" + i).style.display = "";

    var x = document.getElementsByName("suite_" + i);
    for (k = 0; k < x.length; k++)
        x[k].style.display = "";
}

function collapse_suite(i) {
    document.getElementById("expand_suite_" + i).style.display = "";
    document.getElementById("collapse_suite_" + i).style.display = "none";

    var x = document.getElementsByName("suite_" + i);
    for (k = 0; k < x.length; k++)
        x[k].style.display = "none";
}

function expand_suites() {
    document.getElementById("expand_all_suites").style.display = "none";
    document.getElementById("collapse_all_suites").style.display = "";

    var s = getNumberOfSuites();
    for (i = 0; i != s; i++)
        expand_suite(i+1);
}

function collapse_suites() {
    document.getElementById("expand_all_suites").style.display = "";
    document.getElementById("collapse_all_suites").style.display = "none";

    var s = getNumberOfSuites();
    for (i = 0; i != s; i++)
        collapse_suite(i+1);
}

function filter_table(elem) {
    const status = elem.getAttribute("status");
    const class_suites = "row_suite " + status;
    rows = document.getElementsByClassName(class_suites);
    for (i = 0; i < rows.length; i++)
        rows[i].hidden = !elem.checked;
    const class_tests = "row_test " + status;
    rows = document.getElementsByClassName(class_tests);
    for (i = 0; i < rows.length; i++)
        rows[i].hidden = !elem.checked;
}

