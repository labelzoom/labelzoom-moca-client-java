package com.labelzoom.moca.exceptions;

import java.sql.ResultSet;

public class MocaException extends Exception
{
    private final int code;
    private ResultSet results;

    public MocaException(final String message, final int code)
    {
        super(message);
        this.code = code;
    }

    public int getErrorCode() { return code; }

    public ResultSet getResults() { return results; }
    public void setResults(final ResultSet results) { this.results = results; }
}
