package com.excelhk.openapi.demoservice.utils.constants;

/**
 *
 * @author anita
 *
 */
public class DemoConstants {
    /** public static final String REQUEST_TOKEN_HEADER = "connection-type";*/
    public static final String CONNECT_TYPE_FTP = "ftp";
    public static final String PROD_TYPE_DEPOSIT = "Deposits";
    public static final String PROD_TYPE_LOANS = "Loans";
    public static final String SLASH="/";
    public static final String BACK_SLASH_SLASH="\\";
    public static final String ASTERISK="*";
    public static final String PERIOD = ".";
    public static final String LINE_BREAK="\r\n";
    public static final String NEW_LINE="\n";

    /**
     * Exception message
     *
     * Auth fail
     * Connection timed out
     * timeout:
     * Connection refused
     */
    public static final String AUTH_FAIL = "Auth fail";
    public static final String CONN_TIMED_OUT = "Connection timed out";
    public static final String TIME_OUT = "timeout:";
    public static final String CONN_REFUSED = "Connection refused";

    // Event Handle type
    public static final String EVENT_TYPE_DOWNLOAD="DOWNLOAD";
    public static final String EVENT_TYPE_PROD="PROD";
    public static final String EVENT_TYPE_DETAILS="DETAILS";
    // type
    public static final String FTP_IN = "IN";
    public static final String FTP_OUT="OUT";

}
