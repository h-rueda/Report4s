package com.github.report4s;

/**
 * Enumeration of {test|suite} execution status.
 */
enum Status {
    PASSED,
    FAILED,
    SKIPPED,
    PASSED_WITH_WARNING,
    FAILED_WITHIN_PERCENTAGE,
    UNKNOWN,
    EMPTY,    //for multi-threaded tests
}
