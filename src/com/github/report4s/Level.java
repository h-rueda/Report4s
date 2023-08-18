package com.github.report4s;

/**
 * Enumeration of the log levels supported by Report4s.
 */
public enum Level {
    PASSED,
    FAILED,
    INFO,
    WARNING,
    DEBUG,
    TRACE,
    ERROR,
    /**
     * Used to display the status of multi-threaded tests (not supported by Report4s).
     */
    UNKNOWN,
}
